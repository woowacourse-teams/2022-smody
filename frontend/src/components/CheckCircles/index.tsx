import { BsCheckCircleFill, BsCircle } from 'react-icons/bs';
import styled from 'styled-components';

import { FlexBox } from 'components';
import { CheckCirclesProps } from 'components/CheckCircles/type';

const totalCheck = [...Array(3)];

export const CheckCircles = ({ progressCount }: CheckCirclesProps) => {
  return (
    <Wrapper>
      {totalCheck.map((_, index) => {
        if (index < progressCount) {
          return <BsCheckCircleFill key={index} />;
        }
        return <BsCircle key={index} />;
      })}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  gap: '1rem',
})``;
