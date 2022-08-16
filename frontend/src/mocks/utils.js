import { accessTokenData } from './data';

export const checkValidAccessToken = (req) => {
  const { authorization } = req.headers.headers;
  const accessToken = authorization.split(' ')[1];

  return accessToken === accessTokenData;
};
