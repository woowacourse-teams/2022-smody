import { Tab } from './Tab';
import { TabsProps } from './type';
import { useLocation, useNavigate } from 'react-router-dom';
import styled, { css } from 'styled-components';

import { FlexBox } from 'components';

export const Tabs = ({ tabList }: TabsProps) => {
  const navigate = useNavigate();
  const { pathname } = useLocation();

  return (
    <TabList as="ul" alignItems="center">
      {tabList.map(({ path, tabName }) => (
        <Tab
          key={path}
          tabName={tabName}
          isSelected={path === pathname}
          onClick={() => navigate(path)}
        />
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
