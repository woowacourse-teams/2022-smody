import useSnackBar from 'hooks/useSnackBar';

let deferredPrompt: any; // 비표준 API라서 any로 선언

window.addEventListener('beforeinstallprompt', (event) => {
  event.preventDefault();
  deferredPrompt = event;
});

const useInstallApp = () => {
  const renderSnackBar = useSnackBar();

  const installApp = () => {
    if (!deferredPrompt) {
      renderSnackBar({
        message: '이미 앱이 설치되어 있거나 앱을 설치할 수 없는 환경입니다.',
        status: 'ERROR',
      });
      return;
    }

    deferredPrompt.prompt();
  };
  return { installApp };
};

export default useInstallApp;
