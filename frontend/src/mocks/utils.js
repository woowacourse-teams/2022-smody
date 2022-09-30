import { accessTokenData } from './data';

export const checkValidAccessToken = (req) => {
  const authorization = req.headers.get('authorization');
  const accessToken = authorization.split(' ')[1];

  return accessToken === accessTokenData;
};
