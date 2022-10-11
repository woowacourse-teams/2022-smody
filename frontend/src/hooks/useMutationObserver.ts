import { RefObject, useEffect } from 'react';

const DEFAULT_CONFIG = {
  subtree: true,
  characterData: true,
  childList: true,
} as const;

const useMutationObserver = <T extends HTMLElement>(
  ref: RefObject<T>,
  onMutate: MutationCallback,
  config: MutationObserverInit = DEFAULT_CONFIG,
) => {
  useEffect(() => {
    const observer = new MutationObserver(onMutate);

    observer.observe(ref.current!, config);

    return () => {
      observer.disconnect();
    };
  }, []);
};

export default useMutationObserver;
