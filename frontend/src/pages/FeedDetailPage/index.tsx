import { MenuBottomSheetProps } from './type';
import useFeedDetailPage from './useFeedDetailPage';
import { useEffect } from 'react';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import {
  Text,
  FlexBox,
  CommentItem,
  CommentInput,
  FeedItem,
  BottomSheet,
} from 'components';

const FeedDetailPage = () => {
  const {
    feedData,
    commentsData,
    isMenuBottomSheetOpen,
    selectedCommentId,
    editMode,
    isLoadingDeleteComment,
    handleClickMenuButton,
    handleCloseBottomSheet,
    handleClickCommentEdit,
    handleClickCommentDelete,
    turnOffEditMode,
  } = useFeedDetailPage();

  useEffect(() => {
    scrollTo(0, 0);
  }, []);

  if (typeof feedData === 'undefined' || typeof commentsData === 'undefined') {
    return null;
  }

  return (
    <Wrapper flexDirection="column" alignItems="center">
      <FeedItem isDetailPage={true} isShowBriefChallengeName={false} {...feedData.data} />
      <CommentList as="ul" flexDirection="column" gap="1.563rem">
        {commentsData?.data.map((comment) => (
          <li key={comment.commentId}>
            <CommentItem
              isWriter={feedData.data.memberId === comment.memberId}
              isSelectedComment={comment.commentId === selectedCommentId}
              handleClickMenuButton={handleClickMenuButton}
              {...comment}
            />
          </li>
        ))}
      </CommentList>
      <CommentInput
        selectedCommentId={selectedCommentId}
        editMode={editMode}
        turnOffEditMode={turnOffEditMode}
      />
      {isMenuBottomSheetOpen && (
        <MenuBottomSheet
          isLoadingDeleteComment={isLoadingDeleteComment}
          handleCloseBottomSheet={handleCloseBottomSheet}
          handleClickCommentEdit={handleClickCommentEdit}
          handleClickCommentDelete={handleClickCommentDelete}
        />
      )}
    </Wrapper>
  );
};

export default FeedDetailPage;

const MenuBottomSheet = ({
  isLoadingDeleteComment,
  handleCloseBottomSheet,
  handleClickCommentEdit,
  handleClickCommentDelete,
}: MenuBottomSheetProps) => {
  const themeContext = useThemeContext();

  return (
    <BottomSheet height="200px" handleCloseBottomSheet={handleCloseBottomSheet}>
      <MenuBottomSheetWrapper flexDirection="column" alignItems="flex-start">
        <MenuBottomSheetButton
          as="button"
          size={16}
          color={themeContext.onBackground}
          disabled={isLoadingDeleteComment}
          onClick={handleClickCommentEdit}
        >
          댓글 수정하기
        </MenuBottomSheetButton>
        <MenuBottomSheetButton
          as="button"
          size={16}
          color={themeContext.error}
          disabled={isLoadingDeleteComment}
          onClick={handleClickCommentDelete}
        >
          댓글 삭제하기
        </MenuBottomSheetButton>
      </MenuBottomSheetWrapper>
    </BottomSheet>
  );
};

const Wrapper = styled(FlexBox)`
  padding-bottom: 8.625rem;
`;

const CommentList = styled(FlexBox)`
  width: 100%;
  max-width: 430px;
`;

const MenuBottomSheetWrapper = styled(FlexBox)`
  width: 100%;
`;

const MenuBottomSheetButton = styled(Text)`
  ${({ theme }) => css`
    width: 100%;
    padding: 1rem;
    text-align: left;

    &:active {
      background-color: ${theme.disabled};
    }

    &:disabled {
      color: ${theme.disabled};
    }
  `}
`;
