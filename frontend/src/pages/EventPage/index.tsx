import { EventChallengeItem } from './components';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

import { EVENT_CHALLENGE_ID_LIST, EVENT_CHALLENGES } from 'constants/event';

const EventPage = () => {
  const themeContext = useThemeContext();

  return (
    <FlexBox flexDirection="column">
      <TitleText color={themeContext.onBackground} size={20} fontWeight="bold">
        ⭐ 우테코 팀프로젝트 이벤트 ⭐
      </TitleText>

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

const TitleText = styled(Text)`
  text-align: center;
  margin-bottom: 1.5rem;
`;
