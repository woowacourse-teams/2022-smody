import { ReactNode } from 'react';

import { FixedButton } from 'components/FixedButton';

export default {
  title: 'components/FixedButton',
  component: FixedButton,
};

type FixedButtonProps = {
  children: ReactNode;
};

export const DefaultFixedButton = ({ children }: FixedButtonProps) => (
  <FixedButton>{children}</FixedButton>
);

DefaultFixedButton.args = {
  children: '도전하기',
};
