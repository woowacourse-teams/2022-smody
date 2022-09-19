import { UseFeedProps } from './type';
import { MouseEventHandler } from 'react';
import { useNavigate } from 'react-router-dom';
import { parseTime } from 'utils';

import { CYCLE_SUCCESS_CRITERIA } from 'constants/domain';
import { CLIENT_PATH } from 'constants/path';

const useFeedItem = ({
  challengeId,
  cycleDetailId,
  progressTime,
  progressCount,
  challengeName,
  isShowBriefChallengeName,
}: UseFeedProps) => {
  const navigate = useNavigate();
  const { year, month, date, hours, minutes } = parseTime(progressTime);
  const isSuccess = progressCount == CYCLE_SUCCESS_CRITERIA;

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
    isSuccess,
    renderedChallengeName,
    handleClickFeed,
    handleClickChallengeName,
  };
};

export default useFeedItem;
