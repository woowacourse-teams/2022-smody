import { EmptyContent } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const NotFoundPage = () => {
  return (
    <div>
      <EmptyContent
        title="404 Error❗️"
        description="요청하신 페이지를 찾을 수 없습니다 :)"
        linkText="메인 페이지로 이동하기"
        linkTo={CLIENT_PATH.CERT}
      />
    </div>
  );
};
