import { InnerWrapperProps, WriteButtonProps } from './type';
import useCommentInput from './useCommentInput';
import styled, { css } from 'styled-components';

import { FlexBox, ValidationMessage } from 'components';

import { MAX_TEXTAREA_LENGTH } from 'constants/domain';

export const CommentInput = () => {
  const {
    commentInputRef,
    content,
    isVisibleWriteButton,
    isShowLengthWarning,
    isLoadingPostComment,
    handleChangeInput,
    handleClickWrite,
  } = useCommentInput();

  return (
    <Wrapper flexDirection="column" alignItems="center">
      <InnerWrapper alignItems="center" isShowLengthWarning={isShowLengthWarning}>
        <CommentInputElement
          ref={commentInputRef}
          value={content}
          placeholder="다른 사용자와 소통해보세요!"
          rows={1}
          maxLength={MAX_TEXTAREA_LENGTH - 1}
          onChange={handleChangeInput}
        />
        <WriteButton
          disabled={!isVisibleWriteButton || isLoadingPostComment}
          isVisible={isVisibleWriteButton}
          onClick={handleClickWrite}
        >
          작성
        </WriteButton>
      </InnerWrapper>
      {isShowLengthWarning && (
        <ValidationMessageWrapper>
          <ValidationMessage
            isValidated={false}
            value={content}
            message={'댓글은 최대 255자까지 입력할 수 있습니다.'}
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

const CommentInputElement = styled.textarea`
  ${({ theme }) => css`
    flex-grow: 1;
    max-height: 60px;
    outline: none;
    background-color: ${theme.input};
    border: none;
    resize: none;
    font-size: 1rem;
    font-weight: normal;
    color: ${theme.onInput};
  `}
`;

const WriteButton = styled.button<WriteButtonProps>`
  ${({ theme, isVisible }) => css`
    height: 20px;
    padding: 0 0.625rem;
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
