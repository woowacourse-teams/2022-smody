import { ChallengeIconProps } from './type';
import projectIcons from 'assets/project_icons.png';
import styled, { css } from 'styled-components';

import { FlexBox } from 'components';

import { EVENT_CHALLENGES, EVENT_CHALLENGE_ID_LIST } from 'constants/event';
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

export const ChallengeIcon = ({
  size,
  bgColor = '#fff',
  emojiIndex = 0,
  challengeId,
}: ChallengeIconProps) => {
  if (
    typeof challengeId !== 'undefined' &&
    EVENT_CHALLENGE_ID_LIST.includes(challengeId)
  ) {
    return (
      <ProjectIcon size={size} justifyContent="center" alignItems="center">
        <Icon className={EVENT_CHALLENGES[challengeId].name} />
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
    .levellog,
    .moamoa,
    .momo,
    .morak,
    .naepyeon,
    .smody,
    .sokdaksokdak,
    .teatime,
    .ternoko,
    .thankoo {
      display: inline-block;
      background: url(${projectIcons}) no-repeat;
      overflow: hidden;

      width: 200px;
      height: 200px;
      border-radius: 50%;
      zoom: ${IMG_SIZES[size]};
    }
    .f12 {
      background-position: -0px -0px;
      background-color: rgb(254, 232, 232);
    }
    .checkmate {
      background-position: -200px -0px;
      background-color: #fcead4;
    }
    .dallok {
      background-position: -400px -0px;
      background-color: #fff5db;
    }
    .ducks {
      background-position: -600px -0px;
      background-color: #ffffff;
    }
    .gongcheck {
      background-position: -0px -200px;
      background-color: #e1f3fe;
    }
    .gongseek {
      background-position: -200px -200px;
      background-color: #f6f1ff;
    }
    .jupjup {
      background-position: -400px -200px;
      background-color: #ffe6c1;
    }
    .kkogkkog {
      background-position: -600px -200px;
      background-color: #fff5ef;
    }
    .levellog {
      background-position: -0px -400px;
      background-color: #ffffff;
    }
    .moamoa {
      background-position: -200px -400px;
      background-color: #d0ccff;
    }
    .momo {
      background-position: -400px -400px;
      background-color: #ffffff;
    }
    .morak {
      background-position: -600px -400px;
      background-color: #f6f1ff;
    }
    .naepyeon {
      background-position: -0px -600px;
      background-color: #98daff;
    }
    .smody {
      background-position: -200px -600px;
      background-color: #ffffff;
    }
    .sokdaksokdak {
      background-position: -400px -600px;
      background-color: #ceffe6;
    }
    .teatime {
      background-position: -600px -600px;
      background-color: #ffffff;
    }
    .ternoko {
      background-position: -0px -800px;
      background-color: rgb(255, 232, 232);
    }
    .thankoo {
      background-position: -200px -800px;
      background-color: #ffffff;
    }
  `}
`;
