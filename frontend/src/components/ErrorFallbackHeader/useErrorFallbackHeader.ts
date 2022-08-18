import { useGetLinkGoogle } from 'apis';
import { useRecoilValue } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

export const useErrorFallbackHeader = () => {
  const isLogin = useRecoilValue(isLoginState);

  const { refetch: redirectGoogleLoginLink } = useGetLinkGoogle({
    onSuccess: ({ data: redirectionUrl }) => {
      window.location.href = redirectionUrl;
    },
  });

  const handleLoginButton = () => {
    redirectGoogleLoginLink();
  };

  return {
    isLogin,
    handleLoginButton,
  };
};
