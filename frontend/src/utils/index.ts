import { TIMEZONE_OFFSET } from 'constants/domain';

type AddDaysFunction = (date: Date, days: number) => Date;

export const addDays: AddDaysFunction = (date, days) => {
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

export const getCursorPosition = (element: HTMLElement) => {
  const isNotSupported = typeof window.getSelection === 'undefined';

  if (isNotSupported) {
    return;
  }

  const selection = window.getSelection();

  if (selection === null) {
    return;
  }

  const range = selection.getRangeAt(0);
  const preSelectionRange = range.cloneRange();
  preSelectionRange.selectNodeContents(element);
  preSelectionRange.setEnd(range.startContainer, range.startOffset);
  const selectionStart = preSelectionRange.toString().length;

  return selectionStart;
};

export const insertAfter = (referenceNode: Node, newNode: Node) => {
  if (referenceNode.nextSibling) {
    referenceNode.parentNode!.insertBefore(newNode, referenceNode.nextSibling);
  } else {
    referenceNode.parentNode!.appendChild(newNode);
  }
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
  const startData =
    weekDay === 0 ? addDays(currentDate, -6) : addDays(currentDate, 1 - weekDay);

  return parseDateToISOString(startData);
};

type IsSameDateFunction = (dateString1: string, dateString2: string) => boolean;

export const isSameDate: IsSameDateFunction = (dateString1, dateString2) => {
  const { year: year1, month: month1, date: date1 } = parseTime(dateString1);
  const { year: year2, month: month2, date: date2 } = parseTime(dateString2);
  return year1 === year2 && month1 === month2 && date1 === date2;
};

export const getCookie = (name: string): string | undefined => {
  const matches = document.cookie.match(new RegExp(`${name}=([^;]*)`));
  return matches ? decodeURIComponent(matches[1]) : undefined;
};

export const setCookie = (name: string, value: string, maxAge: number) => {
  document.cookie = `${encodeURIComponent(name)}=${encodeURIComponent(
    value,
  )}; max-age=${maxAge}`;
};

export const isIOS =
  (/iPad|iPhone|iPod/.test(navigator.platform) ||
    (navigator.platform === 'MacIntel' && navigator.maxTouchPoints > 1)) &&
  !window.MSStream;
