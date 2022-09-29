import { useEffect, useRef } from 'react';

const DEFAULT_CONFIG = {
  subtree: true,
  characterData: true,
  childList: true,
} as const;

const useMutationObserver = <T extends HTMLElement>(
  onMutate: MutationCallback,
  config: MutationObserverInit = DEFAULT_CONFIG,
) => {
  const ref = useRef<T>(null);

  useEffect(() => {
    const observer = new MutationObserver(onMutate);

    observer.observe(ref.current!, config);

    return () => {
      observer.disconnect();
    };
  }, []);

  return ref;
};

export default useMutationObserver;
