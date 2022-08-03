import { useGetCycleById } from 'apis';
import { MdArrowBackIosNew } from 'react-icons/md';
import { useNavigate, useParams } from 'react-router-dom';
import styled from 'styled-components';
import { parseTime } from 'utils';
import { getEmoji } from 'utils/emoji';

import useThemeContext from 'hooks/useThemeContext';

import {
  LoadingSpinner,
  Text,
  ThumbnailWrapper,
  CycleDetailList,
  FlexBox,
} from 'components';

import { cursorPointer } from 'constants/style';

const CycleDetailPage = () => {
  const navigate = useNavigate();
  const { cycleId } = useParams();
  const themeContext = useThemeContext();
  const { data } = useGetCycleById(
    { cycleId: Number(cycleId) },
    {
      refetchOnWindowFocus: false,
    },
  );

  const backToPreviousPage = () => {
    navigate(-1);
  };

  if (typeof data === 'undefined') {
    return <LoadingSpinner />;
  }

  const { challengeId, challengeName, startTime, cycleDetails } = data.data;
  const { year, month, date } = parseTime(startTime);

  return (
    <Wrapper>
      <TitleWrapper flexDirection="row" justifyContent="space-between">
        <MdArrowBackIosNew
          size={20}
          onClick={backToPreviousPage}
          style={{ ...cursorPointer }}
        />
        <Text fontWeight="bold" size={20} color={themeContext.onBackground}>
          인증기록 보기
        </Text>
        <div />
      </TitleWrapper>
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
            {year}년 {month}월 {date}부터 작심삼일 극복 도전
          </Text>
        </FlexBox>
        <ThumbnailWrapper size="medium" bgColor="#FED6D6">
          {getEmoji(Number(challengeId))}
        </ThumbnailWrapper>
      </ChallengeDetailWrapper>
      <CycleDetailList cycleDetails={cycleDetails} />
    </Wrapper>
  );
};

export default CycleDetailPage;

const Wrapper = styled.div`
  margin: 0 1.25rem;
`;

const TitleWrapper = styled(FlexBox)`
  margin-bottom: 2rem;
`;

const ChallengeDetailWrapper = styled(FlexBox)`
  line-height: 1rem;
  margin-bottom: 2rem;
`;
