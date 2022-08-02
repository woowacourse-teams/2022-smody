import { BsCheckCircleFill, BsFillCircleFill } from 'react-icons/bs';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox } from 'components';
import { CheckCirclesProps } from 'components/CheckCircles/type';

const totalCheck = [...Array(3)];

export const CheckCircles = ({ progressCount }: CheckCirclesProps) => {
  const themeContext = useThemeContext();

  return (
    <Wrapper>
      {totalCheck.map((_, index) => {
        if (index < progressCount) {
          return <BsCheckCircleFill key={index} color={themeContext.primary} size={27} />;
        }
        return <BsFillCircleFill key={index} color={themeContext.secondary} size={27} />;
      })}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  gap: '1rem',
})``;
