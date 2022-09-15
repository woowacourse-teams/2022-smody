import { userData, feedData, commentData } from 'mocks/data';
import { checkValidAccessToken } from 'mocks/utils';
import { rest } from 'msw';

const getNowTime = () => {
  const date = new Date();
  date.setHours(date.getHours() + 9);

  const [nowTime, _] = date.toISOString().split('.');

  return nowTime;
};

export const feed = [
  // 1. 피드 전체 조회(GET)
  rest.get(`${process.env.BASE_URL}/feeds`, (req, res, ctx) => {
    return res(ctx.delay(2000), ctx.status(200), ctx.json(feedData));
  }),
  // 2. id로 피드 조회(GET)
  rest.get(`${process.env.BASE_URL}/feeds/:cycleDetailId`, (req, res, ctx) => {
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
  rest.post(`${process.env.BASE_URL}/feeds/:cycleDetailId/comments`, (req, res, ctx) => {
    const { content } = req.body;

    if (!checkValidAccessToken(req)) {
      return res(ctx.status(403), ctx.json({ code: 2002 }));
    }

    const newCommentData = {
      commentId: commentData.length + 1,
      memberId: 1,
      nickname: userData.nickname,
      picture: userData.picture,
      content,
      createdAt: getNowTime(),
      isMyComment: true,
    };

    commentData.push(newCommentData);

    return res(ctx.delay(2000), ctx.status(201));
  }),
  // 4. 댓글 조회(GET) - 비회원용
  rest.get(`${process.env.BASE_URL}/feeds/:cycleDetailId/comments`, (req, res, ctx) => {
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
  rest.get(
    `${process.env.BASE_URL}/feeds/:cycleDetailId/comments/auth`,
    (req, res, ctx) => {
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

      if (!checkValidAccessToken(req)) {
        return res(ctx.status(403), ctx.json({ code: 2002 }));
      }

      return res(ctx.status(200), ctx.json(commentData));
    },
  ),
  // 5. 댓글 수정
  rest.patch(`${process.env.BASE_URL}/comments/:commentId`, (req, res, ctx) => {
    const { commentId: targetCommentIdString } = req.params;
    const { content } = req.body;

    const targetCommentId = Number(targetCommentIdString);
    const commentDataIndex = commentData.findIndex(
      ({ commentId }) => commentId === targetCommentId,
    );

    if (Number.isNaN(targetCommentId) || commentDataIndex === -1) {
      return res(
        ctx.status(404),
        ctx.json({
          code: 4002,
          message: '존재하지 않는 챌린지입니다.',
        }),
      );
    }

    if (!checkValidAccessToken(req)) {
      return res(ctx.status(403), ctx.json({ code: 2002 }));
    }

    commentData[commentDataIndex] = { ...commentData[commentDataIndex], content };

    return res(ctx.delay(2000), ctx.status(204));
  }),
  // 6. 댓글 삭제
  rest.delete(`${process.env.BASE_URL}/comments/:commentId`, (req, res, ctx) => {
    const { commentId: targetCommentIdString } = req.params;

    const targetCommentId = Number(targetCommentIdString);
    const commentDataIndex = commentData.findIndex(
      ({ commentId }) => commentId === targetCommentId,
    );

    if (Number.isNaN(targetCommentId) || commentDataIndex === -1) {
      return res(
        ctx.status(404),
        ctx.json({
          code: 4002,
          message: '존재하지 않는 챌린지입니다.',
        }),
      );
    }

    if (!checkValidAccessToken(req)) {
      return res(ctx.status(403), ctx.json({ code: 2002 }));
    }

    commentData.splice(commentDataIndex, 1);

    return res(ctx.delay(2000), ctx.status(204));
  }),
];
