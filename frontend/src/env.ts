export const BASE_URL = process.env.BASE_URL ?? 'http://localhost:3000';

export const isLocal = process.env.IS_LOCAL;

export const isDev = process.env.NODE_ENV === 'development' && !process.env.IS_LOCAL;

export const isProd = process.env.NODE_ENV === 'production';
