import ShareIcon from 'assets/share_icon.svg';
import styled from 'styled-components';

import useSnackBar from 'hooks/useSnackBar';

export const ShareButton = ({ text }: { text: string }) => {
  const renderSnackBar = useSnackBar();

  const share = ({ text }: { text: string }) => {
    if (navigator.share) {
      navigator.share({
        title: document.title,
        text,
        url: document.location.href,
      });
    } else {
      renderSnackBar({
        message: '공유 기능은 안드로이드 앱에서 가능합니다',
        status: 'ERROR',
      });
    }
  };
  return (
    <IconWrapper onClick={() => share({ text })}>
      <ShareIcon />
    </IconWrapper>
  );
};

const IconWrapper = styled.div`
  cursor: pointer;
`;
