import { AxiosResponse } from 'axios';
import { InfiniteData } from 'react-query';
import { Member } from 'types/member';

export type MembersPopoverProps = {
  fetchNextMembersPage: () => void;
  hasNextMembersPage: boolean | undefined;
  handleClosePopover: () => void;
  membersData:
    | InfiniteData<
        AxiosResponse<Pick<Member, 'memberId' | 'nickname' | 'picture'>[], any>
      >
    | undefined;
  selectMember: (memberId: number) => void;
};
