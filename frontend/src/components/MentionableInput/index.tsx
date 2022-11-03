import { MentionableInputProps } from './type';
import useMentionableInput from './useMentionableInput';
import { cloneElement } from 'react';

import { MembersPopover } from 'components/MentionableInput/MembersPopover';

export const MentionableInput = ({
  editableElementRef,
  setContent,
  mentionedMemberIds,
  setMentionedMemberIds,
  editableElement,
}: MentionableInputProps) => {
  const {
    isPopoverOpen,
    handleClosePopover,
    membersData,
    hasNextMembersPage,
    fetchNextMembersPage,
    selectMember,
    handleKeydownCommentInput,
    handleInputCommentInput,
  } = useMentionableInput({
    editableElementRef,
    setContent,
    mentionedMemberIds,
    setMentionedMemberIds,
  });

  return (
    <>
      {cloneElement(editableElement, {
        ref: editableElementRef,
        contentEditable: true,
        onKeyDown: handleKeydownCommentInput,
        onInput: handleInputCommentInput,
        style: {
          whiteSpace: 'pre-wrap',
          wordWrap: 'break-word',
          wordBreak: 'break-all',
        },
      })}
      {isPopoverOpen && (
        <MembersPopover
          handleClosePopover={handleClosePopover}
          membersData={membersData}
          hasNextMembersPage={hasNextMembersPage}
          fetchNextMembersPage={fetchNextMembersPage}
          selectMember={selectMember}
        />
      )}
    </>
  );
};
