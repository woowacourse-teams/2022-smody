import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, Button } from 'components';

const VocPage = () => {
  const themeContext = useThemeContext();
  const handleLink = (url: string) => {
    window.open(url, '_blank');
  };

  return (
    <Wrapper>
      <Text color={themeContext.primary} size={24} fontWeight="bold">
        Smody 소개
      </Text>

      <table>
        <caption>
          <Text
            color={themeContext.onBackground}
            size={20}
            fontWeight="bold"
            style={{ marginBottom: '1rem' }}
          >
            Frontend
          </Text>
        </caption>
        <thead>
          <tr>
            <th align="center">마르코</th>
            <th align="center">빅터</th>
            <th align="center">우연</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td align="center">
              <a
                target="_blank"
                href="https://avatars.githubusercontent.com/u/59413128?v=4"
                rel="noreferrer"
              >
                <img
                  src="https://avatars.githubusercontent.com/u/59413128?v=4"
                  alt="marco"
                  width="100"
                  height="100"
                  style={{ maxWidth: '100%' }}
                />
              </a>
            </td>
            <td align="center">
              <a
                target="_blank"
                href="https://avatars.githubusercontent.com/u/52148907?v=4"
                rel="noreferrer"
              >
                <img
                  src="https://avatars.githubusercontent.com/u/52148907?v=4"
                  alt="victor"
                  width="100"
                  height="100"
                  style={{ maxWidth: '100%' }}
                />
              </a>
            </td>
            <td align="center">
              <a
                target="_blank"
                href="https://avatars.githubusercontent.com/u/70249108?v=4"
                rel="noreferrer"
              >
                <img
                  src="https://avatars.githubusercontent.com/u/70249108?v=4"
                  alt="woo_yeon"
                  width="100"
                  height="100"
                  style={{ maxWidth: '100%' }}
                />
              </a>
            </td>
          </tr>
          <tr>
            <td align="center">
              <a href="https://github.com/wonsss">wonsss</a>
            </td>
            <td align="center">
              <a href="https://github.com/woose28">woose28</a>
            </td>
            <td align="center">
              <a href="https://github.com/ronci">ronci</a>
            </td>
          </tr>
        </tbody>
      </table>

      <table>
        <caption>
          <Text
            color={themeContext.onBackground}
            size={20}
            fontWeight="bold"
            style={{ marginBottom: '1rem' }}
          >
            Backend
          </Text>
        </caption>
        <br />
        <thead>
          <tr>
            <th align="center">토닉</th>
            <th align="center">알파</th>
            <th align="center">더즈</th>
            <th align="center">조조그린</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td align="center">
              <a
                target="_blank"
                rel="noopener noreferrer"
                href="https://avatars.githubusercontent.com/u/59171113?v=4"
              >
                <img
                  src="https://avatars.githubusercontent.com/u/59171113?v=4"
                  alt="tonic"
                  width="100"
                  height="100"
                  style={{ maxWidth: '100%' }}
                />
              </a>
            </td>
            <td align="center">
              <a
                target="_blank"
                rel="noopener noreferrer"
                href="https://avatars.githubusercontent.com/u/50986686?v=4"
              >
                <img
                  src="https://avatars.githubusercontent.com/u/50986686?v=4"
                  alt="alpha"
                  width="100"
                  height="100"
                  style={{ maxWidth: '100%' }}
                />
              </a>
            </td>
            <td align="center">
              <a
                target="_blank"
                rel="noopener noreferrer"
                href="https://avatars.githubusercontent.com/u/78652144?v=4"
              >
                <img
                  src="https://avatars.githubusercontent.com/u/78652144?v=4"
                  alt="does"
                  width="100"
                  height="100"
                  style={{ maxWidth: '100%' }}
                />
              </a>
            </td>
            <td align="center">
              <a
                target="_blank"
                rel="noopener noreferrer"
                href="https://avatars.githubusercontent.com/u/82805588?v=4"
              >
                <img
                  src="https://avatars.githubusercontent.com/u/82805588?v=4"
                  alt="jojo_green"
                  width="100"
                  height="100"
                  style={{ maxWidth: '100%' }}
                />
              </a>
            </td>
          </tr>
          <tr>
            <td align="center">
              <a href="https://github.com/tonic523">tonic523</a>
            </td>
            <td align="center">
              <a href="https://github.com/bcc0830">bcc0830</a>
            </td>
            <td align="center">
              <a href="https://github.com/ldk980130">ldk980130</a>
            </td>
            <td align="center">
              <a href="https://github.com/jojogreen91">jojogreen91</a>
            </td>
          </tr>
        </tbody>
      </table>
      <Button
        onClick={() => handleLink('https://forms.gle/1nuk3EozEBzRtRFy9')}
        size="large"
      >
        에러 제보 및 개선 건의
      </Button>
    </Wrapper>
  );
};

export default VocPage;

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
