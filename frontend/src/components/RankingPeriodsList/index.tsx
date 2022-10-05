import { SelectListProps } from './type';
import useRankingPeriodsList from './useRankingPeriodsList';
import { BsFillCaretDownFill, BsFillCaretUpFill } from 'react-icons/bs';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { FlexBox } from 'components/@shared/FlexBox';

import { RankingPeriodItem, Text } from 'components';

export const RankingPeriodsList = () => {
  const themeContext = useThemeContext();
  const {
    selectedPeriodIndex,
    showSelectBox,
    rankingPeriodsData,
    handleSelectBox,
    handleChooseRankingPeriod,
    titleDate,
    detailDate,
  } = useRankingPeriodsList();

  if (typeof rankingPeriodsData?.data === 'undefined') {
    return null;
  }

  return (
    <FlexBox flexDirection="column" gap="0.5rem">
      <Text size={24} color={themeContext.onBackground} fontWeight="bold">
        {titleDate}
      </Text>
      <FlexBox justifyContent="flex-end">
        <div></div>
        <SelectPeriod role="button" onClick={handleSelectBox} alignItems="center">
          {detailDate}&nbsp;
          {showSelectBox ? <BsFillCaretUpFill /> : <BsFillCaretDownFill />}
          <SelectList show={showSelectBox}>
            {rankingPeriodsData.data.map((rankingPeriod, index) => (
              <RankingPeriodItem
                key={rankingPeriod.rankingPeriodId}
                selected={selectedPeriodIndex === index}
                handleChooseRankingPeriod={() =>
                  handleChooseRankingPeriod({
                    index,
                    rankingPeriodId: rankingPeriod.rankingPeriodId,
                  })
                }
                {...rankingPeriod}
              />
            ))}
          </SelectList>
        </SelectPeriod>
      </FlexBox>
    </FlexBox>
  );
};

const SelectPeriod = styled(FlexBox)`
  ${({ theme }) => css`
    width: fit-content;
    height: 2.5rem;
    line-height: 2.5rem;
    font-weight: bold;
    font-size: 1rem;
    text-align: center;
    border-radius: 15px;
    background-color: ${theme.surface};
    color: ${theme.primary};
    padding: 0 0.8rem;
    position: relative;
    cursor: pointer;
  `}
`;

const SelectList = styled.ul<SelectListProps>`
  ${({ theme, show }) => css`
    display: ${!show && 'none'};
    width: fit-content;
    max-height: 10rem;
    overflow-y: scroll;
    position: absolute;
    top: 2.5rem;
    right: 0.75rem;
    border-radius: 0 0 10px 10px;

    // 스크롤바
    /* 스크롤바 설정*/
    &::-webkit-scrollbar {
      width: 4px;
    }

    /* 스크롤바 막대 설정*/
    &::-webkit-scrollbar-thumb {
      height: 17%;
      background-color: ${theme.primary};
      border-radius: 100px;
    }

    /* 스크롤바 뒷 배경 설정*/
    &::-webkit-scrollbar-track {
      background-color: ${theme.surface};
      border-radius: 100px;
    }
  `}
`;
