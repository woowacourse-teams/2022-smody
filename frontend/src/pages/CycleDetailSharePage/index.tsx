import useCycleDetailSharePage from './useCycleDetailSharePage';
import { parseTime } from 'utils';

import { Button, FlexBox, Title } from 'components';

const CycleDetailSharePage = () => {
  const { canvasRef, cycleDetailData, handleSaveClick } = useCycleDetailSharePage();

  if (typeof cycleDetailData === 'undefined') {
    return null;
  }

  const { startTime } = cycleDetailData.data;

  const { year, month, date } = parseTime(startTime);

  return (
    <FlexBox flexDirection="column" alignItems="center">
      <Title text="인증 기록 공유하기" />
      <canvas ref={canvasRef} width="600" height="600" />
      <FlexBox>
        <Button size="medium" onClick={handleSaveClick}>
          저장하기
        </Button>
      </FlexBox>
    </FlexBox>
  );
};

export default CycleDetailSharePage;
