import { useContext } from 'react';
import { AiFillFlag } from 'react-icons/ai';
import { GrNext } from 'react-icons/gr';
import { Link } from 'react-router-dom';
import styled, { ThemeContext } from 'styled-components';

import { Text, FlexBox } from 'components';
import { ChallengeItemProps } from 'components/ChallengeItem/type';

export const ChallengeItem = ({
  challengeId,
  challengeName,
  challengerCount,
}: ChallengeItemProps) => {
  const themeContext = useContext(ThemeContext);

  return (
    <Link to={`challenge/${challengeId}`}>
      <Wrapper>
        <ChallengeNameWrapper>
          <AiFillFlag />
          <Text color={themeContext.onSurface} size={16} fontWeight="bold">
            {challengeName}
          </Text>
        </ChallengeNameWrapper>
        <Text color={themeContext.onSurface} size={16}>
          {challengerCount}명 도전중 🔥
        </Text>
        <GrNext size="1rem" color={themeContext.onSurface} />
      </Wrapper>
    </Link>
  );
};

const Wrapper = styled.div`
  display: grid;
  grid-template-columns: minmax(9.4rem, 2rem) minmax(9.4rem, auto) 1.5rem;
  padding: 1.25rem 0;
  &:active {
    background-color: 'gray';
  }
`;

const ChallengeNameWrapper = styled(FlexBox).attrs({
  gap: '10px',
  alignItems: 'center',
})``;
