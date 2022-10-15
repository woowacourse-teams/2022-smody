import { TabProps, TabsProps } from './type';
import useTabs from './useTabs';
import styled, { css } from 'styled-components';

import { FlexBox } from 'components';

import COLOR from 'styles/color';

export const Tabs = ({ tabList }: TabsProps) => {
  const { pathname, handleClickTab } = useTabs();

  return (
    <TabList as="ul" alignItems="center">
      {tabList.map(({ path, tabName }) => (
        <Tab
          key={path}
          isSelected={path === pathname}
          onClick={() => handleClickTab(path)}
        >
          {tabName}
        </Tab>
      ))}
    </TabList>
  );
};

const TabList = styled(FlexBox)`
  ${({ theme }) => css`
    height: 64px;
    position: fixed;
    background-color: ${theme.background};
    margin-top: -7px;
    width: 100%;
  `}
`;

const Tab = styled.li<TabProps>`
  ${({ isSelected, theme }) => `
      cursor: pointer;
      font-size: 17px;
      padding: 1rem;
      border-bottom: 2px solid ${COLOR.LIGHT_GRAY};
      color: ${theme.onBackground};
  
      border-color: ${isSelected && theme.primary};
      font-weight: ${isSelected ? 'bold' : 'normal'};
  
      &:hover {
         border-color: ${theme.primary};
      }
    `}
`;
