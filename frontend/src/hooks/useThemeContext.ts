import { useContext } from 'react';
import { ThemeContext } from 'styled-components';

const useThemeContext = () => {
  const themeContext = useContext(ThemeContext);

  return themeContext;
};

export default useThemeContext;
