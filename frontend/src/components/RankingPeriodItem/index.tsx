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
  const { detailDateString } = useRankingPeriodItem({
    startDate,
    duration,
  });

  return (
    <SelectListItem selected={selected} onClick={handleChooseRankingPeriod}>
      <button type="button">{detailDateString}</button>
    </SelectListItem>
  );
};

const SelectListItem = styled.li<SelectListItemProps>`
  ${({ theme, selected }) => css`
    width: 13rem;
    height: 2.5rem;
    line-height: 2.5rem;
    text-align: center;
    background-color: ${theme.surface};
    border: 1px solid ${theme.border};

    &:hover {
      background-color: ${theme.primary};
      button {
        color: ${theme.onPrimary};
      }
    }

    button {
      font-size: 1rem;
      color: ${theme.onSurface};
      ${selected &&
      css`
        color: ${theme.primary};
      `}
    }
  `}
`;
