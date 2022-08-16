import useTitle from './useTitle';
import { MdArrowBackIosNew } from 'react-icons/md';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';
import { TitleProps } from 'components/Title/type';

export const Title = ({ text, linkTo, children }: TitleProps) => {
  const themeContext = useThemeContext();
  const { backToPreviousPage } = useTitle({ linkTo });

  return (
    <TitleWrapper flexDirection="row" justifyContent="space-between">
      <IconWrapper>
        <MdArrowBackIosNew
          size={20}
          onClick={backToPreviousPage}
          // style={{ position: 'absolute', left: '0', top: '0', cursor: 'pointer' }}
        />
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
  margin-bottom: 2rem;
`;

const IconWrapper = styled.div`
  cursor: pointer;
`;
