import { UseRankingItemProps } from './type';

import useThemeContext from 'hooks/useThemeContext';

const useRankingItem = ({ myRanking }: UseRankingItemProps) => {
  const themeContext = useThemeContext();

  let surfaceColor = themeContext.surface;
  let onSurfaceColor = themeContext.onSurface;
  let pointColor = themeContext.primary;
  if (myRanking) {
    surfaceColor = themeContext.primary;
    onSurfaceColor = themeContext.onPrimary;
    pointColor = themeContext.onPrimary;
  }

  return { surfaceColor, onSurfaceColor, pointColor };
};

export default useRankingItem;
