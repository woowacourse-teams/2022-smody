import { AxiosResponse } from 'axios';
import { RefObject, MutableRefObject, ReactElement } from 'react';
import { InfiniteData } from 'react-query';
import { Member } from 'types/member';

export type MentionableInputProps = {
  editableElementRef: RefObject<HTMLDivElement>;
  mentionedMemberIds: number[];
  setMentionedMemberIds: React.Dispatch<React.SetStateAction<number[]>>;
  setContent: (arg0: string) => void;
  editableElement: ReactElement;
};

export type useMentionableInputProps = Pick<
  MentionableInputProps,
  'editableElementRef' | 'mentionedMemberIds' | 'setMentionedMemberIds' | 'setContent'
>;

export type usePopoverProps = Pick<
  useMentionableInputProps,
  'editableElementRef' | 'mentionedMemberIds' | 'setMentionedMemberIds'
> & {
  lastMentionSymbolPosition: MutableRefObject<number>;
  filterValue: string;
  setFilterValue: (arg0: string) => void;
  isFilterValueInitiated: MutableRefObject<boolean>;
};

export type MembersPopoverProps = {
  fetchNextMembersPage: () => void;
  hasNextMembersPage: boolean | undefined;
  handleClosePopover: () => void;
  membersData:
    | InfiniteData<
        AxiosResponse<Pick<Member, 'memberId' | 'nickname' | 'picture'>[], any>
      >
    | undefined;
  selectMember: (memberId: number, nickname: string) => void;
};

export type MemberItemProps = Pick<Member, 'nickname' | 'picture'>;
