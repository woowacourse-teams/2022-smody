import { UseCommentInputProps } from './type';
import { queryKeys } from 'apis/constants';
import { useGetMembers, usePatchComments, usePostComment } from 'apis/feedApi';
import { useState, useEffect, useRef, KeyboardEventHandler } from 'react';
import { useQueryClient } from 'react-query';
import { useParams } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';
import { getCursorPosition } from 'utils';

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
  const flagCheck = useRef(false);
  const [filterValue, setFilterValue] = useState('');
  const lastMentionSymbolPositionRef = useRef(ABSENCE_SYMBOL_POSITION);
  const flagInitFilterValue = useRef(false);
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

  useEffect(() => {
    if (!flagCheck.current) {
      flagCheck.current = true;
      return;
    }
    if (flagInitFilterValue.current === true) {
      return;
    }
    refetchMembers();
  }, [filterValue]);

  useEffect(() => {
    const handleKeydown: KeyboardEventHandler<HTMLDivElement> = (event) => {
      if (event.key === 'Backspace' || event.key === 'Delete') {
        // refetch
        detectMentionSymbolWhenTextDeleted();
        return;
      }

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
    const hasSymbolPosition =
      lastMentionSymbolPositionRef.current !== ABSENCE_SYMBOL_POSITION;

    const isCurrentCharacterWhiteSpace = (text: string) =>
      text[getCursorPosition() - 1] === ' ';

    if (flagInitFilterValue.current === true) {
      flagInitFilterValue.current = false;
    }

    const setNicknameAfterMentionSymbol = (text: string) => {
      if (isCurrentCharacterWhiteSpace(text)) {
        lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
        setFilterValue(''); // 초기화
        flagInitFilterValue.current = true;

        handleClosePopover();
      } else {
        setFilterValue(
          text.slice(lastMentionSymbolPositionRef.current, getCursorPosition()),
        );
      }
    };

    const commentInputElement = commentInputRef.current!;
    resizeHeight(commentInputElement);
    const { innerText } = commentInputElement;

    if (hasSymbolPosition) {
      setNicknameAfterMentionSymbol(innerText);

      if (getCursorPosition() === 0) {
        handleClosePopover();
      }
    } else {
      detectMentionSymbolWhenTextAdded(innerText);
    }

    setContent(innerText.slice(0, MAX_TEXTAREA_LENGTH));
  };

  const commentInputRef = useMutationObserver<HTMLDivElement>(inputChangeHandler);

  // 문자열 새로 입력됐을 때
  const detectMentionSymbolWhenTextAdded = (text: string) => {
    const cursorPosition = getCursorPosition();
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
    const cursorPosition = getCursorPosition();

    const { innerText } = commentInputRef.current!;

    if (!innerText.includes('@')) {
      return;
    }
    const mentionSymbolPosition = innerText.lastIndexOf('@', cursorPosition);
    const targetText = innerText.slice(mentionSymbolPosition + 1, cursorPosition - 1);

    // 슬라이스 한 문자열 내부에 공백이 있나
    if (targetText.includes(' ')) {
      return;
    }

    // mentionSymbolPosition 앞에 공백이 있나 && mentionSymbolPosition 위치가 첫번째이다.
    if (mentionSymbolPosition !== 0 && innerText[mentionSymbolPosition - 1] !== ' ') {
      return;
    }

    setFilterValue(targetText);
  };

  const detectLeftEscapingMentionArea = () => {
    const cursorPosition = getCursorPosition();
    const { innerText } = commentInputRef.current!;

    if (innerText[cursorPosition - 2] === ' ') {
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      setFilterValue('');
      flagInitFilterValue.current = true;

      handleClosePopover();
    }
  };

  const detectRightEscapingMentionArea = () => {
    const cursorPosition = getCursorPosition();
    const { innerText } = commentInputRef.current!;

    if (innerText[cursorPosition] === ' ') {
      // init 3종 세트
      lastMentionSymbolPositionRef.current = ABSENCE_SYMBOL_POSITION;
      setFilterValue('');
      flagInitFilterValue.current = true;

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
  };
};

export default useCommentInput;
