import { CertImageWrapperProps } from './type';
import Plus from 'assets/plus.svg';
import { useState, FormEventHandler } from 'react';
import styled, { css } from 'styled-components';
import { getEmoji } from 'utils/emoji';

import useImageInput from 'hooks/useImageInput';
import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, CheckCircles, Title, ThumbnailWrapper, Button } from 'components';

const CertFormPage = () => {
  const themeContext = useThemeContext();
  const [comment, setComment] = useState('');
  const {
    previewImageUrl,
    sendImageToServer,
    handleImageInputButtonClick,
    renderImageInput,
  } = useImageInput('progressImage');
  const CHALLENGE_ID = 3;
  const CHALLENGE_NAME = '하루에 만보 걷기';
  const PROGRESS_COUNT = 2;

  const handleSubmitCert: FormEventHandler<HTMLFormElement> = (event) => {
    event.preventDefault();

    // sendImageToServer();
  };

  return (
    <FlexBox flexDirection="column" alignItems="center">
      <Title text="인증하기" />
      <CertInfoWrapper justifyContent="space-between">
        <FlexBox flexDirection="column">
          <Text
            size={20}
            fontWeight="bold"
            color={themeContext.primary}
            style={{ marginBottom: '0.8rem' }}
          >
            {CHALLENGE_NAME}
          </Text>
          <CheckCircles progressCount={PROGRESS_COUNT} />
        </FlexBox>

        <ThumbnailWrapper size="medium" bgColor="#FED6D6">
          {getEmoji(Number(CHALLENGE_ID))}
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
            onChange={(event) => {
              setComment(event.target.value);
            }}
          >
            {comment}
          </TextArea>
          <TextLength
            as="span"
            size={12}
            color={themeContext.mainText}
            style={{ marginLeft: 'auto' }}
          >
            {comment.length}/255
          </TextLength>
        </Section>
        <Button size="large">인증하기</Button>
      </form>
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

    & svg path {
      fill: ${theme.primary};
      stroke: none;
    }

    &:hover {
      & svg {
        display: block;
        position: absolute;
      }

      & path {
        fill: ${isSelectImage && theme.surface};
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

const TextArea = styled.textarea`
  ${({ theme }) => css`
    border-radius: 20px;
    padding: 1rem;
    width: 26rem;
    border: none;
    resize: none;
    font-size: 1rem;
    font-weight: normal;
    font-family: 'Source Sans Pro', sans-serif;

    &:focus {
      outline: ${theme.primary} 2px solid;
    }
  `}
`;

const TextLength = styled(Text)`
  margin: 0.5rem 0 1.5rem;
  align-self: flex-end;
`;
