import { UseCommentInputProps } from './type';
import { queryKeys } from 'apis/constants';
import { useGetMembers, usePatchComments, usePostComment } from 'apis/feedApi';
import { useState, useEffect, useRef } from 'react';
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

const useCommentInput = ({
  selectedCommentId,
  editMode,
  turnOffEditMode,
}: UseCommentInputProps) => {
  // ------- 멘션 알림 기능 ------------------------
  const flagCheck = useRef(false);
  const [filterValue, setFilterValue] = useState('');
  const lastMentionSymbolPositionRef = useRef(-1);

  useEffect(() => {
    if (!flagCheck.current) {
      flagCheck.current = true;
      return;
    }

    console.log('useEffect에서 filterValue', filterValue);
    refetch();
  }, [filterValue]);

  const inputChangeHandler: MutationCallback = (mutations) => {
    console.log(
      '필터밸류',
      filterValue,
      'lastSymbolPostion: ',
      lastMentionSymbolPositionRef.current,
    );
    const commentInputElement = commentInputRef.current!;
    resizeHeight(commentInputElement);
    const { innerText } = commentInputElement;

    // Symbol 위치 정보 저장 여부 판단.

    // symbol의 위치 정보 없어?
    if (lastMentionSymbolPositionRef.current === -1) {
      // symbol의 위치를 찾아서 알려만 줘
      detectMentionSymbol(innerText);
      console.log('왔니1');
    } else {
      // symbol 위치 정보 있으면, 이제 일을 하자.
      // 현재 입력 값이 공백이냐? 어 그럼 말이 달라지지.

      if (innerText[getCursorPosition() - 1] === ' ') {
        // symbol 위치 정보 제거 - 다시 symbol 위치 정보 없앨거야
        lastMentionSymbolPositionRef.current = -1;
        console.log('왔니2');
      } else {
        // 현재 입력 값이 뭐라도 있네. 그러면, 방금 @부터 지금 커서까지 filterValue를 만들어 줄게
        setFilterValue(
          innerText.slice(lastMentionSymbolPositionRef.current, getCursorPosition()),
        );
        console.log('왔니3');
      }
    }

    setContent(innerText.slice(0, MAX_TEXTAREA_LENGTH));
  };
  const commentInputRef = useMutationObserver<HTMLDivElement>(inputChangeHandler);

  const {
    isFetching,
    data: membersData,
    hasNextPage,
    fetchNextPage,
    refetch,
    isError,
  } = useGetMembers(
    { filterValue },
    {
      onSuccess: (data) => {
        console.log('@@멤버 조회 데이터: ', data);
      },
      enabled: false,
    },
  );

  const detectMentionSymbol = (text: string) => {
    const cursorPosition = getCursorPosition();
    const currentCharacter = text[cursorPosition - 1];

    // 1. 현재 cursor 포지션의 바로 앞이 @인 경우에만 @ 이벤트를 호출한다.
    if (currentCharacter !== '@') {
      return;
    }

    // 2. 그런데 호출하려는 @의 앞이 공백이 아니면 호출하지 않는다.
    if (cursorPosition !== 1 && text.length >= 2 && text[cursorPosition - 2] !== ' ') {
      // symbol 위치 정보 제거
      return;
    }

    // 멤버 조회 API 요청
    // console.log(text, cursorPosition);
    // setLastMentionSymbolPosition(cursorPosition);

    // flagFoundMentionSymbol.current = true;
    // setFilterValue();
    // setLastMentionSymbolPosition(1000000);
    lastMentionSymbolPositionRef.current = cursorPosition;
    console.log('setLastMentionSymbolPosition', lastMentionSymbolPositionRef.current);
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
  };
};

export default useCommentInput;
