import { UseFeedProps } from './type';
import { MouseEventHandler } from 'react';
import { useNavigate } from 'react-router-dom';
import { parseTime } from 'utils';

import { CLIENT_PATH } from 'constants/path';

const useFeedItem = ({ challengeId, cycleDetailId, progressTime }: UseFeedProps) => {
  const navigate = useNavigate();
  const { year, month, date, hours, minutes } = parseTime(progressTime);

  const handleClickFeed: MouseEventHandler<HTMLDivElement> = () => {
    navigate(`${CLIENT_PATH.FEED_DETAIL}/${cycleDetailId}`);
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

export default useFeedItem;
