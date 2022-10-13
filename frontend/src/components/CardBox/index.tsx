import { useCardBox } from './useCardBox';
import styled, { css } from 'styled-components';

import { FlexBox, Text, ChallengeIcon } from 'components';
import { CardBoxProps, WrapperProps } from 'components/CardBox/type';

import { colorList, emojiList } from 'constants/style';

import COLOR from 'styles/color';

export const CardBox = ({
  challengeId,
  challengeName,
  successCount,
  emojiIndex,
  colorIndex,
}: CardBoxProps) => {
  const handleNavigateMyChallenge = useCardBox({ challengeId });

  return (
    <Wrapper
      onClick={handleNavigateMyChallenge}
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      bgColor={colorList[colorIndex]}
    >
      <TitleWrapper
        aria-label="성공한 챌린지 이름"
        size={14}
        color={COLOR.BLACK}
        fontWeight="bold"
      >
        {challengeName}
      </TitleWrapper>
      <ChallengeIcon size="medium" bgColor={colorList[colorIndex]}>
        {emojiList[emojiIndex]}
      </ChallengeIcon>
      <TextWrapper size={14} color={COLOR.DARKEST_GRAY} fontWeight="bold">
        <Count color={COLOR.PURPLE}>{successCount}</Count>회 성공
      </TextWrapper>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  ${({ bgColor }: WrapperProps) => css`
    padding: 5px;
    background-color: ${bgColor};
    border: 0;
    width: 128px;
    height: 128px;
    border-radius: 5px;
    cursor: pointer;
  `}
`;

const TitleWrapper = styled(Text)`
  width: 108px;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
`;

const TextWrapper = styled(Text).attrs((props) => ({ ...props }))`
  word-break: keep-all;
  text-align: center;
`;

const Count = styled.span`
  color: ${COLOR.PURPLE};
`;
