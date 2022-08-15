import { FeedItemProps, WrapperProps } from './type';
import useFeedItem from './useFeedItem';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

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
}: FeedItemProps) => {
  const themeContext = useThemeContext();

  const { year, month, date, hours, minutes, handleClickFeed, handleClickChallengeName } =
    useFeedItem({ challengeId, cycleDetailId, progressTime });

  return (
    <Wrapper
      flexDirection="column"
      gap="0.8rem"
      onClick={handleClickFeed}
      isClickable={isClickable}
    >
      <FlexBox alignItems="center">
        <ProfileImg src={picture} alt={`${nickname}님의 프로필 사진`} />
        <Nickname size={16} color={themeContext.mainText}>
          {nickname}
        </Nickname>
        <ChallengeName
          size={20}
          color={themeContext.primary}
          fontWeight="bold"
          onClick={handleClickChallengeName}
        >
          {challengeName}
        </ChallengeName>
      </FlexBox>
      <ProgressImg
        src={progressImage}
        alt={`${nickname}님의 ${challengeName} 인증 사진`}
      />
      <Date
        size={14}
        color={themeContext.mainText}
      >{`${year}.${month}.${date} ${hours}:${minutes}`}</Date>
      <MainText size={16} color={themeContext.onBackground}>
        {description}
      </MainText>
      {/* <Divider /> */}
      <CommentCount size={14} color={themeContext.mainText}>
        {`댓글 ${commentCount}개 보기`}
      </CommentCount>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)<WrapperProps>`
  ${({ isClickable }) => css`
    width: 400px;
    padding: 20px 0;
    cursor: pointer;
    pointer-events: ${isClickable ? 'auto' : 'none'};

    @media all and (max-width: 400px) {
      width: 366px;
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

const ChallengeName = styled(Text)`
  margin-left: auto;
  pointer-events: auto;
`;

const ProgressImg = styled.img`
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 20px;
  background-color: white;
`;

const Date = styled(Text)`
  align-self: flex-start;
  padding-left: 4px;
`;

const MainText = styled(Text)`
  align-self: flex-start;
  padding-left: 4px;
`;

const CommentCount = styled(Text)`
  align-self: flex-start;
  padding-left: 4px;
`;

const Divider = styled.div`
  ${({ theme }) => css`
    width: 100%;
    height: 2px;
    background-color: ${theme.secondary};
  `}
`;
