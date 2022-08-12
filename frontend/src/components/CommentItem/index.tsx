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
        <NickName size={16} color={themeContext.onBackground}>
          {nickname}
        </NickName>
        {isWriter && (
          <Writer size={12} color={themeContext.mainText}>
            (작성자)
          </Writer>
        )}
        <Date
          size={12}
          color={themeContext.mainText}
        >{`${year}.${month}.${date} ${hours}:${minutes}`}</Date>
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
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
`;

const NickName = styled(Text)`
  margin-left: 0.625rem;
`;

const Writer = styled(Text)`
  margin-left: 0.063rem;
`;

const Date = styled(Text)`
  margin-left: auto;
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
