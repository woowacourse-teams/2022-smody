import { RefObject, MutableRefObject, ReactElement } from 'react';

export type MentionableInputProps = {
  commentInputRef: RefObject<HTMLDivElement>;
  mentionedMemberIds: number[];
  setMentionedMemberIds: React.Dispatch<React.SetStateAction<number[]>>;
  setContent: (arg0: string) => void;
  editableElement: ReactElement;
};

export type useMentionableInputProps = Pick<
  MentionableInputProps,
  'commentInputRef' | 'mentionedMemberIds' | 'setMentionedMemberIds' | 'setContent'
>;

export type usePopoverProps = Pick<
  useMentionableInputProps,
  'commentInputRef' | 'mentionedMemberIds' | 'setMentionedMemberIds'
> & {
  lastMentionSymbolPosition: MutableRefObject<number>;
  filterValue: string;
  setFilterValue: (arg0: string) => void;
  isFilterValueInitiated: MutableRefObject<boolean>;
};
