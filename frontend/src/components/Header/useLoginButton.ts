import useAuth from 'hooks/useAuth';

export const useLoginButton = () => {
  const { redirectGoogleLoginLink } = useAuth();

  const handleClickLoginButton = () => {
    redirectGoogleLoginLink();
  };

  return {
    handleClickLoginButton,
  };
};
