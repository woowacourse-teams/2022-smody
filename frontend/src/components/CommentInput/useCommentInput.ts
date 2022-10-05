import { UseCommentInputProps } from './type';
import { queryKeys } from 'apis/constants';
import {
  useGetMembers,
  usePatchComments,
  usePostComment,
  usePostMentionNotifications,
} from 'apis/feedApi';
import { useState, useEffect, useRef, KeyboardEventHandler } from 'react';
import { useQueryClient } from 'react-query';
import { useParams } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { getCursorPosition, insertAfter } from 'utils';

import useMutationObserver from 'hooks/useMutationObserver';
import useSnackBar from 'hooks/useSnackBar';

import { MAX_TEXTAREA_LENGTH } from 'constants/domain';
import { CLIENT_PATH } from 'constants/path';

const DEFAULT_INPUT_HEIGHT = '1.5rem';
const INITIAL_CONTENT = '';
const ABSENCE_SYMBOL_POSITION = -1;

const useCommentInput = ({
  selectedCommentId,
  editMode,
  turnOffEditMode,
}: UseCommentInputProps) => {
  // ------- 멘션 알림 기능 ------------------------
  const isFirstRendered = useRef(true);
  const [filterValue, setFilterValue] = useState('');
  const [mentionedMemberIds, setMentionedMemberIds] = useState<Array<number>>([]);
  const lastMentionSymbolPositionRef = useRef(ABSENCE_SYMBOL_POSITION);
  const isFilterValueInitiated = useRef(false);
  const isPrevPressBackspace = useRef(false);
  const prevCursorPosition = useRef(0);

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
          console.log('6');
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
    if (isFilterValueInitiated.current === true) {
      return;
    }
    console.log('#####filterValue', filterValue);
    refetchMembers();
  }, [filterValue]);

  useEffect(() => {
    const handleKeydown: KeyboardEventHandler<HTMLDivElement> = (event) => {
      if (event.key === 'Backspace' || event.key === 'Delete') {
        // refetch
        detectMentionSymbolWhenTextDeleted();
        return;
      }

      isPrevPressBackspace.current = false;
      prevCursorPosition.current = 0;

      if (event.key === 'ArrowLeft') {
        // closePopover
        detectLeftEscapingMentionArea();
        return;
      }

      if (event.key === 'ArrowRight') {
        detectRightEscapingMentionArea();
        return;
      }
    };
    commentInputRef.current?.addEventListener('keydown', handleKeydown);

    return () => {
      commentInputRef.current?.removeEventListener('keydown', handleKeydown);
    };
  }, []);

  const inputChangeHandler: MutationCallback = (mutations) => {
    mutations.forEach((mutation) => {
      if (mutation.type === 'childList') {
        if (
          mutation.removedNodes.length > 0 &&
          mutation.removedNodes[0].nodeName === 'SPAN'
        ) {
          console.log('@!@!@!@!@@', mutation.removedNodes[0]);
          const targetNode = mutation.removedNodes[0] as HTMLElement;
          const deletedMemberId = Number(targetNode.getAttribute('data-member-id'));
          const deletedIndex = mentionedMemberIds.indexOf(deletedMemberId);
          if (deletedIndex > -1) {
            console.log('삭제');
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
          console.log('mentionedMemberIds', mentionedMemberIds);
          console.log('deletedIndex', deletedIndex);
          console.log('배고파', targetNode.getAttribute('data-member-id'));
          console.log('속성 node: .', targetNode.ATTRIBUTE_NODE);
        }
      }
    });
    const hasSymbolPosition =
      lastMentionSymbolPositionRef.current !== ABSENCE_SYMBOL_POSITION;

    const isCurrentCharacterWhiteSpace = (text: string) =>
      text[getCursorPosition(commentInputRef.current)! - 1] === ' ';

    if (isFilterValueInitiated.current === true) {
      isFilterValueInitiated.current = false;
    }

    const setNicknameAfterMentionSymbol = (text: string) => {
      if (isCurrentCharacterWhiteSpace(text)) {
        lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
        setFilterValue(''); // 초기화
        isFilterValueInitiated.current = true;
        console.log('2');
        handleClosePopover();
      } else {
        // 건들지 마시오
        setFilterValue(
          text.slice(
            lastMentionSymbolPositionRef.current,
            getCursorPosition(commentInputRef.current)!,
          ),
        );
        console.log(
          'inputChangeHandler에서 @가 앞에 있을 때 setNicknameAfterMentionSymbol에서 filterValue 넣어주는 값',
          text.slice(
            lastMentionSymbolPositionRef.current,
            getCursorPosition(commentInputRef.current)!,
          ),
        );
      }
    };

    const commentInputElement = commentInputRef.current!;
    resizeHeight(commentInputElement);
    const { innerText } = commentInputElement;

    if (hasSymbolPosition) {
      setNicknameAfterMentionSymbol(innerText);

      if (getCursorPosition(commentInputRef.current)! === 0) {
        console.log('1');
        handleClosePopover();
      }
    } else {
      detectMentionSymbolWhenTextAdded(innerText);
    }

    setContent(innerText.slice(0, MAX_TEXTAREA_LENGTH));
    console.log('***********');
  };

  const commentInputRef = useMutationObserver<HTMLDivElement>(inputChangeHandler);

  // 문자열 새로 입력됐을 때
  const detectMentionSymbolWhenTextAdded = (text: string) => {
    const cursorPosition = getCursorPosition(commentInputRef.current)!;
    const currentCharacter = text[cursorPosition - 1];

    // 1. 현재 cursor 포지션의 바로 앞이 @인 경우에만 @ 이벤트를 호출한다.
    if (currentCharacter !== '@') {
      console.log('3');
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
    console.log('들어왔니?', text);
  };

  const detectMentionSymbolWhenTextDeleted = () => {
    const cursorPosition = getCursorPosition(commentInputRef.current)!;

    const { innerText } = commentInputRef.current!;

    // 지우려는 element 첫 번째 자식인 경우 contentEditable 부모에서 해당 element를 삭제할 수 없는 이슈 때문에
    // 다음과 같은 분기문을 통해 첫 번째 element도 삭제 가능하도록 이슈 해결함
    if (
      commentInputRef.current?.childNodes.length === 3 &&
      commentInputRef.current?.childNodes[1].nodeName &&
      commentInputRef.current?.childNodes[0].textContent === ''
    ) {
      // init 3종 세트
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      isFilterValueInitiated.current = true;
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
    console.log('@cursorPosition', cursorPosition);
    console.log('mentionSymbolPosition', mentionSymbolPosition);
    console.log('@targetText', targetText);
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

    console.log('삭제할 때 filterValue 넣어주는 값', targetText);
    // setFilterValue(targetText);
  };

  const detectLeftEscapingMentionArea = () => {
    const cursorPosition = getCursorPosition(commentInputRef.current)!;
    const { innerText } = commentInputRef.current!;

    if (innerText[cursorPosition - 2] === ' ') {
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      setFilterValue('');
      isFilterValueInitiated.current = true;
      console.log('4');
      handleClosePopover();
    }
  };

  const detectRightEscapingMentionArea = () => {
    const cursorPosition = getCursorPosition(commentInputRef.current)!;
    const { innerText } = commentInputRef.current!;

    if (innerText[cursorPosition] === ' ') {
      // init 3종 세트
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      setFilterValue('');
      isFilterValueInitiated.current = true;
      console.log('5');
      handleClosePopover();
    }
  };

  const [isPopoverOpen, setIsPopoverOpen] = useState(false);
  const handleClosePopover = () => {
    setIsPopoverOpen(false);
  };
  const handleOpenPopover = () => {
    setIsPopoverOpen(true);
  };

  const selectMember = (memberId: number, nickname: string) => {
    if (commentInputRef.current === null) {
      return;
    }

    mentionedMemberIds.push(memberId);
    setMentionedMemberIds(mentionedMemberIds);

    const cursorPosition = getCursorPosition(commentInputRef.current)!;
    const childNodes = commentInputRef.current.childNodes;

    console.log(childNodes);
    let accLength = 0;

    console.log(
      '현재 lastMentionSymbolPositionRef.current: ',
      lastMentionSymbolPositionRef.current,
    );

    const currentNodeIndex = Array.from(childNodes).findIndex((childNode) => {
      const text = childNode.textContent!;
      if (lastMentionSymbolPositionRef.current <= accLength + text.length) {
        return true;
      }

      accLength += text.length;
      return false;
    });

    console.log('현재 currentNodeIndex: ', currentNodeIndex);

    const currentNode = childNodes[currentNodeIndex];
    const currentText = currentNode.textContent;

    const targetStart = lastMentionSymbolPositionRef.current - accLength - 1;
    const targetEnd =
      lastMentionSymbolPositionRef.current - accLength + filterValue.length;

    const frontTextNode = document.createTextNode(
      currentText!.substring(0, targetStart)!,
    );

    const nicknameSpan = document.createElement('span');
    nicknameSpan.innerText = `@${nickname}`;
    nicknameSpan.setAttribute('contenteditable', 'false');
    nicknameSpan.style.backgroundColor = '#7b61fe';
    nicknameSpan.style.color = '#fff';
    nicknameSpan.style.borderRadius = '5px';
    nicknameSpan.style.lineHeight = '2';
    nicknameSpan.style.padding = '2px 4px';
    nicknameSpan.setAttribute('data-member-id', String(memberId));

    const backTextNode = document.createTextNode(currentText!.substring(targetEnd)!);

    if (currentNodeIndex === 0) {
      commentInputRef.current.appendChild(frontTextNode);
      commentInputRef.current.appendChild(nicknameSpan);
      commentInputRef.current.appendChild(backTextNode);
    } else {
      const prevNode = childNodes[currentNodeIndex - 1];

      insertAfter(prevNode, frontTextNode);
      insertAfter(frontTextNode, nicknameSpan);
      insertAfter(nicknameSpan, backTextNode);
    }

    commentInputRef.current.removeChild(currentNode);

    // init 삼형제
    lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
    setFilterValue('');
    isFilterValueInitiated.current = true;

    // contenteditable div에서 cursor position에 focus 하기
    const element = commentInputRef.current;
    const selection = window.getSelection();
    const range = document.createRange();
    selection!.removeAllRanges();
    range.selectNodeContents(element);
    range.collapse(false);
    selection!.addRange(range);
    element.focus();
  };

  // ---------- 댓글 작성 및 수정하여 db에 보내는 관련 로직 ----------------

  const [content, setContent] = useState(INITIAL_CONTENT);
  const queryClient = useQueryClient();
  const isLogin = useRecoilValue(isLoginState);
  const { cycleDetailId } = useParams();
  const renderSnackBar = useSnackBar();

  const { mutate: postComment, isLoading: isLoadingPostComment } = usePostComment(
    { cycleDetailId: Number(cycleDetailId) },
    {
      onSuccess: () => {
        invalidateQueries();

        setContent(INITIAL_CONTENT);

        commentInputRef.current!.textContent = '';

        setMentionedMemberIds([]);
        console.log('@@@@usePostComment@@@초기화');

        resizeToInitialHeight();

        renderSnackBar({
          status: 'SUCCESS',
          message: '댓글이 작성됐습니다.',
        });
      },
    },
  );

  const { mutate: patchComment, isLoading: isLoadingPatchComment } = usePatchComments({
    onSuccess: () => {
      invalidateQueries();

      setContent(INITIAL_CONTENT);

      commentInputRef.current!.textContent = '';

      setMentionedMemberIds([]);
      console.log('@@@@usePostComment@@@초기화');

      resizeToInitialHeight();

      renderSnackBar({
        status: 'SUCCESS',
        message: '댓글이 수정됐습니다.',
      });

      turnOffEditMode();
    },
  });

  useEffect(() => {
    setContent(editMode.editContent);
  }, [editMode]);

  const isVisibleWriteButton = content.length !== 0;
  const isMaxLengthOver = content.length >= MAX_TEXTAREA_LENGTH - 1;

  const handleClickWrite = () => {
    if (!isLogin) {
      renderSnackBar({
        message: '로그인한 사용자만 댓글을 작성할 수 있습니다. 로그인을 해주세요:)',
        status: 'ERROR',
        linkText: '로그인하러 가기',
        linkTo: CLIENT_PATH.HOME,
      });

      return;
    }

    if (editMode.isEditMode && typeof selectedCommentId === 'number') {
      patchComment({ commentId: selectedCommentId, content });
      return;
    }
    postComment({ content });

    console.log('현재 mentionedMemberIds: ', Array.from(new Set(mentionedMemberIds)));
    postMentionNotifications({
      memberIds: Array.from(new Set(mentionedMemberIds)),
      pathId: Number(cycleDetailId),
    });
  };

  const invalidateQueries = () => {
    queryClient.invalidateQueries(queryKeys.getFeedById);
    queryClient.invalidateQueries(queryKeys.getCommentsById);
  };

  const resizeHeight = (element: HTMLDivElement) => {
    // 입력 값을 지웠을 때, 댓글 입력창의 높이를 줄이기 위한 코드
    element.style.height = DEFAULT_INPUT_HEIGHT;
    element.style.height = element.scrollHeight + 'px';
  };

  const resizeToInitialHeight = () => {
    if (!commentInputRef.current) {
      return;
    }

    commentInputRef.current.style.height = DEFAULT_INPUT_HEIGHT;
  };

  return {
    commentInputRef,
    content,
    isVisibleWriteButton,
    isMaxLengthOver,
    isLoadingPostComment,
    isLoadingPatchComment,
    handleClickWrite,
    isFetchingMembers,
    membersData,
    hasNextMembersPage,
    fetchNextMembersPage,
    isPopoverOpen,
    handleClosePopover,
    selectMember,
    isLoadingPostMentionNotifications,
  };
};

export default useCommentInput;
