import { UseFeedProps } from './type';
import { MouseEventHandler } from 'react';
import { useNavigate } from 'react-router-dom';
import { parseTime } from 'utils';

import { CLIENT_PATH } from 'constants/path';

const useFeedItem = ({
  challengeId,
  cycleDetailId,
  progressTime,
  challengeName,
  isShowBriefChallengeName,
}: UseFeedProps) => {
  const navigate = useNavigate();
  const { year, month, date, hours, minutes } = parseTime(progressTime);

  const renderedChallengeName =
    isShowBriefChallengeName && challengeName.length > 9
      ? `${challengeName.substring(0, 9)}...`
      : challengeName;

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
    renderedChallengeName,
    handleClickFeed,
    handleClickChallengeName,
  };
};

export default useFeedItem;
