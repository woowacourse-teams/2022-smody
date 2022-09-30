import { MembersPopoverProps } from './type';
import { useState } from 'react';

import { Popover, InfiniteScroll, LoadingSpinner } from 'components';

export const MembersPopover = ({
  fetchNextMembersPage,
  hasNextMembersPage,
  handleClosePopover,
  membersData,
}: MembersPopoverProps) => {
  return (
    <Popover handleClosePopover={handleClosePopover}>
      <InfiniteScroll
        loadMore={fetchNextMembersPage}
        hasMore={hasNextMembersPage}
        loader={<LoadingSpinner />}
      >
        {membersData?.pages.map((page) =>
          page?.data.map((member) => <h2 key={member.memberId}>{member.nickname}</h2>),
        )}
      </InfiniteScroll>
    </Popover>
  );
};

// <InfiniteScroll
//   loadMore={fetchNextPageMyCycles}
//   hasMore={hasNextPageMyCycles}
//   isFetching={isFetchingMyCycles}
//   loader={<LoadingSpinner />}
// >
//   <FlexBox as="ul" flexDirection="column" alignItems="center" gap="1rem">
//     {myCyclesInfiniteData?.pages.map((page) =>
//       page?.data.map((cycle) => (
//         <RecordList key={cycle.cycleId}>
//           <Record {...cycle} />
//         </RecordList>
//       )),
//     )}
//   </FlexBox>
// </InfiniteScroll>
