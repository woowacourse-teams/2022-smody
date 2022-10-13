import { FlexBox, Title } from 'components';

import { CLIENT_PATH } from 'constants/path';

const EventPage = () => {
  return (
    <FlexBox flexDirection="column">
      <Title text="이벤트 페이지" linkTo={CLIENT_PATH.SEARCH} />
      우테코 팀프로젝트들을 위한 이벤트 페이지
    </FlexBox>
  );
};

export default EventPage;
