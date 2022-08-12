import useFeedDetailPage from './useFeedDetailPage';
import { useEffect } from 'react';
import styled from 'styled-components';

import { FlexBox, CommentItem, CommentInput, FeedItem } from 'components';

const FeedDetailPage = () => {
  const { feedData, commentsData } = useFeedDetailPage();

  useEffect(() => {
    scrollTo(0, 0);
  }, []);

  if (typeof feedData === 'undefined' || typeof commentsData === 'undefined') {
    return null;
  }

  return (
    <Wrapper flexDirection="column" alignItems="center">
      <FeedItem isClickable={false} {...feedData.data} />
      <CommentList as="ul" flexDirection="column" gap="1.563rem">
        {commentsData?.data.map((comment) => (
          <li key={comment.commentId}>
            <CommentItem
              isWriter={feedData.data.memberId === comment.memberId}
              {...comment}
            />
          </li>
        ))}
      </CommentList>
      <CommentInput />
    </Wrapper>
  );
};

export default FeedDetailPage;

const Wrapper = styled(FlexBox)`
  padding-bottom: 8.625rem;
`;

const CommentList = styled(FlexBox)`
  max-width: 440px;
`;
