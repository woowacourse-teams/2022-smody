import { useGetCycleById } from 'apis';
import { MdArrowBackIosNew } from 'react-icons/md';
import { useNavigate, useParams } from 'react-router-dom';
import styled from 'styled-components';
import { getEmoji } from 'utils/emoji';

import useThemeContext from 'hooks/useThemeContext';

import {
  LoadingSpinner,
  Text,
  ThumbnailWrapper,
  CycleDetailList,
  FlexBox,
} from 'components';

import { makeCursorPointer } from 'constants/style';

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

  const { challengeId, challengeName, cycleDetails } = data.data;

  return (
    <Wrapper>
      <TitleWrapper flexDirection="row" justifyContent="space-between">
        <MdArrowBackIosNew
          size={20}
          onClick={backToPreviousPage}
          style={makeCursorPointer}
        />
        <Text fontWeight="bold" size={20} color={themeContext.onBackground}>
          인증기록 보기
        </Text>
        <div />
      </TitleWrapper>
      <ChallengeDetailWrapper
        flexDirection="row"
        justifyContent="space-between"
        alignItems="center"
        gap="1rem"
      >
        <Text size={20} fontWeight="bold" color={themeContext.primary}>
          {challengeName}
        </Text>
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
