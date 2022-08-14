import { queryKeys } from 'apis/constants';
import {
  useGetVapidPublicKey,
  usePostSubscribe,
  usePostUnsubscribe,
} from 'apis/pushNotificationApi';
import { pushStatus } from 'pushStatus';
import { useEffect, useState } from 'react';
import { useQueryClient } from 'react-query';
import { urlB64ToUint8Array } from 'utils';

let pushSupport = false;
let userSubscription: PushSubscription | null;

const useSubscribe = () => {
  const isAlreadySubscribed = !!pushStatus.pushSubscription;
  const [isSubscribed, setIsSubscribed] = useState(false);
  const [isLoadingSubscribe, setIsLoadingSubscribe] = useState(false);
  const queryClient = useQueryClient();
  const { refetch: getVapidPublicKey, data } = useGetVapidPublicKey();
  const { mutate: postSubscribe } = usePostSubscribe({
    onSuccess: () => {
      queryClient.invalidateQueries(queryKeys.getNotifications);
    },
  });
  const { mutate: postUnsubscribe } = usePostUnsubscribe();

  useEffect(() => {
    setIsSubscribed(isAlreadySubscribed);
  }, [isAlreadySubscribed]);

  useEffect(() => {
    getVapidPublicKey();

    if ('serviceWorker' in navigator) {
      // 푸시 기능 지원 여부에 따라 알림 구독 버튼 보이기/숨기기 처리
      navigator.serviceWorker.ready.then((registration) => {
        if (registration.pushManager) {
          console.log('0-1 registration.pushManager', registration.pushManager);
          pushSupport = true;

          // 구독 정보 불러오기
          registration.pushManager.getSubscription().then((subscription) => {
            // 구독 정보 가져온 후 userSubscription 변수에 저장
            if (subscription === null) {
              return;
            }
            userSubscription = subscription;
            console.log('0-2 userSubscription', userSubscription);
          });
        }
      });
    }
  }, []);

  const pushSubscribe = () => {
    // 푸시 구독 기능 구현
    if (typeof data === 'undefined') {
      return;
    }
    setIsLoadingSubscribe(true);
    // Uint8Array 타입으로 변환
    const publicKey = urlB64ToUint8Array(data.data.publicKey);
    console.log('5-publicKey', publicKey);

    navigator.serviceWorker.ready.then((registration) => {
      // 구독 옵션
      const option = {
        userVisibleOnly: true,
        applicationServerKey: publicKey,
      };

      console.log('6-registration', registration);

      // 푸시 서비스 구독
      registration.pushManager
        .subscribe(option)
        .then((subscription) => {
          // 애플리케이션 서버로 구독 정보 전달
          postSubscribe(subscription);
          userSubscription = subscription;
          console.log('3-Push subscribed!', userSubscription);
          setIsSubscribed(true);
          setIsLoadingSubscribe(false);
        })
        .catch((err) => {
          userSubscription = null;
          console.error('Push subscribe failed:', err);
        });
    });
  };

  // 푸시 구독 취소
  const pushUnsubscribe = () => {
    if (!userSubscription) {
      return;
    }

    // 푸시 서비스 구독 취소
    userSubscription.unsubscribe().then((result) => {
      console.log('4-Push unsubscribed:', userSubscription, result);
      if (result && userSubscription) {
        // 애플리케이션 서버에 저장된 구독 정보 지우기
        postUnsubscribe({ endpoint: userSubscription.endpoint });
        userSubscription = null;
        setIsSubscribed(false);
      }
    });
  };

  const subscribe = () => {
    //  권한 확인 및 요청
    console.log('시작');
    if (!pushSupport) {
      console.log('1');
      return;
    }

    Notification.requestPermission().then((permission) => {
      console.log('2-Push Permission:', permission);
      if (Notification.permission !== 'granted') {
        return;
      }

      if (userSubscription) {
        pushUnsubscribe();
      } else {
        pushSubscribe();
      }
    });
  };

  return { isSubscribed, subscribe, isLoadingSubscribe };
};

export default useSubscribe;
