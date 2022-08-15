import ShareIcon from 'assets/share_icon.svg';
import styled from 'styled-components';

const share = ({ text }: { text: string }) => {
  if (navigator.share) {
    navigator
      .share({
        title: document.title,
        text,
        url: document.location.href,
      })
      .then(() => console.log('Successful share'))
      .catch((error) => console.log('Error sharing', error));
  }
};

export const ShareButton = ({ text }: { text: string }) => {
  return (
    <IconWrapper onClick={() => share({ text })}>
      <ShareIcon />
    </IconWrapper>
  );
};

const IconWrapper = styled.div`
  cursor: pointer;
`;
