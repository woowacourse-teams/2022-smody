import VisibilityIconSvg from 'assets/visibility_icon.svg';

import { VisibilityIconProps } from 'components/VisibilityIcon/type';

export const VisibilityIcon = ({ type, onClick }: VisibilityIconProps) => {
  if (type !== 'password') {
    return null;
  }

  return (
    <div onClick={onClick}>
      <VisibilityIconSvg />
    </div>
  );
};
