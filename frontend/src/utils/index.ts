type addDaysFunction = (date: Date, days: number) => Date;
type parseTimeFunction = (date: Date) => string;

export const addDays: addDaysFunction = (date, days) => {
  const newDate = date;
  newDate.setDate(date.getDate() + days);
  return newDate;
};

export const parseTime: parseTimeFunction = (date) => {
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  return `${year}.${month}.${day}`;
};
