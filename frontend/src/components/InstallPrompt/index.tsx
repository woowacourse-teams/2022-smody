import { useEffect, useState } from 'react';
import styled, { css } from 'styled-components';
import { setCookie, getCookie } from 'utils';

import useInstallApp from 'hooks/useInstallApp';
import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, Button, BottomSheet } from 'components';

export const InstallPrompt = () => {
  const themeContext = useThemeContext();

  const { installApp, isInstallPromptDeferred, isNotInstalledInIOS } = useInstallApp();
  const isAbleInstall = isNotInstalledInIOS || isInstallPromptDeferred;
  const [isBottomSheetOpen, setIsBottomSheetOpen] = useState(true);
  const [isInstallPromptHide, setIsInstallPromptHide] = useState('show');

  const handleCloseBottomSheet = () => {
    setIsBottomSheetOpen(false);
  };

  const hideInstallPrompt = () => {
    setIsBottomSheetOpen(false);
    setCookie('isInstallPromptHide', 'hide', 604800); // 1주일
  };

  useEffect(() => {
    const isHide = getCookie('isInstallPromptHide');
    if (isHide) {
      setIsInstallPromptHide(isHide);
    }
  }, []);

  return (
    <>
      {isInstallPromptHide === 'show' && isBottomSheetOpen && isAbleInstall && (
        <BottomSheet handleCloseBottomSheet={handleCloseBottomSheet} height="225px">
          <FlexBox flexDirection="column" gap="1rem" style={{ width: '80%' }}>
            {isInstallPromptDeferred && (
              <FlexBox flexDirection="column" alignItems="center" gap="0.7rem">
                <Text size={20} fontWeight="bold" color={themeContext.onSurface}>
                  Smody 앱을 설치하시겠습니까?
                </Text>
                <FlexBox gap="1rem">
                  <CancelButton size="medium" onClick={handleCloseBottomSheet}>
                    취소
                  </CancelButton>
                  <Button size="medium" onClick={installApp}>
                    앱 설치
                  </Button>
                </FlexBox>
              </FlexBox>
            )}

            {isNotInstalledInIOS && (
              <FlexBox flexDirection="column" alignItems="center" gap="0.7rem">
                <Text size={20} fontWeight="bold" color={themeContext.onSurface}>
                  iOS에서 Smody 앱 설치 방법
                </Text>
                <p style={{ lineHeight: '1.5' }}>
                  Safari 브라우저에서{' '}
                  <img
                    src="https://help.apple.com/assets/625460ED521B2511EE32CF0E/625460F4521B2511EE32CF50/ko_KR/d26fe35d3438fe81179a80c2b6c9b0c9.png"
                    alt="공유 버튼"
                    height="20"
                    width="18"
                  />
                  을 탭하고 옵션 목록을 아래로 스크롤한 다음, ‘홈 화면에 추가’를 탭하세요.
                </p>
              </FlexBox>
            )}
            <Text
              onClick={hideInstallPrompt}
              size={14}
              color={themeContext.onSecondary}
              style={{
                cursor: 'pointer',
                textAlign: 'center',
                textDecoration: 'underline',
                marginTop: '1rem',
              }}
            >
              1주일 동안 보지 않기
            </Text>
          </FlexBox>
        </BottomSheet>
      )}
    </>
  );
};

const CancelButton = styled(Button)`
  ${({ theme }) => css`
    background-color: ${theme.surface};
    border: 1px solid ${theme.primary};
    color: ${theme.primary};
  `}
`;
