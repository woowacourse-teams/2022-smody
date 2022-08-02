import styled from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox, Text, LinkText } from 'components';
import { EmptyContentProps } from 'components/EmptyContent/type';

export const EmptyContent = ({
  title,
  description,
  linkText,
  linkTo,
}: EmptyContentProps) => {
  const themeContext = useThemeContext();

  return (
    <Wrapper>
      <Text size={20} color={themeContext.onBackground} fontWeight="bold">
        {title}
      </Text>
      <Text size={16} color={themeContext.onBackground}>
        {description}
      </Text>
      <LinkText size={16} color={themeContext.primary} to={linkTo}>
        {linkText}
      </LinkText>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  gap: '1rem',
})`
  flex-grow: 1;
  height: 100%;
`;
