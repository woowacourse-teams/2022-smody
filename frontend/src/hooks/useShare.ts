import useSnackBar from 'hooks/useSnackBar';

export interface ShareLinkProps {
  text: string;
}

const useShare = () => {
  const renderSnackBar = useSnackBar();

  const shareLink = ({ text }: ShareLinkProps) => {
    if (navigator.share) {
      navigator.share({
        title: document.title,
        text,
        url: document.location.href,
      });
    } else {
      renderSnackBar({
        message: '공유 기능은 모바일에서 가능합니다',
        status: 'ERROR',
      });
    }
  };

  return { shareLink };
};

export default useShare;
