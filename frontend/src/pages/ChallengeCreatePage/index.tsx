import { ColorDivProps, ColorListProps, EmojiListProps } from './type';
import useChallengeCreatePage from './useChallengeCreatePage';
import { useState } from 'react';
import styled, { css } from 'styled-components';

import {
  BottomSheet,
  Button,
  FlexBox,
  Input,
  LoadingButton,
  ThumbnailWrapper,
  Title,
  ValidationMessage,
} from 'components';

import { CLIENT_PATH } from 'constants/path';
import { colorList, emojiList } from 'constants/style';

const EmojiSelectValidateMessage = {
  isValidated: false,
  message: '색상과 이모지를 선택해주세요.',
};

const ChallengeCreatePage = () => {
  const {
    challengeName,
    challengeDescription,
    colorSelectedIndex,
    emojiSelectedIndex,
    isEmojiBottomSheetOpen,
    isLoadingPostChallenge,
    isSuccessPostChallenge,
    isInvalidEmojiSelect,
    isAllValidated,
    handleSelectColor,
    handleSelectEmoji,
    handleClickCreateChallenge,
    handleClickEmojiButton,
    handleCloseBottomSheet,
  } = useChallengeCreatePage();

  return (
    <FlexBox flexDirection="column">
      <Title text="새로운 챌린지 생성" linkTo={CLIENT_PATH.SEARCH} />
      <ChallengeCreateForm onSubmit={handleClickCreateChallenge}>
        <Input
          type="text"
          label="챌린지 제목"
          placeholder="챌린지 제목을 입력해주세요"
          {...challengeName}
        />
        <Input
          isTextArea={true}
          needWordLength={true}
          maxLength={255}
          label="챌린지 설명"
          placeholder="챌린지 설명을 입력해주세요"
          {...challengeDescription}
        />
        <EmojiWrapper gap="2rem">
          <PreviewWrapper flexDirection="column" gap="1.5rem">
            <Label as="p">이모지 미리 보기</Label>
            <ThumbnailWrapper
              size="large"
              bgColor={colorSelectedIndex === -1 ? 'gray' : colorList[colorSelectedIndex]}
            >
              {emojiSelectedIndex !== -1 && emojiList[emojiSelectedIndex]}
            </ThumbnailWrapper>
          </PreviewWrapper>
          <EmojiSelectWrapper flexDirection="column" gap="1.5rem">
            <Label>색상 선택</Label>
            <ColorList
              colorSelectedIndex={colorSelectedIndex}
              handleSelectColor={handleSelectColor}
            />
            <Label>이모지 선택</Label>
            <Button size="medium" onClick={handleClickEmojiButton} type="button">
              이모지 리스트 보기
            </Button>
            {isEmojiBottomSheetOpen && (
              <BottomSheet handleCloseBottomSheet={handleCloseBottomSheet}>
                <EmojiList
                  emojiSelectedIndex={emojiSelectedIndex}
                  handleSelectEmoji={handleSelectEmoji}
                />
              </BottomSheet>
            )}
            {isInvalidEmojiSelect && (
              <ValidationMessage value="value" {...EmojiSelectValidateMessage} />
            )}
          </EmojiSelectWrapper>
        </EmojiWrapper>
        <ButtonWrapper>
          <LoadingButton
            isDisabled={!isAllValidated}
            isLoading={isLoadingPostChallenge}
            isSuccess={isSuccessPostChallenge}
            defaultText="챌린지 생성하기"
            loadingText="새로운 챌린지 생성중"
            successText="생성 완료"
          />
        </ButtonWrapper>
      </ChallengeCreateForm>
    </FlexBox>
  );
};

export default ChallengeCreatePage;

const ColorList = ({ colorSelectedIndex, handleSelectColor }: ColorListProps) => {
  return (
    <RadioList as="ul" gap="1rem">
      {colorList.map((color, index) => (
        <li key={index}>
          <InvisibleInput
            type="radio"
            id={`color${index}`}
            name="mainColorList"
            value={index}
            checked={colorSelectedIndex === index}
            onChange={handleSelectColor}
          />
          <label htmlFor={`color${index}`}>
            <ColorDiv color={color} />
          </label>
        </li>
      ))}
    </RadioList>
  );
};

const EmojiList = ({ emojiSelectedIndex, handleSelectEmoji }: EmojiListProps) => {
  return (
    <RadioList as="ul" gap="1rem">
      {emojiList.map((emoji, index) => (
        <li key={index}>
          <InvisibleInput
            type="radio"
            id={`emoji${index}`}
            name="mainEmojiList"
            value={index}
            checked={emojiSelectedIndex === index}
            onChange={handleSelectEmoji}
          />
          <label htmlFor={`emoji${index}`}>
            <EmojiDiv>{emoji}</EmojiDiv>
          </label>
        </li>
      ))}
    </RadioList>
  );
};

const ChallengeCreateForm = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  width: 100%;
  min-width: 390px;
  margin: 1rem 0;
`;

const RadioList = styled(FlexBox)`
  flex-wrap: wrap;
  width: 80%;

  @media all and (max-width: 43rem) {
    width: 19rem;
  }
`;

const Label = styled.label`
  ${({ theme }) => css`
    font-size: 1rem;
    color: ${theme.disabled};
  `}
`;

const EmojiWrapper = styled(FlexBox)`
  /* PC (해상도 1024px)*/
  /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
  @media all and (min-width: 768px) {
    flex-direction: row;
  }

  /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
  @media all and (max-width: 767px) {
    flex-direction: column;
  }
`;

const PreviewWrapper = styled(FlexBox)`
  ${Label} {
    width: 10rem;
  }
`;

const EmojiSelectWrapper = styled(FlexBox)`
  flex-grow: 1;
`;

const InvisibleInput = styled.input`
  display: none;

  &:checked + label div {
    transform: scale(1.2);
  }
`;

const ColorDiv = styled.div<ColorDivProps>`
  ${({ color }) => css`
    width: 3rem;
    height: 3rem;
    background-color: ${color};
    border-radius: 50%;
    cursor: pointer;
  `}
`;

const EmojiDiv = styled.div`
  font-size: 2.3rem;
  cursor: pointer;
`;

const ButtonWrapper = styled.div`
  width: 80%;
  margin: 3rem auto 0;
`;
