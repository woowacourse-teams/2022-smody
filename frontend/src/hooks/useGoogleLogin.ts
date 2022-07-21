import { useGetLinkGoogle } from 'apis';

export const useGoogleLogin = () => {
  const { refetch: getLinkGoogle } = useGetLinkGoogle({
    enabled: false,
    onSuccess: ({ data: redirectionUrl }) => {
      window.location.href = redirectionUrl;
    },
  });

  return getLinkGoogle;
};
