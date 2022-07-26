import { useGetMyInfo } from 'apis';
import { useRecoilState } from 'recoil';
import { isLoginState } from 'recoil/auth/atoms';

import { useManageAccessToken } from 'hooks/useManageAccessToken';

const useAuth = () => {
  const [isLogin, setIsLogin] = useRecoilState(isLoginState);
  const checkLogout = useManageAccessToken();

  const { isLoading } = useGetMyInfo({
    onSuccess: () => {
      setIsLogin(true);
    },
    onError: (error) => {
      checkLogout(error);
    },
  });

  return { isLogin, isLoading };
};
export default useAuth;
