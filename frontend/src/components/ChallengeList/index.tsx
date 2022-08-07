import { FlexBox, ChallengeItem, LoadingSpinner } from 'components';
import { ChallengeListProps } from 'components/ChallengeList/type';

export const ChallengeList = ({
  targetRef,
  challengeListData,
  challengeListRefetch,
}: ChallengeListProps) => {
  if (challengeListData === null) {
    return <LoadingSpinner />;
  }

  return (
    <FlexBox as="ul" flexDirection="column" gap="27px">
      {challengeListData.map((challengeInfo, challengeIndex) => (
        <li
          key={challengeInfo.challengeId}
          ref={challengeIndex === challengeListData.length - 1 ? targetRef : undefined}
        >
          <ChallengeItem {...challengeInfo} challengeListRefetch={challengeListRefetch} />
        </li>
      ))}
    </FlexBox>
  );
};
