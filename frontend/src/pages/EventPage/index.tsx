import { EventChallengeItem } from './components';

import { FlexBox, Title } from 'components';

import { EVENT_CHALLENGE_ID_LIST, EVENT_CHALLENGES } from 'constants/event';
import { CLIENT_PATH } from 'constants/path';

const EventPage = () => {
  return (
    <FlexBox flexDirection="column">
      <Title text="⭐ 우테코 팀프로젝트 이벤트 ⭐" linkTo={CLIENT_PATH.SEARCH} />
      <FlexBox as="ul" flexDirection="column" gap="27px">
        {EVENT_CHALLENGE_ID_LIST.map((challengeId: number) => (
          <li key={challengeId}>
            <EventChallengeItem
              challengeId={challengeId}
              challengeName={EVENT_CHALLENGES[challengeId].challengeName}
              link={EVENT_CHALLENGES[challengeId].link}
            />
          </li>
        ))}
      </FlexBox>
    </FlexBox>
  );
};

export default EventPage;
