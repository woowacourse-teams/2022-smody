import { MembersPopoverProps } from './type';

import { Popover, InfiniteScroll, LoadingSpinner } from 'components';

export const MembersPopover = ({
  fetchNextMembersPage,
  hasNextMembersPage,
  handleClosePopover,
  membersData,
  selectMember,
}: MembersPopoverProps) => {
  return (
    <Popover handleClosePopover={handleClosePopover}>
      <InfiniteScroll
        loadMore={fetchNextMembersPage}
        hasMore={hasNextMembersPage}
        loader={<LoadingSpinner />}
      >
        {membersData?.pages.map((page) =>
          page?.data.map((member) => (
            <h2
              key={member.memberId}
              onClick={() => {
                selectMember(member.memberId);
                handleClosePopover();
              }}
            >
              {member.nickname}
            </h2>
          )),
        )}
      </InfiniteScroll>
    </Popover>
  );
};
