import { useGetMembers, usePostMentionNotifications } from 'apis/feedApi';
import {
  useState,
  useEffect,
  useRef,
  KeyboardEventHandler,
  FormEventHandler,
  RefObject,
} from 'react';
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

const ABSENCE_SYMBOL_POSITION = -1;

const useMention = <T extends HTMLElement>({
  commentInputRef,
  setContent,
  mentionedMemberIds,
  setMentionedMemberIds,
}: useMentionProps<T>) => {
  // ------- 멘션 알림 기능 ------------------------
  const isFirstRendered = useRef(true);
  const [filterValue, setFilterValue] = useState('');

  // lastMentionSymbolPositionRef
  const lastMentionSymbolPositionRef = useRef(ABSENCE_SYMBOL_POSITION);
  const isFilterValueInitiatedRef = useRef(false);

  const { isPopoverOpen, handleOpenPopover, handleClosePopover, selectMember } =
    usePopover({
      commentInputRef,
      mentionedMemberIds,
      setMentionedMemberIds,
      lastMentionSymbolPosition: lastMentionSymbolPositionRef,
      filterValue,
      setFilterValue,
      isFilterValueInitiated: isFilterValueInitiatedRef,
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

        // 멘션 심볼 포지션(@)이 없다면 아래 함수를 호출하지 않도록 고쳐야한다.
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

  const isNotDeleteSpanNode = (nodes: NodeList) =>
    nodes.length === 0 || nodes[0].nodeName !== 'SPAN';

  const inputChangeHandler: MutationCallback = (mutations) => {
    mutations.forEach((mutation) => {
      if (mutation.type !== 'childList') {
        return;
      }

      if (isNotDeleteSpanNode(mutation.removedNodes)) {
        return;
      }

      const targetNode = mutation.removedNodes[0] as HTMLElement;
      const deletedMemberId = Number(targetNode.getAttribute('data-member-id'));

      setMentionedMemberIds((prevMentionMemberIds) => {
        const copiedMentionMembersId = [...prevMentionMemberIds];

        const deletedIndex = copiedMentionMembersId.indexOf(deletedMemberId);

        if (deletedIndex > -1) {
          copiedMentionMembersId.splice(deletedIndex, 1);
        }

        return copiedMentionMembersId;
      });
    });
  };

  useMutationObserver<T>(commentInputRef, inputChangeHandler);

  useEffect(() => {
    // useGetMembers의 enables 옵션을 false로 했기 때문에 해당 로직은 제거해도 될 거 같다.
    if (isFirstRendered.current) {
      isFirstRendered.current = false;
      return;
    }

    if (isFilterValueInitiatedRef.current) {
      isFilterValueInitiatedRef.current = false;
      return;
    }

    refetchMembers();
  }, [filterValue]);

  const handleKeydownCommentInput: KeyboardEventHandler<HTMLDivElement> = (event) => {
    const { key } = event;

    if (key === 'Backspace' || key === 'Delete') {
      detectMentionSymbolWhenTextDeleted();
      return;
    }

    if (key === 'ArrowLeft') {
      detectLeftEscapingMentionArea();
      return;
    }

    if (key === 'ArrowRight') {
      detectRightEscapingMentionArea();
      return;
    }
  };

  const handleInputCommentInput: FormEventHandler<HTMLDivElement> = () => {
    resizeHeight(commentInputRef.current!);
    const { innerText } = commentInputRef.current!;

    resetIsFilterValueInitiated();

    setContent(innerText.slice(0, MAX_TEXTAREA_LENGTH));
    if (hasNotMentionSymbolPosition) {
      detectMentionSymbolWhenTextAdded(innerText);
      return;
    }

    setNicknameAfterMentionSymbol(innerText);

    if (isStartPosition()) {
      handleClosePopover();
    }
  };

  const resetIsFilterValueInitiated = () => {
    if (isFilterValueInitiatedRef.current) {
      isFilterValueInitiatedRef.current = false;
    }
  };

  const hasNotMentionSymbolPosition =
    lastMentionSymbolPositionRef.current === ABSENCE_SYMBOL_POSITION;

  const setNicknameAfterMentionSymbol = (text: string) => {
    if (isCurrentCharacterWhiteSpace(text)) {
      initializeMention();
      return;
    }

    const detectedFilterValue = text.slice(
      lastMentionSymbolPositionRef.current,
      getCursorPosition(commentInputRef.current!)!,
    );

    setFilterValue(detectedFilterValue);
  };

  const isStartPosition = () => getCursorPosition(commentInputRef.current!) === 0;

  const isCurrentCharacterWhiteSpace = (text: string) =>
    text[getCursorPosition(commentInputRef.current!)! - 1] === ' ';

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

    // onSuccess 안에서 popover를 열기 떄문에 해당 로직은 제거해도 될 거 같다.
    handleOpenPopover();
  };

  const detectMentionSymbolWhenTextDeleted = () => {
    const cursorPosition = getCursorPosition(commentInputRef.current!)!;
    const { innerText } = commentInputRef.current!;

    // 지우려는 element 첫 번째 자식인 경우 contentEditable 부모에서 해당 element를 삭제할 수 없는 이슈 때문에
    // 다음과 같은 분기문을 통해 첫 번째 element도 삭제 가능하도록 이슈 해결함
    if (isFirstMentionTag()) {
      initializeMention();

      // 아래 공백의 타당한 이유가 없다면 제거
      commentInputRef.current!.textContent = '';
      return;
    }

    if (isNotIncludeMentionSymbol(innerText)) {
      // 아래의 초기화 로직이 필요한 이유가 타당하지 않다면 제거
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      return;
    }

    const mentionSymbolPosition = innerText.lastIndexOf('@', cursorPosition);

    const targetText = innerText.slice(mentionSymbolPosition + 1, cursorPosition - 1);

    // 현재 지운 문자가 @일때 그 앞부분을 다시 탐색하여 유효한 @를 찾지 못할 때 return 하도록 조건문 변경 필요
    if (isCurrentDeleteMentionSymbol(cursorPosition, mentionSymbolPosition)) {
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;

      return;
    }

    // 슬라이스 한 문자열 내부에 공백이 있나, 해당 조건을 위 조건문이 리팩터링 됐을 때 위 조건문 위로 옮길 필요가 있음
    if (isIncludeWhite(targetText)) {
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      return;
    }

    // mentionSymbolPosition 앞에 공백이 있나 && mentionSymbolPosition 위치가 첫번째이다.
    if (isNotMeetMentionSymbol(mentionSymbolPosition, innerText)) {
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      return;
    }

    lastMentionSymbolPositionRef.current = mentionSymbolPosition + 1;
  };

  const isFirstMentionTag = () =>
    commentInputRef.current!.childNodes.length === 3 &&
    commentInputRef.current!.childNodes[1].nodeName &&
    commentInputRef.current!.childNodes[0].textContent === '';

  const isNotIncludeMentionSymbol = (text: string) => !text.includes('@');

  const isCurrentDeleteMentionSymbol = (
    cursorPosition: number,
    mentionSymbolPosition: number,
  ) => cursorPosition - 1 === mentionSymbolPosition;

  const isIncludeWhite = (text: string) => text.includes(' ');

  const isNotMeetMentionSymbol = (mentionSymbolPosition: number, text: string) =>
    mentionSymbolPosition !== 0 && text[mentionSymbolPosition - 1] !== ' ';

  const detectLeftEscapingMentionArea = () => {
    const cursorPosition = getCursorPosition(commentInputRef.current!)!;
    const { innerText } = commentInputRef.current!;

    if (isEscapingLeftMentionArea(innerText, cursorPosition)) {
      initializeMention();
    }
  };

  const isEscapingLeftMentionArea = (text: string, cursorPosition: number) =>
    text[cursorPosition - 2] === ' ';

  const detectRightEscapingMentionArea = () => {
    const cursorPosition = getCursorPosition(commentInputRef.current!)!;
    const { innerText } = commentInputRef.current!;

    if (isEscapingRightMentionArea(innerText, cursorPosition)) {
      initializeMention();
    }
  };

  const isEscapingRightMentionArea = (text: string, cursorPosition: number) =>
    text[cursorPosition] === ' ';

  const initializeMention = () => {
    lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
    // FilterValue 초기값 상수화 필요
    setFilterValue('');
    isFilterValueInitiatedRef.current = true;
    handleClosePopover();
  };

  return {
    postMentionNotifications,
    isLoadingPostMentionNotifications,
    isPopoverOpen,
    handleClosePopover,
    membersData,
    hasNextMembersPage,
    fetchNextMembersPage,
    selectMember,
    handleKeydownCommentInput,
    handleInputCommentInput,
  };
};

export default useMention;
