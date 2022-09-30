import { MemberItem } from './MemberItem';
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
        <ul style={{ listStyle: 'none' }}>
          {membersData?.pages.map((page) =>
            page?.data.map((member) => (
              <li
                key={member.memberId}
                onClick={() => {
                  selectMember(member.memberId);
                  handleClosePopover();
                }}
              >
                <MemberItem nickname={member.nickname} picture={member.picture} />
              </li>
            )),
          )}
        </ul>
      </InfiniteScroll>
    </Popover>
  );
};
