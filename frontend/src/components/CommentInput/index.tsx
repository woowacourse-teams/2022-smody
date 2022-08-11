import { ChangeEventHandler } from 'react';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';

const DEFAULT_INPUT_HEIGHT = '20px';

export const CommentInput = () => {
  const themeContext = useThemeContext();

  const handleChangeInput: ChangeEventHandler<HTMLTextAreaElement> = (event) => {
    // 입력 값을 지웠을 때, 댓글 입력창의 높이를 줄이기 위한 코드
    event.target.style.height = DEFAULT_INPUT_HEIGHT;
    event.target.style.height = event.target.scrollHeight + 'px';
  };

  return (
    <Wrapper alignItems="center">
      <InnerWrapper alignItems="center">
        <CommentInputElement
          placeholder="다른 사용자와 소통해보세요!"
          rows={1}
          onChange={handleChangeInput}
        />
        <Text
          style={{ padding: '0 0.625rem' }}
          size={12}
          color={themeContext.primary}
          fontWeight="bold"
        >
          작성
        </Text>
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
    overflow-y: hidden;
  `}
`;
