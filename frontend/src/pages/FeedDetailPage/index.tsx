import useFeedDetailPage from './useFeedDetailPage';
import styled from 'styled-components';

import { FlexBox, Comment, CommentInput, FeedItem } from 'components';

const FeedDetailPage = () => {
  const { feedDetailData } = useFeedDetailPage();

  if (typeof feedDetailData === 'undefined') {
    return null;
  }

  return (
    <Wrapper flexDirection="column" alignItems="center">
      <FeedItem isClickable={false} {...feedDetailData.data} />
      <FlexBox as="ul" flexDirection="column" gap="1.563rem">
        <Comment />
        <Comment />
      </FlexBox>
      <CommentInput />
    </Wrapper>
  );
};

export default FeedDetailPage;

const Wrapper = styled(FlexBox)`
  padding-bottom: 6.625rem;
`;
