import { BASE_URL } from 'env';
import { accessTokenData, userData, feedData, commentData } from 'mocks/data';
import { rest } from 'msw';

const getNowTime = () => {
  const date = new Date();
  date.setHours(date.getHours() + 9);

  const [nowTime, _] = date.toISOString().split('.');

  return nowTime;
};

export const feed = [
  // 1. 피드 전체 조회(GET)
  rest.get(`${BASE_URL}/feeds`, (req, res, ctx) => {
    return res(ctx.delay(2000), ctx.status(200), ctx.json(feedData));
  }),
  // 2. id로 피드 조회(GET)
  rest.get(`${BASE_URL}/feeds/:cycleDetailId`, (req, res, ctx) => {
    const { cycleDetailId } = req.params;

    if (Number.isNaN(cycleDetailId) || cycleDetailId > feedData.length) {
      return res(
        ctx.status(404),
        ctx.json({
          code: 4002,
          message: '존재하지 않는 챌린지입니다.',
        }),
      );
    }

    return res(ctx.status(200), ctx.json(feedData[cycleDetailId - 1]));
  }),
  // 3. 댓글 생성(POST)
  rest.post(`${BASE_URL}/feeds/:cycleDetailId/comments`, (req, res, ctx) => {
    console.log('댓글 생성 API의 content: ', req.body);
    const { content } = req.body;
    const { authorization } = req.headers.headers;

    const accessToken = authorization.split(' ')[1];

    if (accessToken !== accessTokenData) {
      return res(
        ctx.status(403),
        ctx.json({
          code: 2002,
          message: '유효하지 않은 토큰입니다.',
        }),
      );
    }

    const newCommentData = {
      commentId: commentData.length + 1,
      memberId: 1,
      nickname: userData.nickname,
      picture: userData.picture,
      content,
      createdAt: getNowTime(),
    };

    commentData.push(newCommentData);

    return res(ctx.delay(2000), ctx.status(201));
  }),
  // 4. 댓글 조회(GET) - 비회원용
  rest.get(`${BASE_URL}/feeds/:cycleDetailId/comments`, (req, res, ctx) => {
    const { cycleDetailId } = req.params;

    if (Number.isNaN(cycleDetailId) || cycleDetailId > feedData.length) {
      return res(
        ctx.status(404),
        ctx.json({
          code: 4002,
          message: '존재하지 않는 챌린지입니다.',
        }),
      );
    }

    return res(ctx.status(200), ctx.json(commentData));
  }),
  // 4. 댓글 조회(GET) - 회원용
  rest.get(`${BASE_URL}/feeds/:cycleDetailId/comments/auth`, (req, res, ctx) => {
    const { cycleDetailId } = req.params;
    const { authorization } = req.headers.headers;

    const accessToken = authorization.split(' ')[1];

    if (Number.isNaN(cycleDetailId) || cycleDetailId > feedData.length) {
      return res(
        ctx.status(404),
        ctx.json({
          code: 4002,
          message: '존재하지 않는 챌린지입니다.',
        }),
      );
    }

    if (accessToken !== accessTokenData) {
      return res(
        ctx.status(403),
        ctx.json({
          code: 2002,
          message: '유효하지 않은 토큰입니다.',
        }),
      );
    }

    return res(ctx.status(200), ctx.json(commentData));
  }),
];
