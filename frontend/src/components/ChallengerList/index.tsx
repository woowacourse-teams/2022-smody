import { ChallengerListProps } from './type';
import { useChallengerList } from './useChallengerList';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, Challenger } from 'components';

export const ChallengerList = ({ challengeId }: ChallengerListProps) => {
  const themeContext = useThemeContext();
  const challengersData = useChallengerList({ challengeId });

  return (
    <FlexBox flexDirection="column" gap="30px">
      <Text size={24} color={themeContext.onBackground} fontWeight="bold">
        챌린지 도전자
      </Text>
      <FlexBox as="ul" flexDirection="column" gap="27px">
        {challengersData?.data.map((challenger) => (
          <li key={challenger.memberId}>
            <Challenger {...challenger} />
          </li>
        ))}
      </FlexBox>
    </FlexBox>
  );
};
