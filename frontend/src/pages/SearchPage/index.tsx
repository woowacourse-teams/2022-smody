import { useSearchPage } from 'pages/SearchPage/useSearchPage';

import { FlexBox, LoadingSpinner, ChallengeList } from 'components';

const SearchPage = () => {
  const { rootRef, targetRef, isFetching, challengeListData, challengeListRefetch } =
    useSearchPage();

  return (
    <FlexBox ref={rootRef} flexDirection="column">
      <ChallengeList
        targetRef={targetRef}
        challengeListData={challengeListData}
        challengeListRefetch={challengeListRefetch}
      />
      {isFetching && <LoadingSpinner />}
    </FlexBox>
  );
};

export default SearchPage;
