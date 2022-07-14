import { usePostLogin } from 'apis';
import { authApiClient } from 'apis/apiClient';
import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue, useRecoilState, useSetRecoilState } from 'recoil';
import { nicknameState, isLoginState } from 'recoil/auth/atoms';
import styled, { ThemeContext, css } from 'styled-components';

import { Logo, FlexBox, Text, Button } from 'components';

import { CLIENT_PATH } from 'constants/path';

export const Header = () => {
  const [isLogin, setIsLogin] = useRecoilState(isLoginState);
  const nickname = useRecoilValue(nicknameState);
  const themeContext = useContext(ThemeContext);
  const navigate = useNavigate();
  const setNickname = useSetRecoilState(nicknameState);

  const { mutate } = usePostLogin({
    onSuccess: (data) => {
      console.log(data.data);
      window.location.href = data.data + '&redirect_uri=http://localhost:3000/home'

      // window.location.

      // const { }
      // console.log("로그인 응답 데이터 : ", data);
      // window.location.href = data + '&redirect_uri=http://localhost:3000/'
      // setIsLogin(true);
      // setNickname(nickname);
      //
      // authApiClient.updateAuth(accessToken);
      // navigate(CLIENT_PATH.HOME);
    },
    onError: () => {
      alert('로그인 실패...');
      throw new Error();
    },
  });

  const handleLogin = () => {
    console.log('로그인 버튼 눌림!!');
    mutate();
  };

  const handleLogout = () => {
    authApiClient.deleteAuth();
    setIsLogin(false);
    navigate(CLIENT_PATH.HOME);
  };

  return (
    <Wrapper>
      <Logo width="100" color={themeContext.onPrimary} />
      {isLogin ? (
        <UserWrapper>
          <Text size={20} color={themeContext.onPrimary}>
            {nickname}
          </Text>
          <button onClick={handleLogout}>
            <Text size={20} color={themeContext.onPrimary}>
              로그아웃
            </Text>
          </button>
        </UserWrapper>
      ) : (
        <Button size="small" onClick={handleLogin}>
          로그인
        </Button>
      )}
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  justifyContent: 'space-between',
  alignItems: 'center',
})`
  ${({ theme }) => css`
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    background-color: ${theme.primary};
    padding: 1.2rem 4rem;
    z-index: 1000;
  `}
`;

const UserWrapper = styled(FlexBox).attrs({
  alignItems: 'center',
  gap: '1rem',
})`
  width: fit-content;
`;
