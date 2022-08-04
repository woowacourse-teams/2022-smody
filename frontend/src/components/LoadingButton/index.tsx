import { LoadingButtonProps } from './type';
import LoadingDots from 'assets/loading_dots.svg';
import styled from 'styled-components';

import { Button } from 'components/@shared/Button';
import { FlexBox } from 'components/@shared/FlexBox';

export const LoadingButton = ({
  isDisabled,
  isLoading,
  isSuccess,
  defaultText,
  loadingText,
  successText,
}: LoadingButtonProps) => {
  return (
    <Element size="large" disabled={isDisabled || isLoading || isSuccess}>
      {isLoading ? (
        <FlexBox
          flexDirection="row"
          gap="1rem"
          justifyContent="center"
          alignItems="center"
        >
          {loadingText}
          <LoadingDots />
        </FlexBox>
      ) : isSuccess ? (
        successText
      ) : (
        defaultText
      )}
    </Element>
  );
};

const Element = styled(Button)`
  svg {
    width: 40px;
    height: 40px;
    margin: 0;
    display: inline-block;
  }
`;
