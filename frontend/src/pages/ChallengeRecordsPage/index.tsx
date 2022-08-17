import { ChallengeInfoWithUser, FlexBox } from 'components';
import { CycleRecords } from 'components/CycleRecords';

const ChallengeRecordsPage = () => {
  return (
    <FlexBox flexDirection="column" gap="1rem">
      <ChallengeInfoWithUser />
      <CycleRecords />
    </FlexBox>
  );
};

export default ChallengeRecordsPage;
