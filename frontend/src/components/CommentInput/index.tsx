import { MentionableInput } from '../MentionableInput';
import { InnerWrapperProps, CommentInputProps, WriteButtonProps } from './type';
import useCommentInput from './useCommentInput';
import { useState, useRef } from 'react';
import styled, { css } from 'styled-components';

import { FlexBox, ValidationMessage, Tooltip } from 'components';

export const CommentInput = ({
  selectedCommentId,
  editMode,
  turnOffEditMode,
}: CommentInputProps) => {
  const commentInputRef = useRef<HTMLDivElement>(null);
  const [mentionedMemberIds, setMentionedMemberIds] = useState<Array<number>>([]);
  const [content, setContent] = useState('');

  const {
    isVisibleWriteButton,
    isCommentError,
    commentErrorMessage,
    isLoadingPostComment,
    isLoadingPatchComment,
    handleClickWrite,
    isLoadingPostMentionNotifications,
  } = useCommentInput({
    selectedCommentId,
    editMode,
    turnOffEditMode,
    commentInputRef,
    mentionedMemberIds,
    setMentionedMemberIds,
    setContent,
    content,
  });

  return (
    <Wrapper flexDirection="column">
      <TopRowWrapper alignItems="center" gap="0.5rem">
        <InnerWrapper alignItems="center" isShowLengthWarning={isCommentError}>
          <MentionableInput
            editableElementRef={commentInputRef}
            editableElement={<CommentInputElement />}
            setContent={setContent}
            mentionedMemberIds={mentionedMemberIds}
            setMentionedMemberIds={setMentionedMemberIds}
          />
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
        <Tooltip
          ariaLabel="댓글 툴팁"
          xPosition="left"
          yPosition="top"
          yDelta="-100px"
          line={2.3}
        >
          댓글을 통해 다른 사람에게 멘션 알림을 보내려면 @와 닉네임을 붙여서 입력한 후
          선택하세요.
        </Tooltip>
      </TopRowWrapper>
      {isCommentError && (
        <ValidationMessageWrapper>
          <ValidationMessage
            isValidated={false}
            value={content}
            message={commentErrorMessage}
          />
        </ValidationMessageWrapper>
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
    flex-grow: 1;
  `}
`;

const TopRowWrapper = styled(FlexBox)`
  width: 100%;
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
