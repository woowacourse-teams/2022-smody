import { useContext } from 'react';
import styled, { css, ThemeContext } from 'styled-components';

import { FlexBox, Text, Button } from 'components';

export const VocPage = () => {
  const themeContext = useContext(ThemeContext);
  const handleLink = (url: string) => {
    window.open(url, '_blank');
  };

  return (
    <Wrapper>
      <Text color={themeContext.primary} size={24} fontWeight="bold">
        Smody 소개
      </Text>
      <Button
        onClick={() => handleLink('https://forms.gle/1nuk3EozEBzRtRFy9')}
        size="large"
      >
        에러 제보 및 개선 건의
      </Button>
      <Button
        onClick={() => handleLink('http://13.124.154.60:8080/docs/api.html')}
        size="large"
      >
        Smody API 문서
      </Button>
      <Button
        onClick={() =>
          handleLink('https://62c67b97ff406488a4595ad0-yussdwyrjw.chromatic.com/')
        }
        size="large"
      >
        Smody 스토리북
      </Button>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  gap: '1rem',
})`
  ${({ theme }) => css`
    margin: auto;
    border-radius: 20px;
    align-items: center;
    width: 100%;
    max-width: 600px;
    min-width: 366px;
  `}
`;
