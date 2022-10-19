import { InfoAboutSubscribeProps, SubscriptionButtonProps } from './type';
import { useSubscriptionButton } from './useSubscriptionButton';
import LoadingDots from 'assets/loading_dots.svg';
import { pushStatus } from 'pwa/pushStatus';
import { FaTrashAlt } from 'react-icons/fa';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, ToggleButton, UnderLineText, Text } from 'components';

export const SubscriptionButton = ({ updateIsSubscribed }: SubscriptionButtonProps) => {
  const themeContext = useThemeContext();
  const {
    isSubscribed,
    subscribe,
    isLoadingSubscribe,
    isAbleSubscribe,
    handleClickDeleteAllNotifications,
    isLoadingDeleteAllNotifications,
  } = useSubscriptionButton({ updateIsSubscribed });

  return (
    <div>
      <InfoAboutSubscribe isAbleSubscribe={isAbleSubscribe} />
      <SubscriptionWrapper alignItems="center" gap="1rem">
        <UnderLineText
          fontSize={16}
          fontColor={themeContext.onSurface}
          fontWeight="bold"
          underLineColor={themeContext.primary}
        >
          알림 설정
        </UnderLineText>

        <ToggleButton
          disabled={isLoadingSubscribe}
          checked={isSubscribed}
          handleChange={subscribe}
        />
        <DeleteWrapper>
          <DeleteButton
            type="button"
            disabled={isLoadingDeleteAllNotifications}
            onClick={() => handleClickDeleteAllNotifications()}
          >
            <FaTrashAlt
              size={20}
              color={
                isLoadingDeleteAllNotifications
                  ? themeContext.disabled
                  : themeContext.primary
              }
            />
          </DeleteButton>
        </DeleteWrapper>
        {isLoadingSubscribe && (
          <LoadingWrapper>
            <LoadingDots />
          </LoadingWrapper>
        )}
      </SubscriptionWrapper>
    </div>
  );
};

const InfoAboutSubscribe = ({ isAbleSubscribe }: InfoAboutSubscribeProps) => {
  const themeContext = useThemeContext();

  if (isAbleSubscribe) {
    return null;
  }

  return (
    <InfoAboutSubscribeWrapper flexDirection="column" justifyContent="center">
      {!pushStatus.pushSupport && (
        <ErrorText size={12} color={themeContext.error}>
          [알림 기능을 사용할 수 없는 브라우저입니다.]
          <br />
          크롬 브라우저 사용을 권장합니다.
          <br />
        </ErrorText>
      )}
      {pushStatus.notificationPermission !== 'granted' && (
        <div>
          <ErrorText size={12} color={themeContext.error}>
            [브라우저나 OS에서 알림 전송 설정이 꺼져있습니다.]
            <br />
            아래 링크를 참고하여 알림을 켜주세요.
            <br />
          </ErrorText>
          <a
            href="https://woowa.notion.site/7b0c17a8206743bea14f90210b699067"
            target="_blank"
            rel="noreferrer"
          >
            <Text size={12} color={themeContext.primary} fontWeight="bold">
              알림 설정 문제 해결 문서
            </Text>
          </a>
        </div>
      )}
    </InfoAboutSubscribeWrapper>
  );
};

const SubscriptionWrapper = styled(FlexBox)`
  cursor: default;
  padding: 0 1rem;
  margin: 1rem 0 0.5rem;
`;

const InfoAboutSubscribeWrapper = styled(FlexBox)`
  padding: 0 1rem;
  margin: 1rem 0;
  cursor: default;
`;

const LoadingWrapper = styled.div`
  ${({ theme }) => css`
    width: 2.4rem;
    height: 2rem;

    & svg circle {
      fill: ${theme.onSurface};
      stroke: none;
    }
  `}
`;

const ErrorText = styled(Text)`
  line-height: 1.5;
`;

const DeleteWrapper = styled.div`
  flex-grow: 1;
  text-align: end;
`;

const DeleteButton = styled.button`
  &:disabled {
    cursor: wait;
  }
`;
