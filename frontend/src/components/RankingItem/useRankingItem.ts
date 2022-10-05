import { UseRankingItemProps } from './type';
import { useRecoilValue } from 'recoil';
import { myMemberIdState } from 'recoil/ranking/atom';

import useThemeContext from 'hooks/useThemeContext';

const useRankingItem = ({ memberId }: UseRankingItemProps) => {
  const themeContext = useThemeContext();
  const myMemberId = useRecoilValue(myMemberIdState);

  let surfaceColor = themeContext.surface;
  let onSurfaceColor = themeContext.onSurface;
  let pointColor = themeContext.primary;
  if (myMemberId === memberId) {
    surfaceColor = themeContext.primary;
    onSurfaceColor = themeContext.onPrimary;
    pointColor = themeContext.onPrimary;
  }

  return { surfaceColor, onSurfaceColor, pointColor };
};

export default useRankingItem;
