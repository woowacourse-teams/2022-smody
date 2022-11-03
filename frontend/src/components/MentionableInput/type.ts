import { RefObject, MutableRefObject } from 'react';

export type useMentionableInputProps = {
  commentInputRef: RefObject<HTMLDivElement>;
  mentionedMemberIds: number[];
  setMentionedMemberIds: React.Dispatch<React.SetStateAction<number[]>>;
  setContent: (arg0: string) => void;
};

export type usePopoverProps = Pick<
  useMentionableInputProps,
  'commentInputRef' | 'mentionedMemberIds' | 'setMentionedMemberIds'
> & {
  lastMentionSymbolPosition: MutableRefObject<number>;
  filterValue: string;
  setFilterValue: (arg0: string) => void;
  isFilterValueInitiated: MutableRefObject<boolean>;
};
