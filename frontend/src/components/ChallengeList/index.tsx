import { challengeData } from 'mock/data';
import styled, { css } from 'styled-components';

import { FlexBox, ChallengeItem } from 'components';
import { ChallengeItemProps } from 'components/ChallengeItem/type';

export const ChallengeList = () => {
  const data = challengeData;
  return (
    <Wrapper as="ul">
      {data.map(({ challengeId, challengeName, challengerCount }: ChallengeItemProps) => (
        <li key={challengeId}>
          <ChallengeItem
            challengeId={challengeId}
            challengeName={challengeName}
            challengerCount={challengerCount}
          />
          <Line />
        </li>
      ))}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
})``;

const Line = styled.hr`
  ${({ theme }) => css`
    color: ${theme.border};
    border-style: solid none none;
    margin: 0 1rem;
    padding: 0;
  `}
`;
