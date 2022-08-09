import { useSearchPage } from 'pages/SearchPage/useSearchPage';

import { FlexBox, LoadingSpinner, ChallengeList } from 'components';

const SearchPage = () => {
  const { rootRef, targetRef, isFetching, challengeListData } = useSearchPage();

  return (
    <FlexBox ref={rootRef} flexDirection="column">
      <ChallengeList targetRef={targetRef} challengeListData={challengeListData} />
      {isFetching && <LoadingSpinner />}
    </FlexBox>
  );
};

export default SearchPage;
