import { InnerWrapperProps, CommentInputProps, WriteButtonProps } from './type';
import useCommentInput from './useCommentInput';
import styled, { css } from 'styled-components';

import { FlexBox, ValidationMessage, MembersPopover } from 'components';

export const CommentInput = ({
  selectedCommentId,
  editMode,
  turnOffEditMode,
}: CommentInputProps) => {
  const {
    commentInputRef,
    content,
    isVisibleWriteButton,
    isCommentError,
    commentErrorMessage,
    isLoadingPostComment,
    isLoadingPatchComment,
    handleClickWrite,
    isLoadingPostMentionNotifications,
    isPopoverOpen,
    handleClosePopover,
    membersData,
    hasNextMembersPage,
    fetchNextMembersPage,
    selectMember,
  } = useCommentInput({ selectedCommentId, editMode, turnOffEditMode });

  return (
    <Wrapper flexDirection="column" alignItems="center">
      <InnerWrapper alignItems="center" isShowLengthWarning={isCommentError}>
        <CommentInputElement contentEditable={true} ref={commentInputRef} />
        <WriteButton
          disabled={
            !isVisibleWriteButton ||
            isLoadingPostComment ||
            isLoadingPatchComment ||
            isCommentError ||
            isLoadingPostMentionNotifications
          }
          isVisible={isVisibleWriteButton}
          onClick={handleClickWrite}
        >
          {editMode.isEditMode ? '수정' : '작성'}
        </WriteButton>
      </InnerWrapper>
      {isCommentError && (
        <ValidationMessageWrapper>
          <ValidationMessage
            isValidated={false}
            value={content}
            message={commentErrorMessage}
          />
        </ValidationMessageWrapper>
      )}
      {isPopoverOpen && (
        <MembersPopover
          handleClosePopover={handleClosePopover}
          membersData={membersData}
          hasNextMembersPage={hasNextMembersPage}
          fetchNextMembersPage={fetchNextMembersPage}
          selectMember={selectMember}
        />
      )}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  ${({ theme }) => css`
    position: fixed;
    bottom: 3.625rem;
    width: 100%;
    margin-top: auto;
    padding: 0.813rem 1.25rem;
    border-top: 2px solid ${theme.secondary};
    background-color: ${theme.background};
  `}
`;

const InnerWrapper = styled(FlexBox)<InnerWrapperProps>`
  ${({ theme, isShowLengthWarning }) => css`
    width: 100%;
    padding: 0.563rem 1.313rem;
    background-color: ${theme.input};
    border: ${isShowLengthWarning ? `solid 2px ${theme.error}` : 'none'};
    border-radius: 20px;
  `}
`;

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

const WriteButton = styled.button<WriteButtonProps>`
  ${({ theme, isVisible }) => css`
    min-width: 50px;
    height: 20px;
    font-size: 0.83rem;
    letter-spacing: 0.6px;
    font-weight: bold;
    color: ${theme.primary};
    visibility: ${isVisible ? 'visible' : 'hidden'};

    &:disabled {
      color: ${theme.disabled};
    }
  `}
`;

const ValidationMessageWrapper = styled.div`
  align-self: flex-start;
`;
