import { EventChallengeItem } from './components/EventChallengeItem';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

import { EVENT_CHALLENGE_ID_LIST, EVENT_CHALLENGES } from 'constants/event';

const EventPage = () => {
  const themeContext = useThemeContext();

  return (
    <FlexBox
      flexDirection="column"
      justifyContent="center"
      aria-label="이벤트 챌린지 페이지"
      as="section"
    >
      <TitleText color={themeContext.onBackground} size={20} fontWeight="bold" as="h1">
        ⭐ 우테코 팀프로젝트 사용 챌린지 이벤트 ⭐
      </TitleText>
      <DescriptionText color={themeContext.onBackground} size={16}>
        챌린지를 1회라도 인증한다면 추첨을 통해 스모디 컵을 드립니다!
      </DescriptionText>
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
  margin-bottom: 0.3rem;
`;

const DescriptionText = styled(Text)`
  text-align: center;
  margin-bottom: 1.5rem;
`;
