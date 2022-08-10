import useFeed from './useFeed';
import { Feed } from 'commonType';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

export const FeedItem = ({
  cycleDetailId,
  memberId,
  picture,
  nickname,
  progressImage,
  description,
  progressTime,
  challengeId,
  challengeName,
  commentCount,
}: Feed) => {
  const themeContext = useThemeContext();

  const { year, month, date, hours, minutes, handleClickFeed, handleClickChallengeName } =
    useFeed({ challengeId, progressTime });

  return (
    <Wrapper flexDirection="column" gap="0.625rem" onClick={handleClickFeed}>
      <FlexBox alignItems="center">
        <ProfileImg src={picture} alt={`${nickname}님의 프로필 사진`} />
        <Text style={{ marginLeft: '0.313rem' }} size={16} color={themeContext.mainText}>
          {nickname}
        </Text>
        <Text
          style={{ marginLeft: 'auto' }}
          size={20}
          color={themeContext.primary}
          fontWeight="bold"
          onClick={handleClickChallengeName}
        >
          {challengeName}
        </Text>
      </FlexBox>
      <ProgressImg
        src={progressImage}
        alt={`${nickname}님의 ${challengeName} 인증 사진`}
      />
      <Text
        style={{ alignSelf: 'flex-end' }}
        size={12}
        color={themeContext.mainText}
      >{`${year}.${month}.${date} ${hours}:${minutes}`}</Text>
      <Text size={16} color={themeContext.mainText}>
        {description}
      </Text>
      <Divider />
      <Text
        style={{ alignSelf: 'flex-end' }}
        size={12}
        color={themeContext.primary}
        fontWeight="bold"
      >
        {`댓글 ${commentCount}개 보기`}
      </Text>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  width: 100%;
  max-width: 440px;
  min-width: 366px;
  padding: 20px 0;
  cursor: pointer;
`;

const ProfileImg = styled.img`
  width: 2.563rem;
  height: 2.563rem;
  border-radius: 50%;
`;

const ProgressImg = styled.img`
  width: 100%;
  border-radius: 20px;
`;

const Divider = styled.div`
  ${({ theme }) => css`
    width: 100%;
    height: 2px;
    background-color: ${theme.secondary};
  `}
`;
