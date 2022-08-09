import { UseFeedProps } from './type';
import { MouseEventHandler } from 'react';
import { useNavigate } from 'react-router-dom';
import { parseTime } from 'utils';

import { CLIENT_PATH } from 'constants/path';

const useFeed = ({ challengeId, progressTime }: UseFeedProps) => {
  const navigate = useNavigate();
  const { year, month, date, hours, minutes } = parseTime(progressTime);

  const handleClickFeed: MouseEventHandler<HTMLDivElement> = () => {
    // TODO: 피드 상세보기 페이지로 이동하는 기능 구현
  };

  const handleClickChallengeName: MouseEventHandler<HTMLParagraphElement> = (event) => {
    event.stopPropagation();
    navigate(`${CLIENT_PATH.CHALLENGE_DETAIL}/${challengeId}`);
  };

  return {
    year,
    month,
    date,
    hours,
    minutes,
    handleClickFeed,
    handleClickChallengeName,
  };
};

export default useFeed;
