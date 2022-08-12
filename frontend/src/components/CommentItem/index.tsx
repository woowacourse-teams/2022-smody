import { CommentItemProps } from './type';
import styled from 'styled-components';
import { parseTime } from 'utils';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

export const CommentItem = ({
  commentId,
  memberId,
  nickname,
  picture,
  content,
  createdAt,
  isWriter,
}: CommentItemProps) => {
  const themeContext = useThemeContext();
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
        <Content size={16} color={themeContext.mainText}>
          {content}
        </Content>
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
  flex-wrap: wrap;
`;

const Content = styled(Text)`
  line-height: 1.5;
  word-wrap: break-word;
  word-break: break-all;
`;
