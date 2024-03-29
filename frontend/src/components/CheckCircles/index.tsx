import { BsCheckCircleFill, BsFillCircleFill } from 'react-icons/bs';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox } from 'components';
import { CheckCircleProps, CheckCirclesProps } from 'components/CheckCircles/type';

const totalCheck = [...Array(3)];

export const CheckCircles = ({ progressCount }: CheckCirclesProps) => {
  return (
    <FlexBox gap="1rem" role="group" aria-label={`${progressCount}번 인증완료`}>
      {totalCheck.map((_, index) => (
        <CheckCircle key={index} checkCircleCount={index} progressCount={progressCount} />
      ))}
    </FlexBox>
  );
};

const CheckCircle = ({ checkCircleCount, progressCount }: CheckCircleProps) => {
  const themeContext = useThemeContext();

  if (checkCircleCount < progressCount) {
    return (
      <BsCheckCircleFill color={themeContext.primary} size={27} aria-hidden={true} />
    );
  }

  return (
    <BsFillCircleFill
      style={{ border: `1px solid ${themeContext.surface}`, borderRadius: '50%' }}
      color={themeContext.secondary}
      size={27}
      aria-hidden={true}
    />
  );
};
