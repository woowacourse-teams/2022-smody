import ShareIcon from 'assets/share_icon.svg';
import styled from 'styled-components';

import useShare from 'hooks/useShare';
import { ShareLinkProps } from 'hooks/useShare';

export const ShareButton = ({ text }: ShareLinkProps) => {
  const { shareLink } = useShare();

  return (
    <IconWrapper onClick={() => shareLink({ text })}>
      <ShareIcon />
    </IconWrapper>
  );
};

const IconWrapper = styled.div`
  cursor: pointer;
`;
