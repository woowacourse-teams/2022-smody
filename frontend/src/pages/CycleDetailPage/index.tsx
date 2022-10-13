import useCycleDetailPage from './useCycleDetailPage';
import styled from 'styled-components';
import { parseTime } from 'utils';

import useThemeContext from 'hooks/useThemeContext';

import {
  Text,
  ChallengeIcon,
  CycleDetailList,
  FlexBox,
  Title,
  ShareButton,
} from 'components';

import { CLIENT_PATH } from 'constants/path';
import { emojiList, colorList } from 'constants/style';

const CycleDetailPage = () => {
  const themeContext = useThemeContext();
  const cycleDetailData = useCycleDetailPage();

  if (typeof cycleDetailData === 'undefined') {
    return null;
  }

  const { challengeName, startTime, cycleDetails, emojiIndex, colorIndex } =
    cycleDetailData.data;
  const { year, month, date } = parseTime(startTime);

  return (
    <div>
      <Title text="인증 기록 보기" linkTo={CLIENT_PATH.CERT}>
        <ShareButton
          text={`${year}년 ${month}월 ${date}일부터 도전한 ${challengeName} 작심삼일 극복 챌린지 기록을 공유해요`}
        />
      </Title>
      <ChallengeDetailWrapper
        flexDirection="row"
        justifyContent="space-evenly"
        alignItems="center"
        gap="1rem"
      >
        <FlexBox flexDirection="column" alignItems="center" gap="1rem">
          <Text size={20} fontWeight="bold" color={themeContext.primary}>
            {challengeName}
          </Text>
          <Text color={themeContext.onBackground}>
            {year}년 {month}월 {date}일부터 작심삼일 극복 도전
          </Text>
        </FlexBox>
        <ChallengeIcon size="medium" bgColor={colorList[colorIndex]}>
          {emojiList[emojiIndex]}
        </ChallengeIcon>
      </ChallengeDetailWrapper>
      <CycleDetailListWrapper>
        <CycleDetailList cycleDetails={cycleDetails} />
      </CycleDetailListWrapper>
    </div>
  );
};

export default CycleDetailPage;

const ChallengeDetailWrapper = styled(FlexBox)`
  line-height: 1rem;
  margin-bottom: 2rem;
`;

const CycleDetailListWrapper = styled(FlexBox)`
  width: 100%;
  height: 50vh;
`;
