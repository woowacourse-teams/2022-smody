import useTitle from './useTitle';
import { PropsWithChildren } from 'react';
import { MdArrowBackIosNew } from 'react-icons/md';
import styled, { css } from 'styled-components';

import { FlexBox, Text } from 'components';
import { TitleProps, TitleWrapperProps } from 'components/Title/type';

import { Z_INDEX } from 'constants/css';
import { TITLE_HEIGHT } from 'constants/style';

export const Title = ({ text, linkTo, children }: PropsWithChildren<TitleProps>) => {
  const { backToPreviousPage, TitleRef, themeContext, bgColor } = useTitle({ linkTo });

  return (
    <TitleWrapper
      ref={TitleRef}
      flexDirection="row"
      justifyContent="space-between"
      alignItems="center"
      gap="10px"
      bgColor={bgColor}
    >
      <IconWrapper>
        <MdArrowBackIosNew
          role="button"
          size={20}
          onClick={backToPreviousPage}
          aria-label="뒤로가기"
        />
      </IconWrapper>
      <Text
        fontWeight="bold"
        size={20}
        color={themeContext.onBackground}
        aria-label="현재 문서 위치"
      >
        {text}
      </Text>
      <div>{children}</div>
    </TitleWrapper>
  );
};

const TitleWrapper = styled(FlexBox)<TitleWrapperProps>`
  ${({ bgColor }) => css`
    position: fixed;
    top: 3rem;
    left: 0;
    width: 100%;
    height: ${TITLE_HEIGHT};
    z-index: ${Z_INDEX.TITLE};
    background-color: ${bgColor};
    transition: top 0.4s;

    /* PC (해상도 1024px)*/
    @media all and (min-width: 1024px) {
      padding: 1rem 10rem;
    }

    /* 테블릿 가로, 테블릿 세로 (해상도 768px ~ 1023px)*/
    @media all and (min-width: 768px) and (max-width: 1023px) {
      padding: 1rem 7rem;
    }

    /* 모바일 가로, 모바일 세로 (해상도 480px ~ 767px)*/
    @media all and (max-width: 767px) {
      padding: 1rem 1.25rem;
    }
  `}
`;

const IconWrapper = styled.div`
  cursor: pointer;
`;
