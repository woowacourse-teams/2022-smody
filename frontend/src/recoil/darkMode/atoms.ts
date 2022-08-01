import { atom } from 'recoil';
import { detectDarkMode } from 'utils';

const isDarkMode = detectDarkMode();

export const isDarkState = atom({
  key: 'isDarkState',
  default: isDarkMode,
});
