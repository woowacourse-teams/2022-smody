import { usePostCycleProgress } from 'apis';
import Plus from 'assets/plus.svg';
import { useState, useMemo, FormEventHandler } from 'react';
import { useLocation, Navigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isDarkState } from 'recoil/darkMode/atoms';
import styled, { css } from 'styled-components';
import { getEmoji } from 'utils/emoji';

import useImageInput from 'hooks/useImageInput';
import useThemeContext from 'hooks/useThemeContext';

import {
  CertImageWrapperProps,
  TextAreaProps,
  CertFormPageLocationState,
} from 'pages/CertFormPage/type';

import { FlexBox, Text, Button, CheckCircles, Title, ThumbnailWrapper } from 'components';
import { SuccessModal } from 'components/SuccessModal';

import { CLIENT_PATH } from 'constants/path';

const FORM_DATA_IMAGE_NAME = 'progressImage';

const CertFormPage = () => {
  const isDark = useRecoilValue(isDarkState);
  const themeContext = useThemeContext();
  const [description, setDescription] = useState('');
  const [isSuccessModalOpen, setIsSuccessModalOpen] = useState(false);

  const {
    previewImageUrl,
    handleImageInputButtonClick,
    renderImageInput,
    hasImageFormData,
    isImageLoading,
    formData,
  } = useImageInput(FORM_DATA_IMAGE_NAME);
  const isButtonDisabled = useMemo(
    () => isImageLoading || !hasImageFormData,
    [isImageLoading, formData],
  );

  const { mutate: postCycleProgress } = usePostCycleProgress({
    onSuccess: () => {
      setIsSuccessModalOpen(true);
    },
  });

  const location = useLocation();

  if (location.state === null) {
    return <Navigate to={CLIENT_PATH.NOT_FOUND} />;
  }

  const { cycleId, challengeId, challengeName, successCount, progressCount } =
    location.state as CertFormPageLocationState;

  const handleSubmitCert: FormEventHandler<HTMLFormElement> = (event) => {
    event.preventDefault();

    formData.append('description', description);

    postCycleProgress({ cycleId, formData });
  };

  const handleCloseModal = () => {
    setIsSuccessModalOpen(false);
  };

  return (
    <FlexBox flexDirection="column" alignItems="center">
      <Title text="인증하기" linkTo={CLIENT_PATH.CERT} />
      <CertInfoWrapper
        flexDirection="row"
        justifyContent="space-evenly"
        alignItems="center"
        gap="1rem"
      >
        <FlexBox flexDirection="column" alignItems="center">
          <Text
            size={20}
            fontWeight="bold"
            color={themeContext.primary}
            style={{ marginBottom: '0.8rem' }}
          >
            {challengeName}
          </Text>
          <CheckCircles progressCount={progressCount} />
        </FlexBox>

        <ThumbnailWrapper size="medium" bgColor="#FED6D6">
          {getEmoji(Number(challengeId))}
        </ThumbnailWrapper>
      </CertInfoWrapper>
      <form onSubmit={handleSubmitCert}>
        <section>
          <Text
            size={14}
            color={themeContext.mainText}
            style={{ margin: '2rem 0 0.5rem 1rem' }}
          >
            사진
          </Text>
          <CertImageWrapper
            onClick={handleImageInputButtonClick}
            justifyContent="center"
            alignItems="center"
            isSelectImage={!!previewImageUrl}
          >
            {previewImageUrl && (
              <CertImage src={previewImageUrl} alt="챌린지 인증 이미지" />
            )}
            <Plus />
          </CertImageWrapper>
          {renderImageInput()}
        </section>

        <Section flexDirection="column">
          <Text
            size={14}
            color={themeContext.mainText}
            style={{ margin: '2rem 0 0.5rem 1rem' }}
          >
            기록
          </Text>
          <TextArea
            cols={50}
            rows={5}
            maxLength={254}
            value={description}
            onChange={(event) => {
              setDescription(event.target.value);
            }}
            isDark={isDark}
          />
          <TextLength
            as="span"
            size={12}
            color={themeContext.mainText}
            style={{ marginLeft: 'auto' }}
          >
            {description.length}/255
          </TextLength>
        </Section>
        <Button size="large" disabled={isButtonDisabled}>
          인증하기
        </Button>
      </form>
      {isSuccessModalOpen && (
        <SuccessModal
          handleCloseModal={handleCloseModal}
          cycleId={cycleId}
          challengeId={challengeId}
          challengeName={challengeName}
          successCount={successCount}
          progressCount={progressCount + 1}
        />
      )}
    </FlexBox>
  );
};

export default CertFormPage;

const CertInfoWrapper = styled(FlexBox)`
  width: 100%;
  padding: 0 2.5rem;
`;

const CertImageWrapper = styled(FlexBox)<CertImageWrapperProps>`
  ${({ theme, isSelectImage }) => css`
    background-color: ${theme.background};
    width: 26rem;
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

const CertImage = styled.img`
  width: 100%;
  height: 100%;
  border-radius: 20px;
  object-fit: cover;
`;

const Section = styled(FlexBox)``;

const TextArea = styled.textarea<TextAreaProps>`
  ${({ theme, isDark }) => css`
    border-radius: 20px;
    padding: 1rem;
    width: 26rem;
    border: none;
    resize: none;
    font-size: 1rem;
    font-weight: normal;
    font-family: 'Source Sans Pro', sans-serif;
    background-color: ${isDark ? theme.input : theme.background};
    color: ${theme.onInput};

    &:focus {
      outline: ${theme.primary} 2px solid;
    }
  `}
`;

const TextLength = styled(Text)`
  margin: 0.5rem 0 1.5rem;
  align-self: flex-end;
`;
