import { CommentItemProps } from './type';
import MoreIcon from 'assets/more.svg';
import styled from 'styled-components';
import { parseTime } from 'utils';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

export const CommentItem = ({
  nickname,
  picture,
  content,
  createdAt,
  isWriter,
  isMyComment,
  handleClickMenuButton,
}: CommentItemProps) => {
  const themeContext = useThemeContext();
  const { year, month, date, hours, minutes } = parseTime(createdAt);

  return (
    <Wrapper flexDirection="column">
      <FlexBox alignItems="center">
        <ProfileImg src={picture} alt={`${nickname}님의 프로필 사진`} />
        <CommentInfoWrapper flexDirection="column">
          <FlexBox>
            <Text size={16} color={themeContext.onBackground}>
              {nickname}
            </Text>
            {isWriter && (
              <Writer size={12} color={themeContext.mainText}>
                (작성자)
              </Writer>
            )}
          </FlexBox>
          <Text
            size={12}
            color={themeContext.mainText}
          >{`${year}.${month}.${date} ${hours}:${minutes}`}</Text>
        </CommentInfoWrapper>
        {isMyComment && (
          <MoreIconWrapper onClick={handleClickMenuButton}>
            <MoreIcon />
          </MoreIconWrapper>
        )}
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

const CommentInfoWrapper = styled(FlexBox)`
  margin-left: 1.125rem;
`;

const Writer = styled(Text)`
  margin-left: 0.063rem;
`;

const MoreIconWrapper = styled.div`
  margin-left: auto;
  padding-left: 0.5rem;
  cursor: pointer;
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
