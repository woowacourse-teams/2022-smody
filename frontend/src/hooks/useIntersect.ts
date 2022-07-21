import { useRef, useEffect, useCallback, RefObject } from 'react';

type IntersectHandler = (
  entry: IntersectionObserverEntry,
  observer: IntersectionObserver,
) => void;

const useIntersect = <T extends HTMLElement, U extends HTMLElement>(
  onIntersect: IntersectHandler,
  options?: IntersectionObserverInit,
) => {
  const rootRef = useRef<T>(null) as RefObject<T>;
  const targetRef = useRef<U>(null);

  const defaultOptions = {
    root: rootRef.current,
    threshold: 0.5,
    ...options,
  };

  const callback = useCallback(
    (entries: IntersectionObserverEntry[], observer: IntersectionObserver) => {
      entries.forEach((entry) => {
        console.log(entry);
        if (entry.isIntersecting) {
          observer.unobserve(entry.target);
          onIntersect(entry, observer);
        }
      });
    },
    [onIntersect],
  );

  useEffect(() => {
    if (!targetRef.current) return;

    const observer = new IntersectionObserver(callback, defaultOptions);
    observer.observe(targetRef.current);

    return () => observer.disconnect();
  }, [callback]);

  return { targetRef, rootRef };
};

export default useIntersect;
