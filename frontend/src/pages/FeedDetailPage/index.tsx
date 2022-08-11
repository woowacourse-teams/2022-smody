import useFeedDetailPage from './useFeedDetailPage';
import styled from 'styled-components';

import { FlexBox, CommentItem, CommentInput, FeedItem } from 'components';

const FeedDetailPage = () => {
  const { feedData, commentsData } = useFeedDetailPage();

  if (typeof feedData === 'undefined' || typeof commentsData === 'undefined') {
    return null;
  }

  return (
    <Wrapper flexDirection="column" alignItems="center">
      <FeedItem isClickable={false} {...feedData.data} />
      <FlexBox as="ul" flexDirection="column" gap="1.563rem">
        {commentsData?.data.map((comment) => (
          <li key={comment.commentId}>
            <CommentItem
              isWriter={feedData.data.memberId === comment.memberId}
              {...comment}
            />
          </li>
        ))}
      </FlexBox>
      <CommentInput />
    </Wrapper>
  );
};

export default FeedDetailPage;

const Wrapper = styled(FlexBox)`
  padding-bottom: 6.625rem;
`;
