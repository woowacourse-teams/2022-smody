import { FeedItemProps, WrapperProps } from './type';
import useFeedItem from './useFeedItem';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, UnderLineText } from 'components';

export const FeedItem = ({
  cycleDetailId,
  picture,
  nickname,
  progressImage,
  description,
  progressTime,
  challengeId,
  challengeName,
  commentCount,
  isClickable = true,
  isShowBriefChallengeName = true,
}: FeedItemProps) => {
  const themeContext = useThemeContext();

  const {
    year,
    month,
    date,
    hours,
    minutes,
    renderedChallengeName,
    handleClickFeed,
    handleClickChallengeName,
  } = useFeedItem({
    challengeId,
    cycleDetailId,
    progressTime,
    challengeName,
    isShowBriefChallengeName,
  });

  return (
    <Wrapper
      flexDirection="column"
      gap="0.4rem"
      onClick={handleClickFeed}
      isClickable={isClickable}
    >
      <FlexBox alignItems="center">
        <ProfileImg src={picture} alt={`${nickname}님의 프로필 사진`} loading="lazy" />
        <FlexBox alignItems="center">
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
            {challengeName.length > 9
              ? `${challengeName.substring(0, 9)}...`
              : challengeName}
          </ChallengeName>
        </FlexBox>
      </FlexBox>
      <ProgressImg
        src={progressImage}
        alt={`${nickname}님의 ${challengeName} 인증 사진`}
        loading="lazy"
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

const Wrapper = styled(FlexBox)<WrapperProps>`
  ${({ isClickable }) => css`
    max-width: 440px;
    min-width: 366px;
    padding: 20px 0;
    cursor: pointer;
    pointer-events: ${isClickable ? 'auto' : 'none'};

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

const ProgressImg = styled.img`
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 20px;
  background-color: white;
  margin: 0.5rem 0;
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
