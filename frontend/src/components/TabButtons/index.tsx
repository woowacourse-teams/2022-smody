import { TabButtonsProps } from './type';

import { Button, FlexBox } from 'components';

export const TabButtons = ({
  isFirstTab,
  setIsFirstTab,
  firstTabName,
  secondTabName,
}: TabButtonsProps) => {
  return (
    <FlexBox gap="1rem">
      <Button
        onClick={() => setIsFirstTab(true)}
        size="small"
        style={{ backgroundColor: isFirstTab ? '#7B61FF' : '#B8B5FF', color: '#fff' }}
      >
        {firstTabName}
      </Button>
      <Button
        onClick={() => setIsFirstTab(false)}
        size="small"
        style={{ backgroundColor: isFirstTab ? '#B8B5FF' : '#7B61FF', color: '#fff' }}
      >
        {secondTabName}
      </Button>
    </FlexBox>
  );
};
