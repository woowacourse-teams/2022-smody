import { queryKeys } from 'apis/constants';
import { usePostComment } from 'apis/feedApi';
import { useState, ChangeEvent } from 'react';
import { useQueryClient } from 'react-query';
import { useParams } from 'react-router-dom';

const DEFAULT_INPUT_HEIGHT = '20px';
const INITIAL_CONTENT = '';

const useCommentInput = () => {
  const [content, setContent] = useState(INITIAL_CONTENT);
  const { cycleDetailId } = useParams();
  const queryClient = useQueryClient();

  const { mutate: postComment } = usePostComment(
    { cycleDetailId: Number(cycleDetailId) },
    {
      onSuccess: () => {
        queryClient.invalidateQueries(queryKeys.getFeedById);
        queryClient.invalidateQueries(queryKeys.getCommentsById);
      },
    },
  );

  const handleChangeInput = (event: ChangeEvent<HTMLTextAreaElement>) => {
    // 입력 값을 지웠을 때, 댓글 입력창의 높이를 줄이기 위한 코드
    event.target.style.height = DEFAULT_INPUT_HEIGHT;
    event.target.style.height = event.target.scrollHeight + 'px';

    setContent(event.target.value);
  };

  const handleClickWrite = () => {
    // TODO: 비로그인 시 postComment를 실행하지 않도록 구현
    // TODO: content의 length가 0일 때 postComment를 실행하지 않도록 구현
    postComment({ content });
    setContent(INITIAL_CONTENT);
  };

  return {
    content,
    handleChangeInput,
    handleClickWrite,
  };
};

export default useCommentInput;
