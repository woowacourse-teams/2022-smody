import { useRecoilState } from 'recoil';
import { isDarkState } from 'recoil/darkMode/atoms';

export const useHeader = () => {
  const [isDark, setIsDark] = useRecoilState(isDarkState);

  const handleDarkToggle = () => {
    localStorage.setItem('isDark', JSON.stringify(!isDark));
    setIsDark((prev) => !prev);
  };

  return {
    isDark,
    handleDarkToggle,
  };
};
