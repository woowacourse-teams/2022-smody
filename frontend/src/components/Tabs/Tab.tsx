import { TabProps } from './type';
import styled from 'styled-components';

import COLOR from 'styles/color';

export const Tab = ({ isSelected, onClick, tabName }: TabProps) => {
  return (
    <TabLi isSelected={isSelected} onClick={onClick}>
      {tabName}
    </TabLi>
  );
};

const TabLi = styled.li<{ isSelected: boolean }>`
  ${({ isSelected, theme }) => `
      cursor: pointer;
      font-size: 17px;
      padding: 1rem 1rem;
      border-bottom: 2px solid ${COLOR.LIGHT_GRAY};
      color: ${theme.onBackground};
  
      border-color: ${isSelected && theme.primary};
      font-weight: ${isSelected ? 'bold' : 'normal'};
  
      &:hover {
         border-color: ${theme.primary};
      }
    `}
`;
