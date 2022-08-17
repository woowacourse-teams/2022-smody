import { HandleNavigateFeedDetailProps } from './type';
import { useNavigate } from 'react-router-dom';

import { CLIENT_PATH } from 'constants/path';

export const useRecordItem = () => {
  const navigate = useNavigate();
  const handleNavigateFeedDetail = ({ cycleDetailId }: HandleNavigateFeedDetailProps) => {
    navigate(`${CLIENT_PATH.FEED_DETAIL}/${cycleDetailId}`);
  };

  return handleNavigateFeedDetail;
};
