import { ScrollToTopProps } from './type';
import React from 'react';

export class ScrollToTop extends React.Component<ScrollToTopProps> {
  constructor(props: ScrollToTopProps) {
    super(props);
  }

  componentDidUpdate(prevProps: ScrollToTopProps) {
    const { pathname: prevPathname } = prevProps;
    const { pathname: currentPathname } = this.props;

    // 컴포넌트 업데이트 시 페이지 이동인지 확인
    if (prevPathname !== currentPathname) {
      window.scrollTo(0, 0);
    }
  }

  render() {
    return this.props.children;
  }
}
