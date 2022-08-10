import styled from 'styled-components';
import { parseTime } from 'utils';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

export const Comment = () => {
  const themeContext = useThemeContext();
  const nickname = '테스트닉네임';
  const picture = 'https://emoji-copy.com/wp-content/uploads/1f64a.png';
  const content = '오!!! 정말 열심히 하셨네요ㅎㅎ 저도 좋은 자극 받고 갑니당ㅎㅎ';
  const createdAt = '2022-08-08T10:00:00';
  const isWriter = true;

  const { year, month, date, hours, minutes } = parseTime(createdAt);

  return (
    <Wrapper flexDirection="column">
      <FlexBox alignItems="center">
        <ProfileImg src={picture} alt={`${nickname}님의 프로필 사진`} />
        <Text
          style={{ marginLeft: '0.625rem' }}
          size={16}
          color={themeContext.onBackground}
        >
          {nickname}
        </Text>
        {isWriter && (
          <Text
            style={{ marginLeft: '0.063rem' }}
            size={12}
            color={themeContext.mainText}
          >
            (작성자)
          </Text>
        )}
        <Text
          style={{ marginLeft: 'auto' }}
          size={12}
          color={themeContext.mainText}
        >{`${year}.${month}.${date} ${hours}:${minutes}`}</Text>
      </FlexBox>
      <ContentWrapper>
        <Text size={16} color={themeContext.mainText}>
          {content}
        </Text>
      </ContentWrapper>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  width: 100%;
`;

const ProfileImg = styled.img`
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 50%;
`;

const ContentWrapper = styled(FlexBox)`
  margin-top: 0.625rem;
  margin-left: 3.125rem;
`;
