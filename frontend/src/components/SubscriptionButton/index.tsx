import { SubscriptionButtonProps } from './type';
import LoadingDots from 'assets/loading_dots.svg';
import { pushStatus } from 'pushStatus';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, ToggleButton, UnderLineText, Text } from 'components';

export const SubscriptionButton = ({
  isSubscribed,
  subscribe,
  isLoadingSubscribe,
}: SubscriptionButtonProps) => {
  const themeContext = useThemeContext();
  const isAbleSubscribe =
    pushStatus.pushSupport && pushStatus.notificationPermission === 'granted';

  return (
    <>
      {!isAbleSubscribe && (
        <FlexBox
          justifyContent="center"
          style={{ cursor: 'default', padding: '0 1rem ', margin: '1rem 0 ' }}
        >
          <Text size={12} color={themeContext.error} style={{ lineHeight: '1.5' }}>
            {!pushStatus.pushSupport && (
              <>
                <p>[알림 기능을 사용할 수 없는 브라우저입니다.]</p>
                <p>크롬 브라우저 사용을 권장합니다.</p>
              </>
            )}
            {pushStatus.notificationPermission !== 'granted' && (
              <>
                <p>[브라우저나 OS에서 알림 전송 설정이 꺼져있습니다.]</p>
                <p>아래 링크를 참고하여 알림을 켜주세요.</p>
                <a
                  href="https://woowa.notion.site/7b0c17a8206743bea14f90210b699067"
                  target="_blank"
                  rel="noreferrer"
                >
                  <Text size={12} color={themeContext.primary} fontWeight="bold">
                    알림 설정 문제 해결 문서
                  </Text>
                </a>
              </>
            )}
          </Text>
        </FlexBox>
      )}
      <FlexBox
        alignItems="center"
        gap="1rem"
        style={{ cursor: 'default', padding: '0 1rem ' }}
      >
        <UnderLineText
          fontSize={16}
          fontColor={themeContext.onSurface}
          fontWeight="bold"
          underLineColor={themeContext.primary}
        >
          {!isAbleSubscribe ? '알림 설정 불가' : isSubscribed ? '알림 끄기' : '알림 켜기'}
        </UnderLineText>

        <ToggleButton
          disabled={!isAbleSubscribe || isLoadingSubscribe}
          checked={isSubscribed}
          handleChange={subscribe}
        />
        {isLoadingSubscribe && (
          <FlexBox gap="0.5rem" flexDirection="row" alignItems="center">
            <Text size={14} color={themeContext.onSurface}>
              알림 설정 중
            </Text>
            <LoadingWrapper>
              <LoadingDots />
            </LoadingWrapper>
          </FlexBox>
        )}
      </FlexBox>
    </>
  );
};

const LoadingWrapper = styled.div`
  width: 2rem;
  height: 2rem;
`;
