import { feedData } from 'mocks/data';
import { rest } from 'msw';

import { BASE_URL } from 'constants/path';

export const feed = [
  rest.get(`${BASE_URL}/feeds`, (req, res, ctx) => {
    return res(ctx.delay(2000), ctx.status(200), ctx.json(feedData));
  }),
];
