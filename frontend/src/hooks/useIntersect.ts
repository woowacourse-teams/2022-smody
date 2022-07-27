import { useRef, useEffect, useCallback } from 'react';

type IntersectHandler = (
  entry: IntersectionObserverEntry,
  observer: IntersectionObserver,
) => void;

const useIntersect = <T extends HTMLElement>(
  onIntersect: IntersectHandler,
  options?: IntersectionObserverInit,
) => {
  const ref = useRef<T>(null);
  const callback = useCallback(
    (entries: IntersectionObserverEntry[], observer: IntersectionObserver) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) onIntersect(entry, observer);
      });
    },
    [onIntersect],
  );

  useEffect(() => {
    if (!ref.current) return;

    const observer = new IntersectionObserver(callback, options);
    observer.observe(ref.current);

    return () => observer.disconnect();
  }, [callback]);

  return ref;
};

export default useIntersect;
