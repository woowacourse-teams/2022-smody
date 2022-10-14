import { EventChallengeItemProps } from './type';
import { Link } from 'react-router-dom';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { Text, FlexBox, Button, ChallengeIcon } from 'components';

import { EVENT_CHALLENGES } from 'constants/event';
import { CLIENT_PATH } from 'constants/path';

import COLOR from 'styles/color';

export const EventChallengeItem = ({
  challengeId,
  challengeName,
  link,
}: EventChallengeItemProps) => {
  const themeContext = useThemeContext();

  return (
    <Wrapper justifyContent="space-between" alignItems="center">
      <Link to={`${CLIENT_PATH.CHALLENGE_DETAIL}/${challengeId}`}>
        <FlexBox gap="17px" alignItems="center">
          <ChallengeIcon challengeId={challengeId} size="small" />
          <FlexBox flexDirection="column">
            <Text aria-label="challenge-name" color={themeContext.onSurface} size={16}>
              {challengeName}
            </Text>
            <Text color={themeContext.mainText} size={12}>
              {EVENT_CHALLENGES[challengeId].link}
            </Text>
          </FlexBox>
        </FlexBox>
      </Link>
      <FlexBox justifyContent="flex-end" gap="5px">
        <a href={link} target="_blank" rel="noreferrer">
          <LinkButton size="small">링크</LinkButton>
        </a>
        <Link to={`${CLIENT_PATH.CHALLENGE_DETAIL}/${challengeId}`}>
          <DetailButton size="small">보기</DetailButton>
        </Link>
      </FlexBox>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  a {
    flex-grow: 1;
  }
  div {
    flex-grow: 0;
  }
`;

const LinkButton = styled(Button)`
  background-color: #e7e1ff;
  color: ${COLOR.DARK_PURPLE};
  width: 35px;
  padding: 0;
`;

const DetailButton = styled(Button)`
  width: 35px;
  padding: 0;
`;
