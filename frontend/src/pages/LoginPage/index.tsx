import { useContext } from 'react';
import { ThemeContext } from 'styled-components';

import { Text } from 'components';

export const LoginPage = () => {
  const themeContext = useContext(ThemeContext);

  return (
    <Text size={24} color={themeContext.onBackground} fontWeight="bold">
      LoginPage
    </Text>
  );
};
