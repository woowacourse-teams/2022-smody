import { useGetAllChallenges } from 'apis';

import useIntersect from 'hooks/useIntersect';
import useSnackBar from 'hooks/useSnackBar';

import { CLIENT_PATH } from 'constants/path';

export const useChallengeList = () => {
  const renderSnackBar = useSnackBar();
  const { isFetching, data, refetch, hasNextPage, fetchNextPage } = useGetAllChallenges({
    refetchOnWindowFocus: false,
    onError: () => {
      renderSnackBar({
        message: '챌린지 목록 조회 시 에러가 발생했습니다.',
        status: 'ERROR',
        linkText: '문의하기',
        linkTo: CLIENT_PATH.VOC,
      });
    },
  });

  const handleIntersect = () => {
    if (hasNextPage) {
      fetchNextPage();
    }
  };

  const { targetRef, rootRef } = useIntersect<HTMLUListElement, HTMLLIElement>(
    handleIntersect,
  );

  return { isFetching, refetch, data, rootRef, targetRef };
};
