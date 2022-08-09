import { apiClient, authApiClient } from 'apis/apiClient';
import { useEffect, useState } from 'react';

import { Button } from 'components';

let pushSupport = false;
let userSubscription: PushSubscription;

const urlB64ToUint8Array = (base64String: string) => {
  const padding = '='.repeat((4 - (base64String.length % 4)) % 4);
  const base64 = (base64String + padding).replace(/-/g, '+').replace(/_/g, '/');

  const rawData = atob(base64);
  const outputArray = new Uint8Array(rawData.length);

  for (let i = 0; i < rawData.length; i++) {
    outputArray[i] = rawData.charCodeAt(i);
  }
  return outputArray;
};

const updateSubscription = (subscription: PushSubscription) => {
  // @ch10. 푸시 구독 정보 전송 기능 구현
  authApiClient.axios.post('/web-push/subscribe', { subscription }).catch((err) => {
    console.error(err);
  });
};

const TestPage = () => {
  const [isSubscribed, setIsSubscribed] = useState(false);
  useEffect(() => {
    if ('serviceWorker' in navigator) {
      // 푸시 기능 지원 여부에 따라 알림 구독 버튼 보이기/숨기기 처리
      navigator.serviceWorker.ready.then((registration) => {
        if (registration.pushManager) {
          console.log('registration.pushManager', registration.pushManager);
          pushSupport = true;
          // notificationControl.classList.remove('disabled');

          // 구독 정보 불러오기
          registration.pushManager.getSubscription().then((subscription) => {
            // 구독 정보 가져온 후 userSubscription 변수에 저장
            userSubscription = subscription!;
            console.log('userSubscription', userSubscription);
          });
        }
      });
    }
  }, []);

  function pushSubscribe() {
    // @ch10. 푸시 구독 기능 구현
    apiClient.axios
      .get('/web-push/public-key')
      .then((response) => {
        // Uint8Array 타입으로 변환
        const publicKey = urlB64ToUint8Array(response.data);

        navigator.serviceWorker.ready.then((registration) => {
          // 구독 옵션
          const option = {
            userVisibleOnly: true,
            applicationServerKey: publicKey,
          };

          // 푸시 서비스 구독
          registration.pushManager
            .subscribe(option)
            .then((subscription) => {
              // 애플리케이션 서버로 구독 정보 전달
              updateSubscription(subscription);
              userSubscription = subscription;
              console.log('Push subscribed!', subscription);
            })
            .catch((err) => {
              // userSubscription = null;
              console.error('Push subscribe failed:', err);
              alert('푸시 알림을 구독할 수 없습니다.');
            })
            .finally(() => {
              setIsSubscribed((prev) => !prev);
            });
        });
      })
      .catch((err) => {
        console.error(err);
      });
  }

  const handleClickSubscribeButton = () => {
    //  권한 확인 및 요청
    console.log('시작');
    if (!pushSupport) {
      console.log('ㅇ');

      return;
    } else {
      Notification.requestPermission().then((permission) => {
        console.log('Push Permission:', permission);
        // setIsSubscribed((prev) => !prev);

        if (Notification.permission !== 'granted') {
          return;
        } else {
          if (userSubscription) {
            // pushUnsubscribe();
            console.log(userSubscription);
          } else {
            console.log('hi');
            pushSubscribe();
          }
        }
      });
    }
  };
  return (
    <div>
      <h1>TestPage</h1>
      <Button size="medium" onClick={handleClickSubscribeButton}>
        구독
      </Button>
    </div>
  );
};

export default TestPage;
