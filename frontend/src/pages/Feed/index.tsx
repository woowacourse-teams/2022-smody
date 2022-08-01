import { EmptyContent } from 'components';

import { CLIENT_PATH } from 'constants/path';

const Feed = () => {
  return (
    <>
      <EmptyContent
        title="아직 올라온 피드가 없습니다 :)"
        description="피드를 올려주세요!!"
        linkText="인증 페이지로 이동하기"
        linkTo={CLIENT_PATH.CERT}
      />
    </>
  );
};

export default Feed;
