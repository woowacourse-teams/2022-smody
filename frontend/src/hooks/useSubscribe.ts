import {
  useGetVapidPublicKey,
  usePostSubscribe,
  usePostUnsubscribe,
} from 'apis/pushNotificationApi';
import { pushStatus } from 'push/pushStatus';
import { useEffect, useState } from 'react';
import { urlB64ToUint8Array } from 'utils';

const useSubscribe = () => {
  const [isSubscribed, setIsSubscribed] = useState(false);
  const [isLoadingSubscribe, setIsLoadingSubscribe] = useState(false);

  const { refetch: getVapidPublicKey, data: vapid } = useGetVapidPublicKey({
    suspense: false,
  });
  const { mutate: postSubscribe } = usePostSubscribe();
  const { mutate: postUnsubscribe } = usePostUnsubscribe();

  useEffect(() => {
    getVapidPublicKey();
  }, []);

  const isAlreadySubscribed = !!pushStatus.pushSubscription;

  useEffect(() => {
    setIsSubscribed(isAlreadySubscribed);
  }, [isAlreadySubscribed]);

  const isAlreadySubscribed = !!pushStatus.pushSubscription;

  useEffect(() => {
    setIsSubscribed(isAlreadySubscribed);
  }, [isAlreadySubscribed]);

  const pushSubscribe = () => {
    if (typeof vapid === 'undefined') {
      return;
    }
    setIsLoadingSubscribe(true);

    const publicKey = urlB64ToUint8Array(vapid.data.publicKey);

    navigator.serviceWorker.ready.then((registration) => {
      const option = {
        userVisibleOnly: true,
        applicationServerKey: publicKey,
      };

      registration.pushManager
        .subscribe(option)
        .then((subscription) => {
          postSubscribe(subscription);
          pushStatus.pushSubscription = subscription;

          setIsSubscribed(true);
          setIsLoadingSubscribe(false);
        })
        .catch(() => {
          pushStatus.pushSubscription = null;
        });
    });
  };

  const pushUnsubscribe = () => {
    if (!pushStatus.pushSubscription) {
      return;
    }

    pushStatus.pushSubscription.unsubscribe().then((result) => {
      if (result && pushStatus.pushSubscription) {
        postUnsubscribe(pushStatus.pushSubscription.endpoint);
        pushStatus.pushSubscription = null;
        setIsSubscribed(false);
      }
    });
  };

  const subscribe = () => {
    if (!pushStatus.pushSupport) {
      return;
    }

    Notification.requestPermission().then((permission) => {
      pushStatus.notificationPermission = permission;

      if (permission !== 'granted') {
        return;
      }

      if (pushStatus.pushSubscription) {
        pushUnsubscribe();
      } else {
        pushSubscribe();
      }
    });
  };

  return { isSubscribed, subscribe, isLoadingSubscribe };
};

export default useSubscribe;
