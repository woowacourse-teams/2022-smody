import { MdArrowBackIosNew } from 'react-icons/md';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text } from 'components';
import { TitleProps } from 'components/Title/type';

export const Title = ({ text, linkTo }: TitleProps) => {
  const themeContext = useThemeContext();
  const navigate = useNavigate();
  const backToPreviousPage = () => {
    if (linkTo) {
      navigate(linkTo);
      return;
    }

    navigate(-1);
  };

  return (
    <TitleWrapper flexDirection="row" justifyContent="center">
      <MdArrowBackIosNew
        size={20}
        onClick={backToPreviousPage}
        style={{ position: 'absolute', left: '0', top: '0', cursor: 'pointer' }}
      />
      <Text fontWeight="bold" size={20} color={themeContext.onBackground}>
        {text}
      </Text>
    </TitleWrapper>
  );
};

const TitleWrapper = styled(FlexBox)`
  position: relative;
  width: 100%;
  margin-bottom: 2rem;
`;
