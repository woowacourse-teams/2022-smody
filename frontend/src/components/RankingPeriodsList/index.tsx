import { SelectListProps } from './type';
import { useGetRankingPeriods } from 'apis/rankingApi';
import { useState } from 'react';
import { BsFillCaretDownFill } from 'react-icons/bs';
import styled, { css } from 'styled-components';
import { addDays, dateYMDFormatParsing } from 'utils';

import { FlexBox } from 'components/@shared/FlexBox';

import { RankingPeriodItem } from 'components/RankingPeriodItem';

import { RANKING_DURATION } from 'constants/domain';

export const RankingPeriodsList = () => {
  const [selectedPeriodIndex, setSelectedPeriodIndex] = useState(0);
  const [showSelectBox, setShowSelectBox] = useState(false);
  const { data: rankingPeriodsData } = useGetRankingPeriods();

  const handleSelectBox = () => {
    setShowSelectBox((prev) => !prev);
  };

  const handleChooseRankingPeriod = (id: number) => {
    setSelectedPeriodIndex(id);
    handleSelectBox();
  };

  if (typeof rankingPeriodsData?.data === 'undefined') {
    return null;
  }

  const { startDate, duration } = rankingPeriodsData.data[selectedPeriodIndex];

  const startDateString = dateYMDFormatParsing(startDate);
  const endDateString = dateYMDFormatParsing(
    String(addDays(new Date(Date.parse(startDate)), RANKING_DURATION[duration])),
  );

  return (
    <div>
      <SelectPeriod as="button" onClick={handleSelectBox} alignItems="center">
        {startDateString} ~ {endDateString}&nbsp;
        <BsFillCaretDownFill />
      </SelectPeriod>
      <SelectList show={showSelectBox}>
        {rankingPeriodsData.data.map((rankingPeriod, index) => (
          <RankingPeriodItem
            key={rankingPeriod.rankingPeriodId}
            selected={selectedPeriodIndex === index}
            handleChooseRankingPeriod={() => handleChooseRankingPeriod(index)}
            {...rankingPeriod}
          />
        ))}
      </SelectList>
    </div>
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
  `}
`;

const SelectList = styled.ul`
  ${({ show }: SelectListProps) => css`
    display: ${!show && 'none'};
    width: fit-content;
    height: 10rem;
    overflow-y: scroll;
    position: relative;
    right: -0.5rem;
  `}
`;
