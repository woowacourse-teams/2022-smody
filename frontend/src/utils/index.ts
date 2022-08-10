type addDaysFunction = (date: Date, days: number) => Date;

export const addDays: addDaysFunction = (date, days) => {
  const newDate = date;
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
