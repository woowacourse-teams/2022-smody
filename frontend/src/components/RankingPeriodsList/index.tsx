import { RankingPeriodItemProps, SelectListItemProps, SelectListProps } from './type';
import { useGetRankingPeriods } from 'apis/rankingApi';
import { useState } from 'react';
import { BsFillCaretDownFill } from 'react-icons/bs';
import styled, { css } from 'styled-components';
import { addDays, parseTime } from 'utils';

import { FlexBox } from 'components/@shared/FlexBox';

import { RANKING_DURATION } from 'constants/domain';

const dateFormatParsing = (dateString: string) => {
  const { year, month, date } = parseTime(dateString);
  return `${year}.${month}.${date}`;
};

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

  const startDateString = dateFormatParsing(startDate);
  const endDateString = dateFormatParsing(
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

const RankingPeriodItem = ({
  rankingPeriodId,
  startDate,
  duration,
  selected,
  handleChooseRankingPeriod,
}: RankingPeriodItemProps) => {
  const startDateString = dateFormatParsing(startDate);
  const endDateString = dateFormatParsing(
    String(addDays(new Date(Date.parse(startDate)), RANKING_DURATION[duration])),
  );

  return (
    <SelectListItem selected={selected} onClick={handleChooseRankingPeriod}>
      <button>
        {startDateString} ~ {endDateString}
      </button>
    </SelectListItem>
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
    /* border-radius: 10px; */
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

const SelectListItem = styled.li<SelectListItemProps>`
  ${({ theme, selected }) => css`
    width: fit-content;
    height: 2.5rem;
    line-height: 2.5rem;
    text-align: center;
    background-color: ${theme.surface};
    border: 1px solid ${theme.border};
    button {
      font-size: 1.2rem;
      ${
        selected &&
        css`
          color: ${theme.primary};
        `
      }
  `}
`;
