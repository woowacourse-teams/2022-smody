import { CycleImgProps } from './type';
import { CycleDetail } from 'commonType';
import styled, { css } from 'styled-components';
import { parseTime } from 'utils';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox } from 'components/@shared/FlexBox';
import { Text } from 'components/@shared/Text';

export const CycleDetailItem = ({
  progressImage,
  progressTime,
  description,
}: CycleDetail) => {
  const themeContext = useThemeContext();

  const { year, month, date, hours, minutes } = parseTime(progressTime);

  return (
    <CycleDetailWrapper>
      <FlexBox flexDirection="row">
        <CycleImg src={progressImage} />
        <DetailContents>
          <FlexBox flexDirection="column" gap="0.25rem">
            <Text size={14} color={themeContext.mainText}>
              {year}년 {month}월 {date}일 {hours}:{minutes}
            </Text>
            <MainText color={themeContext.onSurface}>{description}</MainText>
          </FlexBox>
        </DetailContents>
      </FlexBox>
    </CycleDetailWrapper>
  );
};

const CycleDetailWrapper = styled.div`
  ${({ theme }) => css`
    position: relative;
    max-height: 200px;
    border-radius: 20px;
    background-color: ${theme.surface};

    width: 90%;
    min-width: 390px;

    /* PC (해상도 1024px)*/
    @media all and (min-width: 1024px) {
      height: 300px;
    }

    /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
    @media all and (min-width: 768px) and (max-width: 1023px) {
      height: 200px;
    }

    /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
    @media all and (max-width: 767px) {
      height: 150px;
    }

    & > ${FlexBox} {
      position: absolute;
      width: 100%;
      height: 100%;
    }
  `}
`;

const CycleImg = styled.div<CycleImgProps>`
  ${({ src }) => css`
    width: 40%;
    height: 100%;
    border-radius: 20px 0 0 20px;
    background-image: url(${src});
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
  `}
`;

const DetailContents = styled.div`
  width: 60%;
  height: 90%;
  padding: 0.7rem 1rem 1rem 1rem;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
`;

const MainText = styled(Text)`
  white-space: pre-wrap;
  word-wrap: break-word;
  word-break: break-all;
`;
