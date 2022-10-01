import { RankingPeriodsListProps, SelectListProps } from './type';
import useRankingPeriodsList from './useRankingPeriodsList';
import { BsFillCaretDownFill } from 'react-icons/bs';
import styled, { css } from 'styled-components';

import { FlexBox } from 'components/@shared/FlexBox';

import { RankingPeriodItem } from 'components/RankingPeriodItem';

export const RankingPeriodsList = ({
  handleRankingPeriodId,
}: RankingPeriodsListProps) => {
  const {
    selectedPeriodIndex,
    showSelectBox,
    rankingPeriodsData,
    handleSelectBox,
    handleChooseRankingPeriod,
    startDateString,
    endDateString,
  } = useRankingPeriodsList({ handleRankingPeriodId });

  if (typeof rankingPeriodsData?.data === 'undefined') {
    return null;
  }

  return (
    <FlexBox justifyContent="center">
      <SelectPeriod role="button" onClick={handleSelectBox} alignItems="center">
        {startDateString} ~ {endDateString}&nbsp;
        <BsFillCaretDownFill />
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
  );
};

const SelectPeriod = styled(FlexBox)`
  ${({ theme }) => css`
    width: fit-content;
    height: 2.5rem;
    line-height: 2.5rem;
    font-weight: bold;
    font-size: 1.2rem;
    text-align: center;
    border-radius: 20px;
    background-color: ${theme.surface};
    color: ${theme.primary};
    padding: 0 0.8rem;
    position: relative;
    cursor: pointer;
  `}
`;

const SelectList = styled.ul`
  ${({ show }: SelectListProps) => css`
    display: ${!show && 'none'};
    width: fit-content;
    height: 10rem;
    overflow-y: scroll;
    position: absolute;
    top: 2.5rem;
    right: 0.5rem;
  `}
`;
