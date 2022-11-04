import { RefObject, MutableRefObject, ReactElement } from 'react';

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
