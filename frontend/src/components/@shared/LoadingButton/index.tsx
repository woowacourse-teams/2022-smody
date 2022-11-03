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
  if (isLoading) {
    return (
      <Element size="large" disabled={true}>
        <FlexBox
          flexDirection="row"
          gap="1rem"
          justifyContent="center"
          alignItems="center"
        >
          {loadingText}
          <LoadingDots />
        </FlexBox>
      </Element>
    );
  }

  if (isSuccess) {
    return (
      <Element size="large" disabled={true}>
        {successText}
      </Element>
    );
  }

  return (
    <Element size="large" disabled={isDisabled}>
      {defaultText}
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
