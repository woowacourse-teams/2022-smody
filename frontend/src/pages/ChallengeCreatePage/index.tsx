import useChallengeCreatePage from './useChallengeCreatePage';
import styled, { css } from 'styled-components';

import { FlexBox, Input, LoadingButton, ThumbnailWrapper, Title } from 'components';

import { CLIENT_PATH } from 'constants/path';
import { colorList, emojiList } from 'constants/style';

const ChallengeCreatePage = () => {
  const {
    challengeName,
    challengeDescription,
    colorSelectedIndex,
    emojiSelectedIndex,
    isLoadingPostChallenge,
    isSuccessPostChallenge,
    isAllValidated,
    handleSelectColor,
    handleSelectEmoji,
    handleClickCreateChallenge,
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
        <Label>미리 보기</Label>
        <ThumbnailWrapper
          size="large"
          bgColor={colorSelectedIndex === -1 ? 'gray' : colorList[colorSelectedIndex]}
        >
          {emojiSelectedIndex !== -1 && emojiList[emojiSelectedIndex]}
        </ThumbnailWrapper>
        <Label>대표 색상</Label>
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
        <Label htmlFor="대표 이모지">대표 이모지</Label>
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

interface ColorDivProps {
  color: string;
}

const InvisibleInput = styled.input`
  display: none;

  &:checked + label div {
    transform: scale(1.2);
  }
`;

const ColorDiv = styled.div`
  ${({ color }: ColorDivProps) => css`
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
