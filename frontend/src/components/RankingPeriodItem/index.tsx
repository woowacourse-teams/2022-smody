import { RankingPeriodItemProps, SelectListItemProps } from './type';
import useRankingPeriodItem from './useRankingPeriodItem';
import styled, { css } from 'styled-components';

export const RankingPeriodItem = ({
  rankingPeriodId,
  startDate,
  duration,
  selected,
  handleChooseRankingPeriod,
}: RankingPeriodItemProps) => {
  const { startDateString, endDateString } = useRankingPeriodItem({
    startDate,
    duration,
  });

  return (
    <SelectListItem selected={selected}>
      <button onClick={handleChooseRankingPeriod}>
        {startDateString} ~ {endDateString}
      </button>
    </SelectListItem>
  );
};

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
