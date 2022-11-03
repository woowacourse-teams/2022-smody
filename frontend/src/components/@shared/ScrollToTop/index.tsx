import { ScrollToTopProps } from './type';
import React, { PropsWithChildren } from 'react';

export class ScrollToTop extends React.Component<PropsWithChildren<ScrollToTopProps>> {
  constructor(props: PropsWithChildren<ScrollToTopProps>) {
    super(props);
  }

  componentDidUpdate(prevProps: PropsWithChildren<ScrollToTopProps>) {
    const { pathname: prevPathname } = prevProps;
    const { pathname: currentPathname } = this.props;

    // 컴포넌트 업데이트 시 페이지 이동인지 확인
    if (prevPathname !== currentPathname) {
      window.scrollTo(0, 0);
    }
  }

  render() {
    const { children } = this.props;

    return children;
  }
}
