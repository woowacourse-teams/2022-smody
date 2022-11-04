import { CycleDetailListProps } from './type';
import styled from 'styled-components';

import { CycleDetailItem } from 'pages/CycleDetailPage/components/CycleDetailItem';

import { FlexBox } from 'components/@shared/FlexBox';

import { EmptyContent } from 'components/EmptyContent';

import { CLIENT_PATH } from 'constants/path';

export const CycleDetailList = ({ cycleDetails }: CycleDetailListProps) => {
  if (cycleDetails.length === 0) {
    return (
      <EmptyContent
        title="아직 인증하지 않았습니다 :)"
        description="현재 챌린지에 인증해보아요!"
        linkText="인증 페이지로 이동하기"
        linkTo={CLIENT_PATH.CERT}
      />
    );
  }

  return (
    <CycleDetailItemContainer alignItems="center" flexDirection="column" gap="2rem">
      {cycleDetails.map((cycleDetail) => (
        <CycleDetailItem key={cycleDetail.progressTime} {...cycleDetail} />
      ))}
    </CycleDetailItemContainer>
  );
};

const CycleDetailItemContainer = styled(FlexBox)`
  width: 100%;
`;
