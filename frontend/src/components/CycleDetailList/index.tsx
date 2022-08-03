import { CycleDetailListProps } from './type';

import { FlexBox } from 'components/@shared/FlexBox';

import { CycleDetailItem } from 'components/CycleDetailItem';
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
    <FlexBox alignItems="center" flexDirection="column" gap="2rem">
      {cycleDetails.map((cycleDetail) => {
        return <CycleDetailItem key={cycleDetail.progressTime} {...cycleDetail} />;
      })}
    </FlexBox>
  );
};
