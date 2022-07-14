import { useContext } from 'react';
import { Link } from 'react-router-dom';
import styled, { ThemeContext } from 'styled-components';

import { Text, FlexBox, Button, ThumbnailWrapper } from 'components';
import { ChallengeItemProps } from 'components/ChallengeItem/type';

export const ChallengeItem = ({
  challengeId,
  challengeName,
  challengerCount,
}: ChallengeItemProps) => {
  const themeContext = useContext(ThemeContext);

  return (
    <Link to={`/challenge/detail/${challengeId}`} state={{ challengeName }}>
      <Wrapper>
        <ChallengeInfoWrapper>
          <ThumbnailWrapper size="small" bgColor="#FED6D6">
            🌞
          </ThumbnailWrapper>
          <ChallengeNameWrapper>
            <Text color={themeContext.onSurface} size={16}>
              {challengeName}
            </Text>
            <Text color={themeContext.mainText} size={12}>
              {challengerCount}명 도전중
            </Text>
          </ChallengeNameWrapper>
        </ChallengeInfoWrapper>
        <Button size="small" isActive={false}>
          도전
        </Button>
      </Wrapper>
    </Link>
  );
};

const Wrapper = styled(FlexBox).attrs({
  justifyContent: 'space-between',
  alignItems: 'center',
})``;

const ChallengeInfoWrapper = styled(FlexBox).attrs({
  gap: '17px',
  alignItems: 'center',
})``;

const ChallengeNameWrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '3px',
})``;
