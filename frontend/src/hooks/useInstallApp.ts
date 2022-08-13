import useSnackBar from 'hooks/useSnackBar';

const useInstallApp = () => {
  let deferredPrompt: any; // 비표준 API라서 any로 선언
  const renderSnackBar = useSnackBar();

  window.addEventListener('beforeinstallprompt', (event) => {
    event.preventDefault();
    deferredPrompt = event;
  });

  const installApp = () => {
    if (window.matchMedia('(display-mode: standalone)').matches) {
      renderSnackBar({
        message: '이미 앱이 설치되어 있습니다. 앱으로 접속해주세요.',
        status: 'ERROR',
      });
      return;
    }

    if (!deferredPrompt) {
      renderSnackBar({
        message:
          '앱 설치가 불가한 OS 또는 브라우저입니다. Android 및 Chrome을 권장합니다.',
        status: 'ERROR',
      });
      return;
    }

    deferredPrompt.prompt();
    deferredPrompt.userChoice.then(() => {
      deferredPrompt = null;
    });
  };
  return { installApp };
};

export default useInstallApp;
