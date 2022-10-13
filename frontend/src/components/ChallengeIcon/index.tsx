import styled, { css } from 'styled-components';

import { FlexBox } from 'components';
import { ChallengeIconProps } from 'components/ChallengeIcon/type';

import { emojiList } from 'constants/style';

const SIZES = {
  small: { width: '2.6rem', fontSize: '1.6rem' },
  medium: { width: '4rem', fontSize: '2.2rem' },
  large: { width: '7.5rem', fontSize: '4.375rem' },
};

export const ChallengeIcon = ({
  size,
  bgColor,
  emojiIndex,
  challengeId,
}: ChallengeIconProps) => {
  return (
    <Wrapper size={size} bgColor={bgColor} justifyContent="center" alignItems="center">
      {emojiList[emojiIndex]}
    </Wrapper>
  );
};

export const Wrapper = styled(FlexBox)<Pick<ChallengeIconProps, 'size' | 'bgColor'>>`
  ${({ size, bgColor }) => css`
    min-width: ${SIZES[size].width};
    max-width: ${SIZES[size].width};
    min-height: ${SIZES[size].width};
    max-height: ${SIZES[size].width};
    border-radius: 50%;
    background-color: ${bgColor};
    font-size: ${SIZES[size].fontSize};
  `}
`;
