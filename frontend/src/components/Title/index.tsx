import useTitle from './useTitle';
import { PropsWithChildren } from 'react';
import { MdArrowBackIosNew } from 'react-icons/md';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';
import { TitleProps } from 'components/Title/type';

export const Title = ({ text, linkTo, children }: PropsWithChildren<TitleProps>) => {
  const themeContext = useThemeContext();
  const { backToPreviousPage } = useTitle({ linkTo });

  return (
    <TitleWrapper
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
  position: relative;
  width: 100%;
  margin-bottom: 1.5rem;
`;

const IconWrapper = styled.div`
  cursor: pointer;
`;
