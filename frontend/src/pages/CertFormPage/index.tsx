import { MentionableInput } from '../../components/MentionableInput';
import useCertFormPage from './useCertFormPage';
import Plus from 'assets/plus.svg';
import styled, { css } from 'styled-components';

import { CertImageWrapperProps, TextAreaProps } from 'pages/CertFormPage/type';

import {
  FlexBox,
  Text,
  CheckCircles,
  Title,
  ChallengeIcon,
  LoadingButton,
} from 'components';
import { SuccessModal } from 'components/SuccessModal';

import { MAX_TEXTAREA_LENGTH } from 'constants/domain';
import { CLIENT_PATH } from 'constants/path';
import { TITLE_HEIGHT, colorList } from 'constants/style';

const certImageCompressionOptions = {
  maxSizeMB: 0.2,
  maxWidthOrHeight: 1000,
};

const CertFormPage = () => {
  const {
    themeContext,
    isDark,
    cycleId,
    challengeId,
    challengeName,
    successCount,
    progressCount,
    emojiIndex,
    colorIndex,
    isButtonDisabled,
    isLoadingPost,
    isSuccessPost,
    isSuccessModalOpen,
    description,
    setDescription,
    previewImageUrl,
    handleImageInputButtonClick,
    renderImageInput,
    handleSubmitCert,
    handleCloseModal,
    editableElementRef,
    mentionedMemberIds,
    setMentionedMemberIds,
  } = useCertFormPage();

  return (
    <Wrapper flexDirection="column" alignItems="center">
      <Title text="인증하기" linkTo={CLIENT_PATH.CERT} />

      <CertInfoWrapper
        flexDirection="row"
        justifyContent="space-evenly"
        alignItems="center"
        gap="1rem"
      >
        <FlexBox flexDirection="column" alignItems="center">
          <ChallengeNameText size={20} fontWeight="bold" color={themeContext.primary}>
            {challengeName}
          </ChallengeNameText>
          <CheckCircles progressCount={progressCount} />
        </FlexBox>

        <ChallengeIcon
          emojiIndex={emojiIndex}
          challengeId={challengeId}
          size="medium"
          bgColor={colorList[colorIndex]}
        />
      </CertInfoWrapper>
      <form onSubmit={handleSubmitCert}>
        <section>
          <MiniTitle size={14} color={themeContext.mainText}>
            사진
          </MiniTitle>
          <CertImageWrapper
            role="button"
            onClick={handleImageInputButtonClick}
            justifyContent="center"
            alignItems="center"
            isSelectImage={!!previewImageUrl}
            aria-label="이미지 업로드"
          >
            {previewImageUrl && (
              <CertImage src={previewImageUrl} alt="챌린지 인증 이미지" />
            )}
            <Plus />
          </CertImageWrapper>
          {renderImageInput(certImageCompressionOptions)}
        </section>

        <FlexBox flexDirection="column">
          <MiniTitle size={14} color={themeContext.mainText}>
            기록
          </MiniTitle>
          <TextAreaWrapper isDark={isDark}>
            <MentionableInput
              editableElementRef={editableElementRef}
              editableElement={<TextArea isDark={isDark} />}
              setContent={setDescription}
              mentionedMemberIds={mentionedMemberIds}
              setMentionedMemberIds={setMentionedMemberIds}
            />
          </TextAreaWrapper>
          <TextLength as="span" size={12} color={themeContext.mainText}>
            {description.length}/{MAX_TEXTAREA_LENGTH}
          </TextLength>
        </FlexBox>
        <LoadingButton
          isDisabled={isButtonDisabled}
          isLoading={isLoadingPost}
          isSuccess={isSuccessPost}
          defaultText="인증하기"
          loadingText="업로드 중"
          successText="인증완료"
        />
      </form>
      {isSuccessModalOpen && (
        <SuccessModal
          handleCloseModal={handleCloseModal}
          cycleId={cycleId}
          challengeId={challengeId}
          challengeName={challengeName}
          successCount={successCount}
          progressCount={progressCount + 1}
          emojiIndex={emojiIndex}
        />
      )}
    </Wrapper>
  );
};

export default CertFormPage;

const Wrapper = styled(FlexBox)`
  padding-top: ${TITLE_HEIGHT};
`;

const CertInfoWrapper = styled(FlexBox)`
  width: 100%;
  padding: 0 2.5rem;
`;

const CertImageWrapper = styled(FlexBox)<CertImageWrapperProps>`
  ${({ theme, isSelectImage }) => css`
    background-color: ${theme.background};
    width: 100%;
    height: 18rem;
    border-radius: 20px;
    margin-bottom: 1rem;
    cursor: pointer;

    & svg {
      display: ${isSelectImage ? 'none' : 'block'};
      width: 2rem;
      height: 2rem;
    }

    & path {
      fill: ${theme.primary};
      stroke: none;
    }

    &:hover {
      & svg {
        display: block;
        position: absolute;
      }

      & path {
        fill: ${isSelectImage && theme.primary};
      }
    }
  `}
`;

const ChallengeNameText = styled(Text)`
  margin-bottom: 0.8rem;
`;

const MiniTitle = styled(Text).attrs({ 'aria-label': '이미지 업로드 라벨' })`
  margin: 2rem 0 0.5rem 1rem;
`;

const CertImage = styled.img`
  width: 100%;
  height: 100%;
  border-radius: 20px;
  object-fit: cover;
`;

const TextAreaWrapper = styled.div<TextAreaProps>`
  ${({ theme, isDark }) => css`
    padding: 1rem 0 1rem 1rem;
    border-radius: 20px;
    background-color: ${isDark ? theme.input : theme.background};

    &:focus-within {
      outline: ${theme.primary} 2px solid;
    }
  `}
`;

const TextArea = styled.div<TextAreaProps>`
  ${({ theme, isDark }) => css`
    width: 431px;
    overflow-y: scroll;
    min-height: 137px;
    max-height: 137px;
    padding-right: 1rem;
    border: none;
    resize: none;
    font-size: 1rem;
    background-color: ${isDark ? theme.input : theme.background};
    color: ${theme.onInput};
    outline: none;

    /* 스크롤바 설정*/
    &::-webkit-scrollbar {
      width: 4px;
    }

    /* 스크롤바 막대 설정*/
    &::-webkit-scrollbar-thumb {
      height: 17%;
      background-color: ${theme.primary};
      border-radius: 100px;
    }

    /* 스크롤바 뒷 배경 설정*/
    &::-webkit-scrollbar-track {
      background-color: ${theme.surface};
      border-radius: 100px;
    }
  `}
`;

const TextLength = styled(Text)`
  margin: 0.5rem 0 1.5rem auto;
  align-self: flex-end;
`;
