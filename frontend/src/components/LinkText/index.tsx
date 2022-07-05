import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { ThemeContext } from 'styled-components';

import { Text } from 'components/@shared/Text';

import { LinkTextProps } from 'components/LinkText/type';

export const LinkText = ({ children, to, size, fontWeight }: LinkTextProps) => {
  const themeContext = useContext(ThemeContext);

  return (
    <Link to={to}>
      <Text color={themeContext.primary} size={size} fontWeight={fontWeight}>
        {children}
      </Text>
    </Link>
  );
};
