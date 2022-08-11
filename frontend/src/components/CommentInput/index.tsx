import useCommentInput from './useCommentInput';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox } from 'components';

export const CommentInput = () => {
  const {
    commentInputRef,
    content,
    isWriteButtonDisabled,
    handleChangeInput,
    handleClickWrite,
  } = useCommentInput();

  return (
    <Wrapper alignItems="center">
      <InnerWrapper alignItems="center">
        <CommentInputElement
          ref={commentInputRef}
          value={content}
          placeholder="다른 사용자와 소통해보세요!"
          rows={1}
          maxLength={254}
          onChange={handleChangeInput}
        />
        <WriteButton disabled={isWriteButtonDisabled} onClick={handleClickWrite}>
          작성
        </WriteButton>
      </InnerWrapper>
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

const InnerWrapper = styled(FlexBox)`
  ${({ theme }) => css`
    width: 100%;
    padding: 0.563rem 1.313rem;
    background-color: ${theme.input};
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
    font-family: 'Source Sans Pro', sans-serif;
    color: ${theme.onInput};
  `}
`;

const WriteButton = styled.button`
  ${({ theme, disabled }) => css`
    height: 20px;
    padding: 0 0.625rem;
    font-size: 0.75rem;
    font-weight: bold;
    color: ${theme.primary};
    visibility: ${disabled ? 'hidden' : 'visible'};
  `}
`;
