import { useSearchPage } from 'pages/SearchPage/useSearchPage';

import { FlexBox, ChallengeItem, LoadingSpinner, ChallengeList } from 'components';
import { ChallengeInfo } from 'components/ChallengeList/type';

const SearchPage = () => {
  const { rootRef, targetRef, isFetching, data, refetch } = useSearchPage();

  if (typeof data === 'undefined') {
    return <LoadingSpinner />;
  }

  return (
    <FlexBox as="ul" ref={rootRef} flexDirection="column" gap="27px">
      {data.pages.map((page, pageIndex) => {
        if (typeof page === 'undefined' || typeof page.data === 'undefined') {
          return [];
        }

        return page.data.map((challengeInfo: ChallengeInfo, challengeIndex: number) => (
          <li
            key={challengeInfo.challengeId}
            ref={
              pageIndex === data.pages.length - 1 &&
              challengeIndex === page.data.length - 1
                ? targetRef
                : undefined
            }
          >
            <ChallengeItem {...challengeInfo} challengeListRefetch={refetch} />
          </li>
        ));
      })}
      {isFetching && <LoadingSpinner />}
    </FlexBox>
  );
};

export default SearchPage;
