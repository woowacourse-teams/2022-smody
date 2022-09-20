import useCycleDetailSharePage from './useCycleDetailSharePage';
import styled from 'styled-components';

import { Button, FlexBox, Title } from 'components';

const CycleDetailSharePage = () => {
  const { canvasRef, handleSaveClick, handleShareClick } = useCycleDetailSharePage();

  return (
    <FlexBox flexDirection="column" alignItems="center">
      <Title text="인증 기록 공유하기" />
      <Canvas ref={canvasRef} width="600" height="600" />
      <ButtonWrapper gap="1rem">
        <Button size="medium" onClick={handleSaveClick}>
          저장하기
        </Button>
        <Button size="medium" onClick={handleShareClick}>
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

const Canvas = styled.canvas`
  max-width: 95%;
`;
