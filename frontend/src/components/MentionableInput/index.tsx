import { MentionableInputProps } from './type';
import useMentionableInput from './useMentionableInput';
import { cloneElement } from 'react';
import styled, { css } from 'styled-components';

import { MembersPopover } from 'components/MembersPopover';

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

const CommentInputElement = styled.div`
  ${({ theme }) => css`
    flex-grow: 1;
    max-height: 60px;
    outline: none;
    background-color: ${theme.input};
    border: none;
    resize: none;
    font-size: 1rem;
    color: ${theme.onInput};
    white-space: pre-wrap;
    word-wrap: break-word;
    word-break: break-all;
    overflow-y: scroll;
    // 스크롤바
    /* 스크롤바 설정*/
    &::-webkit-scrollbar {
      width: 4px;
    }

    /* 스크롤바 막대 설정*/
    &::-webkit-scrollbar-thumb {
      height: 17%;
      background-color: ${theme.primary};
      border-radius: 100px;
    }

    /* 스크롤바 뒷 배경 설정*/
    &::-webkit-scrollbar-track {
      background-color: ${theme.surface};
      border-radius: 100px;
    }
  `}
`;
