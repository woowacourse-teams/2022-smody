import { useState, useEffect } from 'react';

let deferredPrompt: any; // 비표준 API라서 any로 선언

declare global {
  interface Navigator {
    standalone: boolean;
  }
  interface Window {
    MSStream: unknown;
  }
}

const useInstallApp = () => {
  const [isInstallPromptDeferred, setIsInstallPromptDeferred] = useState(false);

  useEffect(() => {
    window.addEventListener('beforeinstallprompt', deferInstall);

    return () => {
      window.removeEventListener('beforeinstallprompt', deferInstall);
    };
  }, []);

  const deferInstall = (event: { preventDefault: () => void }) => {
    event.preventDefault();
    deferredPrompt = event;
    setIsInstallPromptDeferred(!!deferredPrompt);
  };

  const installApp = () => {
    deferredPrompt.prompt();
    deferredPrompt = null;
  };

  return { installApp, isInstallPromptDeferred };
};

export default useInstallApp;
