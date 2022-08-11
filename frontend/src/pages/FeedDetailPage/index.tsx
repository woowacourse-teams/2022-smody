import styled from 'styled-components';

import { FlexBox, Comment, CommentInput } from 'components';

const FeedDetailPage = () => {
  return (
    <Wrapper flexDirection="column" alignItems="center">
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
