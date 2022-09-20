import useCycleDetailSharePage from './useCycleDetailSharePage';
import styled from 'styled-components';
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
      <ButtonWrapper gap="1rem">
        <Button size="medium" onClick={handleSaveClick}>
          저장하기
        </Button>
        <Button size="medium" onClick={handleSaveClick}>
          공유하기
        </Button>
      </ButtonWrapper>
    </FlexBox>
  );
};

export default CycleDetailSharePage;

const ButtonWrapper = styled(FlexBox)`
  margin: 2rem 0;
`;
