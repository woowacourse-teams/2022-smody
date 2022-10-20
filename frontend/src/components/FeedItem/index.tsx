import {
  CheckSuccessProps,
  FeedItemProps,
  ImgCloseButtonProps,
  WrapperProps,
} from './type';
import useFeedItem from './useFeedItem';
import { useState } from 'react';
import { AiFillCloseCircle } from 'react-icons/ai';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import {
  FlexBox,
  Text,
  UnderLineText,
  CheckCircles,
  CheckSuccessCycle,
  ModalOverlay,
} from 'components';

import COLOR from 'styles/color';

export const FeedItem = ({
  cycleDetailId,
  picture,
  nickname,
  progressImage,
  progressCount,
  description,
  progressTime,
  challengeId,
  challengeName,
  commentCount,
  isDetailPage = false,
  isShowBriefChallengeName = true,
}: FeedItemProps) => {
  const themeContext = useThemeContext();

  const {
    year,
    month,
    date,
    hours,
    minutes,
    isSuccess,
    renderedChallengeName,
    handleClickFeed,
    handleClickChallengeName,
  } = useFeedItem({
    challengeId,
    cycleDetailId,
    progressTime,
    progressCount,
    challengeName,
    isShowBriefChallengeName,
  });

  const [isImgModalOpen, setIsImgModalOpen] = useState(false);

  const handleCloseImgModal = () => {
    setIsImgModalOpen(false);
  };

  const handleOpenImgModal = () => {
    setIsImgModalOpen(true);
  };

  return (
    <Wrapper
      flexDirection="column"
      gap="0.4rem"
      onClick={isDetailPage ? undefined : handleClickFeed}
      isDetailPage={isDetailPage}
    >
      <FlexBox alignItems="center" flexWrap="wrap">
        <ProfileImg
          src={picture}
          alt={`${nickname}님의 프로필 사진`}
          loading="lazy"
          width="32px"
          height="32px"
        />

        <Nickname size={20} color={themeContext.onBackground}>
          {nickname}
        </Nickname>
        <OfText size={16} color={themeContext.onBackground}>
          의&nbsp;
        </OfText>
        <ChallengeName
          fontSize={20}
          fontColor={themeContext.onBackground}
          underLineColor={themeContext.primary}
          fontWeight="bold"
          onClick={handleClickChallengeName}
        >
          {renderedChallengeName}
        </ChallengeName>

        <CheckSuccessCycle isSuccess={isSuccess} />
      </FlexBox>
      <CheckCirclesWrapper justifyContent="flex-end">
        <CheckCircles progressCount={progressCount} />
      </CheckCirclesWrapper>
      <ProgressImg
        src={progressImage}
        alt={`${nickname}님의 ${challengeName} 인증 사진`}
        isSuccess={isSuccess}
        onClick={isDetailPage ? handleOpenImgModal : undefined}
      />
      <Text size={14} color={themeContext.mainText}>
        {`${year}.${month}.${date} ${hours}:${minutes}`}
      </Text>
      <MainText size={16} color={themeContext.onBackground}>
        {description}
      </MainText>
      <CommentCount size={14} color={themeContext.mainText}>
        {`댓글 ${commentCount}개`}
      </CommentCount>
      {isImgModalOpen && (
        <ModalOverlay handleCloseModal={handleCloseImgModal} isFullSize={true}>
          <div>
            <ImgCloseButton handleCloseImgModal={handleCloseImgModal} />
            <EntireImg
              src={progressImage}
              alt={`${nickname}님의 ${challengeName} 인증 사진`}
            />
          </div>
        </ModalOverlay>
      )}
    </Wrapper>
  );
};

const ImgCloseButton = ({ handleCloseImgModal }: ImgCloseButtonProps) => {
  return (
    <CloseButton type="button" onClick={handleCloseImgModal} aria-label="닫기">
      <BackWhite>
        <CloseCircleWrapper>
          <AiFillCloseCircle color={COLOR.PURPLE} size={30} />
        </CloseCircleWrapper>
      </BackWhite>
    </CloseButton>
  );
};

const Wrapper = styled(FlexBox)<WrapperProps>`
  ${({ isDetailPage }) => css`
    max-width: 440px;
    min-width: 366px;
    padding: 20px 0;
    ${!isDetailPage &&
    css`
      cursor: pointer;
    `}

    @media all and (max-width: 366px) {
      min-width: auto;
    }
  `}
`;

const ProfileImg = styled.img`
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
  object-fit: cover;
`;

const Nickname = styled(Text)`
  margin-left: 0.313rem;
`;

const OfText = styled(Text)`
  text-align: end;
`;

const ChallengeName = styled(UnderLineText)`
  cursor: pointer;
`;

const ProgressImg = styled.img<CheckSuccessProps>`
  ${({ theme, isSuccess }) => css`
    width: 100%;
    height: 400px;
    object-fit: cover;
    border-radius: 20px;
    background-color: white;
    margin: 0.2rem 0.1rem;
    ${isSuccess &&
    css`
      border: 8px solid transparent;
      background-image: linear-gradient(#fff, #fff),
        linear-gradient(to bottom right, ${theme.primary}, #ffd700);
      background-origin: border-box;
      background-clip: content-box, border-box;
    `};
  `}
`;

const MainText = styled(Text)`
  align-self: flex-start;
  white-space: pre-wrap;
  word-wrap: break-word;
  word-break: break-all;
`;

const CommentCount = styled(Text)`
  align-self: flex-end;
`;

const CheckCirclesWrapper = styled(FlexBox)`
  flex-grow: 1;
`;

const EntireImg = styled.img`
  max-height: 90vh;
  max-width: 90vw;
  object-fit: contain;
`;

const CloseButton = styled.button`
  position: absolute;
  top: 0;
  right: 0;
  margin: 4px;
  text-align: end;
`;

const BackWhite = styled.div`
  position: relative;
  margin: 10px;
  background: #fff;
  width: 15px;
  height: 15px;
  border-radius: 50%;
`;

const CloseCircleWrapper = styled.div`
  position: absolute;
  top: -7px;
  right: -7px;
`;
