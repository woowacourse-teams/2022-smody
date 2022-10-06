import { ChallengerListProps } from './type';
import { useChallengerList } from './useChallengerList';

import { FlexBox, Challenger, EmptyContent } from 'components';

export const ChallengerList = ({ challengeId }: ChallengerListProps) => {
  const challengersData = useChallengerList({ challengeId });

  if (typeof challengersData?.data === 'undefined') {
    return null;
  }

  if (challengersData?.data.length === 0) {
    return (
      <EmptyContent
        title="아직 이 챌린지의 도전자가 없습니다 :)"
        description="첫 도전자가 되어보아요!!"
      />
    );
  }

  return (
    <FlexBox as="ul" flexDirection="column" gap="27px">
      {challengersData?.data.map((challenger) => (
        <li key={challenger.memberId}>
          <Challenger {...challenger} />
        </li>
      ))}
      <div style={{ height: '130px' }}></div>
    </FlexBox>
  );
};
