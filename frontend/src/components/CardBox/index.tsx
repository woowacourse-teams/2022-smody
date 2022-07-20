import styled, { css } from 'styled-components';

import { FlexBox, Text } from 'components';
import { CardBoxProps, WrapperProps } from 'components/CardBox/type';

import COLOR from 'styles/color';

export const CardBox = ({
  challengeId,
  challengeName,
  successCount,
  bgColor,
  emoji,
}: CardBoxProps) => {
  return (
    <Wrapper bgColor={bgColor}>
      <TitleWrapper size={14} color={COLOR.BLACK} fontWeight="bold">
        {challengeName}
      </TitleWrapper>
      <EmojiWrapper size={40} color={COLOR.BLACK} fontWeight="normal">
        {emoji}
      </EmojiWrapper>
      <TextWrapper size={10} color={COLOR.DARK_GRAY} fontWeight="bold">
        챌린지 {successCount}회({successCount * 3}일) 성공
      </TextWrapper>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
})`
  ${({ bgColor }: WrapperProps) => css`
    background-color: ${bgColor};
    border: 0;
    width: 128px;
    height: 128px;
    cursor: pointer;
  `}
`;

const TitleWrapper = styled(Text).attrs((props) => ({ ...props }))`
  width: 108px;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
`;

const EmojiWrapper = styled(Text).attrs((props) => ({ ...props }))`
  margin-top: 12px;
`;

const TextWrapper = styled(Text).attrs((props) => ({ ...props }))`
  word-break: keep-all;
  text-align: center;
  margin-top: 20px;
`;
