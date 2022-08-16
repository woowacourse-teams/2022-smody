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
        <FlexBox alignItems="flex-end">
          <Nickname size={20} color={themeContext.onBackground}>
            {nickname}
          </Nickname>
          <Text style={{ textAlign: 'end' }} size={16} color={themeContext.onBackground}>
            의&nbsp;
          </Text>
          <UnderLineText
            fontSize={20}
            fontColor={themeContext.onBackground}
            underLineColor={themeContext.primary}
            fontWeight="bold"
            onClick={handleClickChallengeName}
          >
            {challengeName}
          </UnderLineText>
        </FlexBox>
      </FlexBox>
      <ProgressImg
        src={progressImage}
        alt={`${nickname}님의 ${challengeName} 인증 사진`}
      />
      <Text size={12} color={themeContext.mainText}>
        {`${year}.${month}.${date} ${hours}:${minutes}`}
      </Text>
      <MainText size={16} color={themeContext.onBackground}>
        {description}
      </MainText>
      <CommentCount size={14} color={themeContext.mainText}>
        {`댓글 ${commentCount}개 보기`}
      </CommentCount>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)<WrapperProps>`
  ${({ isClickable }) => css`
    /* width: 340px; */
    max-width: 440px;
    min-width: 366px;
    padding: 20px 0;
    cursor: pointer;
    pointer-events: ${isClickable ? 'auto' : 'none'};

    /* @media all and (max-width: 500px) {
      width: 95%;
    } */
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

const ProgressImg = styled.img`
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 20px;
  background-color: white;
`;

const MainText = styled(Text)`
  align-self: flex-start;
  padding-left: 4px;
`;

const CommentCount = styled(Text)`
  align-self: flex-start;
  padding-left: 4px;
`;
