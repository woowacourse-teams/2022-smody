import useFeed from './useFeed';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

export const Feed = () => {
  const themeContext = useThemeContext();
  const picture = 'https://emoji-copy.com/wp-content/uploads/1f64a.png';
  const nickname = '빅터';
  const progressImage =
    'https://health.chosun.com/site/data/img_dir/2018/03/07/2018030700812_2.jpg';
  const description =
    '오늘도 만보 걸었다. 날씨가 선선해서 좋았다. 하지만 혼자 걸으니 외롭다. 누군가 같이 걸을 사람이 있으면 좋겠는데...';
  const progressTime = '2022-08-08T10:00:00';
  const challengeId = 1;
  const challengeName = '하루 만보 걷기';
  const commentCount = 4;

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
