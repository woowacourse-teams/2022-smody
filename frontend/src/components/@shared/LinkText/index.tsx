import { PropsWithChildren } from 'react';
import { Link } from 'react-router-dom';

import { LinkTextProps } from 'components/@shared/LinkText/type';
import { Text } from 'components/@shared/Text';

export const LinkText = ({
  children,
  to,
  color,
  size,
  fontWeight,
}: PropsWithChildren<LinkTextProps>) => {
  return (
    <Link to={to}>
      <Text color={color} size={size} fontWeight={fontWeight}>
        {children}
      </Text>
    </Link>
  );
};
