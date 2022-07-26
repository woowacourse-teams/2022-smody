import { useGetLinkGoogle } from 'apis';

import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

export const useGoogleLogin = () => {
  const renderSnackBar = useSnackBar();
  const { refetch: getLinkGoogle } = useGetLinkGoogle({
    enabled: false,
    onSuccess: ({ data: redirectionUrl }) => {
      window.location.href = redirectionUrl;
    },
    onError: () => {
      renderSnackBar({
        message: '간편 로그인에 문제가 발생했습니다',
        status: 'ERROR',
        linkText: '문의하기',
        linkTo: CLIENT_PATH.VOC,
      });
    },
  });

  return () => getLinkGoogle();
};