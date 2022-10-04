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
    isMaxLengthOver,
    isLoadingPostComment,
    isLoadingPatchComment,
    handleClickWrite,
    membersData,
    hasNextMembersPage,
    fetchNextMembersPage,
    isPopoverOpen,
    handleClosePopover,
    selectMember,
    isLoadingPostMentionNotifications,
  } = useCommentInput({ selectedCommentId, editMode, turnOffEditMode });

  return (
    <Wrapper flexDirection="column" alignItems="center">
      <InnerWrapper alignItems="center" isShowLengthWarning={isMaxLengthOver}>
        <CommentInputElement contentEditable={true} ref={commentInputRef} />
        <WriteButton
          disabled={
            !isVisibleWriteButton ||
            isLoadingPostComment ||
            isLoadingPatchComment ||
            isMaxLengthOver ||
            isLoadingPostMentionNotifications
          }
          isVisible={isVisibleWriteButton}
          onClick={handleClickWrite}
        >
          {editMode.isEditMode ? '수정' : '작성'}
        </WriteButton>
      </InnerWrapper>
      {isMaxLengthOver && (
        <ValidationMessageWrapper>
          <ValidationMessage
            isValidated={false}
            value={content}
            message={'댓글은 최대 255자까지 입력할 수 있습니다.'}
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
  `}
`;

const WriteButton = styled.button<WriteButtonProps>`
  ${({ theme, isVisible }) => css`
    min-width: 40px;
    height: 20px;
    font-size: 0.75rem;
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
