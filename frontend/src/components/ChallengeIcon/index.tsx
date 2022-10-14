import { ChallengeIconProps, ImgIconListIndexSignature } from './type';
import projectIcons from 'assets/project_icons.png';
import styled, { css } from 'styled-components';

import { FlexBox } from 'components';

import { emojiList } from 'constants/style';

const ratio = 0.7;

const IMG_SIZES = {
  small: 0.297 * ratio,
  medium: 0.456 * ratio,
  large: 0.6 * ratio,
};

const SIZES = {
  small: { width: '2.6rem', fontSize: '1.6rem' },
  medium: { width: '4rem', fontSize: '2.2rem' },
  large: { width: '7.5rem', fontSize: '4.375rem' },
};

const ImgIconList: ImgIconListIndexSignature = {
  1: 'f12',
  2: 'checkmate',
  3: 'dallok',
  4: 'ducks',
  5: 'gongcheck',
  6: 'gongseek',
  7: 'jupjup',
  8: 'kkogkkog',
  9: 'moamoa',
  10: 'momo',
  11: 'morak',
  12: 'smody',
  13: 'sokdaksokdak',
  14: 'teatime',
  15: 'ternoko',
};

export const ChallengeIcon = ({
  size,
  bgColor,
  emojiIndex,
  challengeId,
}: ChallengeIconProps) => {
  if (
    typeof challengeId !== 'undefined' &&
    Object.keys(ImgIconList).includes(String(challengeId))
  ) {
    return (
      <ProjectIcon size={size} justifyContent="center" alignItems="center">
        <Icon className={ImgIconList[challengeId]} />
      </ProjectIcon>
    );
  }

  return (
    <Wrapper size={size} bgColor={bgColor} justifyContent="center" alignItems="center">
      {emojiList[emojiIndex]}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)<Pick<ChallengeIconProps, 'size' | 'bgColor'>>`
  ${({ size, bgColor }) => css`
    min-width: ${SIZES[size].width};
    max-width: ${SIZES[size].width};
    min-height: ${SIZES[size].width};
    max-height: ${SIZES[size].width};
    border-radius: 50%;
    background-color: ${bgColor};
    font-size: ${SIZES[size].fontSize};
  `}
`;

const Icon = styled.div`
  background-size: cover;
`;

const ProjectIcon = styled(FlexBox)<Pick<ChallengeIconProps, 'size'>>`
  ${({ size }) => css`
    min-width: ${SIZES[size].width};
    max-width: ${SIZES[size].width};
    min-height: ${SIZES[size].width};
    max-height: ${SIZES[size].width};
    border-radius: 50%;

    .f12,
    .checkmate,
    .dallok,
    .ducks,
    .gongcheck,
    .gongseek,
    .jupjup,
    .kkogkkog,
    .moamoa,
    .momo,
    .morak,
    .smody,
    .sokdaksokdak,
    .teatime,
    .ternoko {
      display: inline-block;
      background: url(${projectIcons}) no-repeat;
      overflow: hidden;

      width: 200px;
      height: 200px;
      border-radius: 50%;
      zoom: ${IMG_SIZES[size]};
    }

    .f12 {
      background-position: 0 0;
      background-color: rgb(254, 232, 232);
    }
    .checkmate {
      background-position: -200px 0;
      background-color: #fcead4;
    }
    .dallok {
      background-position: -400px 0;
      background-color: #fff5db;
    }
    .ducks {
      background-position: 0 -200px;
      background-color: #ffffff;
    }
    .gongcheck {
      background-position: -200px -200px;
      background-color: #e1f3fe;
    }
    .gongseek {
      background-position: -400px -200px;
      background-color: #f6f1ff;
    }
    .jupjup {
      background-position: 0 -400px;
      background-color: #ffe6c1;
    }
    .kkogkkog {
      background-position: -200px -400px;
      background-color: #fff5ef;
    }
    .moamoa {
      background-position: -400px -400px;
      background-color: #d0ccff;
    }
    .momo {
      background-position: 0 -600px;
      background-color: #ffffff;
    }
    .morak {
      background-position: -200px -600px;
      background-color: #f6f1ff;
    }
    .smody {
      background-position: -400px -600px;
      background-color: #ffffff;
    }
    .sokdaksokdak {
      background-position: 0 -800px;
      background-color: #ceffe6;
    }
    .teatime {
      background-position: -200px -800px;
      background-color: #ffffff;
    }
    .ternoko {
      background-position: -400px -800px;
      background-color: rgb(255, 232, 232);
    }
  `}
`;
