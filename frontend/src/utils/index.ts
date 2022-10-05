import { TIMEZONE_OFFSET } from 'constants/domain';

type addDaysFunction = (date: Date, days: number) => Date;

export const addDays: addDaysFunction = (date, days) => {
  const newDate = new Date(date.getTime());
  newDate.setDate(date.getDate() + days);
  return newDate;
};

export const getUrlParameter = (name: string) => {
  name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
  const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
  const results = regex.exec(window.location.search);
  return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
};

export const detectDarkMode = () => {
  const localTheme = localStorage.getItem('isDark');

  if (localTheme === null) {
    const browserTheme = window.matchMedia('(prefers-color-scheme: dark)').matches;
    return browserTheme;
  }

  return localTheme === 'true';
};

export const parseTime = (dateString: string) => {
  const parsedDate = new Date(Date.parse(dateString));
  const year = String(parsedDate.getFullYear());
  const month = String(parsedDate.getMonth() + 1);
  const date = String(parsedDate.getDate());
  const hours = String(parsedDate.getHours()).padStart(2, '0');
  const minutes = String(parsedDate.getMinutes()).padStart(2, '0');
  const seconds = String(parsedDate.getSeconds()).padStart(2, '0');

  return { year, month, date, hours, minutes, seconds };
};

export const urlB64ToUint8Array = (base64String: string) => {
  const padding = '='.repeat((4 - (base64String.length % 4)) % 4);
  const base64 = (base64String + padding).replace(/-/g, '+').replace(/_/g, '/');

  const rawData = atob(base64);
  const outputArray = new Uint8Array(rawData.length);

  for (let i = 0; i < rawData.length; i++) {
    outputArray[i] = rawData.charCodeAt(i);
  }
  return outputArray;
};

export const parseDateToISOString = (date: Date) => {
  const currentDate = date;
  currentDate.setHours(currentDate.getHours() + TIMEZONE_OFFSET);
  const [stringTypeDate, _] = currentDate.toISOString().split('.');
  return stringTypeDate;
};

export const dataURLtoBlob = (dataURL: string) => {
  // convert base64 to raw binary data held in a string
  // doesn't handle URLEncoded DataURIs - see SO answer #6850276 for code that does this
  const byteString = atob(dataURL.split(',')[1]);

  // separate out the mime component
  const mimeString = dataURL.split(',')[0].split(':')[1].split(';')[0];

  // write the bytes of the string to an ArrayBuffer
  const ab = new ArrayBuffer(byteString.length);

  // create a view into the buffer
  const ia = new Uint8Array(ab);

  // set the bytes of the buffer to the correct values
  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }

  // write the ArrayBuffer to a blob, and you're done
  const blob = new Blob([ab], { type: mimeString });
  return blob;
};

export const dateYMDFormatParsing = (dateString: string) => {
  const { year, month, date } = parseTime(dateString);
  return `${year}.${month}.${date}`;
};

export const getWeekNumber = (dateString: string) => {
  const dateFrom = new Date(Date.parse(dateString));
  // 해당 날짜 (일)
  const currentDate = dateFrom.getDate();
  // 이번 달 1일로 지정
  const startOfMonth = new Date(dateFrom.setDate(1));
  // 이번 달 1일이 무슨 요일인지 확인
  const weekDay = startOfMonth.getDay(); // 0: Sun ~ 6: Sat

  // ((요일 - 1) + 해당 날짜) / 7일로 나누기 = N 주차
  return parseInt(`${(weekDay - 1 + currentDate) / 7}`) + 1;
};

export const getCurrentStartDateString = () => {
  const currentDate = new Date();
  const weekDay = currentDate.getDay();
  return parseDateToISOString(addDays(currentDate, 1 - weekDay));
};
