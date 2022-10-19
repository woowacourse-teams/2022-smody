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
  const [pwaMode, setPwaMode] = useState('browser');

  const deferInstall = (event: { preventDefault: () => void }) => {
    event.preventDefault();
    deferredPrompt = event;
    setIsInstallPromptDeferred(!!deferredPrompt);
  };

  useEffect(() => {
    (() => {
      window.addEventListener('beforeinstallprompt', deferInstall);
    })();

    setPwaMode(getPWADisplayMode());

    return () => {
      window.removeEventListener('beforeinstallprompt', deferInstall);
    };
  }, []);

  useEffect(() => {
    if (pwaMode !== 'browser') {
      setIsInstallPromptDeferred(false);
    }
  }, [pwaMode]);

  const installApp = async () => {
    deferredPrompt.prompt();
    deferredPrompt = null;
  };

  function getPWADisplayMode() {
    const isStandalone = window.matchMedia('(display-mode: standalone)').matches;
    if (document.referrer.startsWith('android-app://')) {
      return 'twa';
    } else if (navigator.standalone || isStandalone) {
      return 'standalone';
    }
    return 'browser';
  }

  const isIOS =
    (/iPad|iPhone|iPod/.test(navigator.platform) ||
      (navigator.platform === 'MacIntel' && navigator.maxTouchPoints > 1)) &&
    !window.MSStream;

  const isNotInstalledInIOS = pwaMode === 'browser' && isIOS;
  return { installApp, isInstallPromptDeferred, isNotInstalledInIOS };
};

export default useInstallApp;
