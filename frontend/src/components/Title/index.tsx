import useTitle from './useTitle';
import { PropsWithChildren } from 'react';
import { MdArrowBackIosNew } from 'react-icons/md';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';
import { TitleProps } from 'components/Title/type';

import { Z_INDEX } from 'constants/css';

export const Title = ({ text, linkTo, children }: PropsWithChildren<TitleProps>) => {
  const themeContext = useThemeContext();
  const { backToPreviousPage, TitleRef } = useTitle({ linkTo });

  return (
    <TitleWrapper
      ref={TitleRef}
      flexDirection="row"
      justifyContent="space-between"
      alignItems="center"
      gap="10px"
    >
      <IconWrapper>
        <MdArrowBackIosNew size={20} onClick={backToPreviousPage} />
      </IconWrapper>
      <Text fontWeight="bold" size={20} color={themeContext.onBackground}>
        {text}
      </Text>
      <div>{children}</div>
    </TitleWrapper>
  );
};

const TitleWrapper = styled(FlexBox)`
  ${({ theme }) => css`
    position: fixed;
    top: 3rem;
    left: 0;
    width: 100%;
    height: 65.5px;
    padding: 0 1.5rem;
    z-index: ${Z_INDEX.TITLE};
    background-color: ${theme.background};
    transition: top 0.4s;
  `}
`;

const IconWrapper = styled.div`
  cursor: pointer;
`;
