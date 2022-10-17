import { useGetMembers, usePostMentionNotifications } from 'apis/feedApi';
import { useState, useEffect, useRef, KeyboardEventHandler, RefObject } from 'react';
import { getCursorPosition } from 'utils';

import useMutationObserver from 'hooks/useMutationObserver';

import usePopover from 'components/CommentInput/usePopover';

import { MAX_TEXTAREA_LENGTH } from 'constants/domain';

const DEFAULT_INPUT_HEIGHT = '1.5rem';

const resizeHeight = (element: HTMLElement) => {
  // 입력 값을 지웠을 때, 댓글 입력창의 높이를 줄이기 위한 코드
  element.style.height = DEFAULT_INPUT_HEIGHT;
  element.style.height = element.scrollHeight + 'px';
};

type useMentionProps<T extends HTMLElement> = {
  commentInputRef: RefObject<T>;
  setContent: (arg0: string) => void;
  mentionedMemberIds: number[];
  setMentionedMemberIds: React.Dispatch<React.SetStateAction<number[]>>;
};
const useMention = <T extends HTMLElement>({
  commentInputRef,
  setContent,
  mentionedMemberIds,
  setMentionedMemberIds,
}: useMentionProps<T>) => {
  // ------- 멘션 알림 기능 ------------------------
  const isFirstRendered = useRef(true);
  const [filterValue, setFilterValue] = useState('');
  const ABSENCE_SYMBOL_POSITION = -1;

  const lastMentionSymbolPositionRef = useRef(ABSENCE_SYMBOL_POSITION);
  const isFilterValueInitiatedRef = useRef(false);
  const isPrevPressBackspace = useRef(false);
  const prevCursorPosition = useRef(0);

  const { isPopoverOpen, handleOpenPopover, handleClosePopover, selectMember } =
    usePopover({
      commentInputRef,
      mentionedMemberIds,
      setMentionedMemberIds,
      lastMentionSymbolPosition: lastMentionSymbolPositionRef.current,
      filterValue,
      setFilterValue,
      isFilterValueInitiated: isFilterValueInitiatedRef.current,
    });

  const {
    isFetching: isFetchingMembers,
    data: membersData,
    hasNextPage: hasNextMembersPage,
    fetchNextPage: fetchNextMembersPage,
    refetch: refetchMembers,
  } = useGetMembers(
    { filter: filterValue },
    {
      onSuccess: (data) => {
        if (data.pages[0].data.length === 0) {
          handleClosePopover();
          return;
        }

        handleOpenPopover();
      },
      enabled: false,
      suspense: false,
      staleTime: 600000, // 10분
    },
  );

  const {
    mutate: postMentionNotifications,
    isLoading: isLoadingPostMentionNotifications,
  } = usePostMentionNotifications();

  useEffect(() => {
    if (isFirstRendered.current) {
      isFirstRendered.current = false;
      return;
    }
    if (isFilterValueInitiatedRef.current === true) {
      return;
    }

    refetchMembers();
  }, [filterValue]);

  useEffect(() => {
    const handleKeydown: KeyboardEventHandler<HTMLDivElement> = (event) => {
      const { key } = event;

      if (key === 'Backspace' || key === 'Delete') {
        detectMentionSymbolWhenTextDeleted();
        return;
      }

      isPrevPressBackspace.current = false;
      prevCursorPosition.current = 0;

      if (key === 'ArrowLeft') {
        detectLeftEscapingMentionArea();
        return;
      }

      if (key === 'ArrowRight') {
        detectRightEscapingMentionArea();
        return;
      }
    };
    commentInputRef.current!.addEventListener('keydown', handleKeydown);
  }, []);

  // inputChangeHandler 시작
  const inputChangeHandler: MutationCallback = (mutations) => {
    mutations.forEach((mutation) => {
      if (mutation.type === 'childList') {
        if (
          mutation.removedNodes.length > 0 &&
          mutation.removedNodes[0].nodeName === 'SPAN'
        ) {
          const targetNode = mutation.removedNodes[0] as HTMLElement;
          const deletedMemberId = Number(targetNode.getAttribute('data-member-id'));
          const deletedIndex = mentionedMemberIds.indexOf(deletedMemberId);
          if (deletedIndex > -1) {
            mentionedMemberIds.splice(deletedIndex, 1);
          }

          setMentionedMemberIds((prevMentionMemberIds) => {
            const copiedMentionMembersId = [...prevMentionMemberIds];

            const deletedIndex = copiedMentionMembersId.indexOf(deletedMemberId);

            if (deletedIndex > -1) {
              copiedMentionMembersId.splice(deletedIndex, 1);
            }

            return copiedMentionMembersId;
          });
        }
      }
    });

    const hasSymbolPosition =
      lastMentionSymbolPositionRef.current !== ABSENCE_SYMBOL_POSITION;

    const isCurrentCharacterWhiteSpace = (text: string) =>
      text[getCursorPosition(commentInputRef.current!)! - 1] === ' ';

    if (isFilterValueInitiatedRef.current === true) {
      isFilterValueInitiatedRef.current = false;
    }

    const setNicknameAfterMentionSymbol = (text: string) => {
      if (isCurrentCharacterWhiteSpace(text)) {
        lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
        setFilterValue(''); // 초기화
        isFilterValueInitiatedRef.current = true;

        handleClosePopover();
      } else {
        // 건들지 마시오
        setFilterValue(
          text.slice(
            lastMentionSymbolPositionRef.current,
            getCursorPosition(commentInputRef.current!)!,
          ),
        );
      }
    };

    resizeHeight(commentInputRef.current!);
    const { innerText } = commentInputRef.current!;

    if (hasSymbolPosition) {
      setNicknameAfterMentionSymbol(innerText);

      if (getCursorPosition(commentInputRef.current!)! === 0) {
        handleClosePopover();
      }
    } else {
      detectMentionSymbolWhenTextAdded(innerText);
    }

    setContent(innerText.slice(0, MAX_TEXTAREA_LENGTH));
  };
  // inputChangeHandler 끝

  // 문자열 새로 입력됐을 때
  const detectMentionSymbolWhenTextAdded = (text: string) => {
    const cursorPosition = getCursorPosition(commentInputRef.current!)!;
    const currentCharacter = text[cursorPosition - 1];

    // 1. 현재 cursor 포지션의 바로 앞이 @인 경우에만 @ 이벤트를 호출한다.
    if (currentCharacter !== '@') {
      handleClosePopover();
      return;
    }

    // 2. 그런데 호출하려는 @의 앞이 공백이 아니면 호출하지 않는다.
    if (cursorPosition !== 1 && text.length >= 2 && text[cursorPosition - 2] !== ' ') {
      return;
    }

    lastMentionSymbolPositionRef.current = cursorPosition;
    refetchMembers();
    handleOpenPopover();
  };

  const detectMentionSymbolWhenTextDeleted = () => {
    const cursorPosition = getCursorPosition(commentInputRef.current!)!;

    const { innerText } = commentInputRef.current!;

    useRef;
    // 지우려는 element 첫 번째 자식인 경우 contentEditable 부모에서 해당 element를 삭제할 수 없는 이슈 때문에
    // 다음과 같은 분기문을 통해 첫 번째 element도 삭제 가능하도록 이슈 해결함
    if (
      commentInputRef.current!.childNodes.length === 3 &&
      commentInputRef.current!.childNodes[1].nodeName &&
      commentInputRef.current!.childNodes[0].textContent === ''
    ) {
      // init 3종 세트
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      isFilterValueInitiatedRef.current = true;
      setFilterValue('');
      handleClosePopover();

      commentInputRef.current!.textContent = ' ';

      return;
    }

    if (!innerText.includes('@')) {
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;

      return;
    }

    const mentionSymbolPosition = innerText.lastIndexOf('@', cursorPosition);

    const targetText = innerText.slice(mentionSymbolPosition + 1, cursorPosition - 1);
    // 슬라이스 한 문자열 내부에 공백이 있나
    if (cursorPosition - 1 === mentionSymbolPosition) {
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      return;
    }

    if (targetText.includes(' ')) {
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      return;
    }

    // mentionSymbolPosition 앞에 공백이 있나 && mentionSymbolPosition 위치가 첫번째이다.
    if (mentionSymbolPosition !== 0 && innerText[mentionSymbolPosition - 1] !== ' ') {
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      return;
    }

    lastMentionSymbolPositionRef.current = mentionSymbolPosition + 1;
    isPrevPressBackspace.current = true;
    prevCursorPosition.current = cursorPosition;
  };

  const detectLeftEscapingMentionArea = () => {
    const cursorPosition = getCursorPosition(commentInputRef.current!)!;
    const { innerText } = commentInputRef.current!;

    if (innerText[cursorPosition - 2] === ' ') {
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      setFilterValue('');
      isFilterValueInitiatedRef.current = true;
      handleClosePopover();
    }
  };

  const detectRightEscapingMentionArea = () => {
    const cursorPosition = getCursorPosition(commentInputRef.current!)!;
    const { innerText } = commentInputRef.current!;

    if (innerText[cursorPosition] === ' ') {
      // init 3종 세트
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      setFilterValue('');
      isFilterValueInitiatedRef.current = true;
      handleClosePopover();
    }
  };

  useMutationObserver<T>(commentInputRef, inputChangeHandler);

  return {
    postMentionNotifications,
    isLoadingPostMentionNotifications,
    isPopoverOpen,
    handleClosePopover,
    membersData,
    hasNextMembersPage,
    fetchNextMembersPage,
    selectMember,
  };
};

export default useMention;
