import { CheckSuccessProps, FeedItemProps, WrapperProps } from './type';
import useFeedItem from './useFeedItem';
import { FaCrown } from 'react-icons/fa';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, UnderLineText, CheckCircles } from 'components';

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

  return (
    <Wrapper
      flexDirection="column"
      gap="0.4rem"
      onClick={handleClickFeed}
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
    </Wrapper>
  );
};

const CheckSuccessCycle = ({ isSuccess }: CheckSuccessProps) => {
  const themeContext = useThemeContext();

  if (!isSuccess) {
    return null;
  }

  return (
    <SuccessIconWrapper>
      <FaCrown color={themeContext.primary} size={25} />
    </SuccessIconWrapper>
  );
};

const Wrapper = styled(FlexBox)<WrapperProps>`
  ${({ isDetailPage }) => css`
    max-width: 440px;
    min-width: 366px;
    padding: 20px 0;
    cursor: pointer;
    pointer-events: ${isDetailPage ? 'none' : 'auto'};

    @media all and (max-width: 366px) {
      min-width: auto;
    }
  `}
`;

const ProfileImg = styled.img`
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
`;

const Nickname = styled(Text)`
  margin-left: 0.313rem;
`;

const OfText = styled(Text)`
  text-align: end;
`;

const ChallengeName = styled(UnderLineText)`
  pointer-events: auto;
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

const SuccessIconWrapper = styled.div`
  margin-left: 5px;
`;
