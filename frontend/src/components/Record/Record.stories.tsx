import { Record } from './';
import { RecordProps } from './type';

export default {
  title: 'components/Record',
  component: Record,
};

export const SuccessRecord = (args: RecordProps) => <Record {...args} />;

SuccessRecord.args = {
  cycleId: 3,
  emojiIndex: 0,
  colorIndex: 1,
  startTime: '2022-07-01T17:00:00',
  cycleDetails: [
    {
      cycleDetailId: 1,
      progressImage:
        'https://post-phinf.pstatic.net/MjAyMDA2MDlfMjEw/MDAxNTkxNjk1MTM4OTI4.LvldwiT0_pjrP8xtQeJUifXhtvO4WFXeEz6xwJmz0J8g.k9LyAkihXYNAGlO-LNG2aDh4MHT1uEw7jdZmssrRddsg.JPEG/documentTitle_8515930031591694382355.jpg?type=w1200',
    },
    {
      cycleDetailId: 2,
      progressImage:
        'https://post-phinf.pstatic.net/MjAyMDA2MDlfMjEw/MDAxNTkxNjk1MTM4OTI4.LvldwiT0_pjrP8xtQeJUifXhtvO4WFXeEz6xwJmz0J8g.k9LyAkihXYNAGlO-LNG2aDh4MHT1uEw7jdZmssrRddsg.JPEG/documentTitle_8515930031591694382355.jpg?type=w1200',
    },
    {
      cycleDetailId: 3,
      progressImage:
        'https://post-phinf.pstatic.net/MjAyMDA2MDlfMjEw/MDAxNTkxNjk1MTM4OTI4.LvldwiT0_pjrP8xtQeJUifXhtvO4WFXeEz6xwJmz0J8g.k9LyAkihXYNAGlO-LNG2aDh4MHT1uEw7jdZmssrRddsg.JPEG/documentTitle_8515930031591694382355.jpg?type=w1200',
    },
  ],
};

export const FailedRecord = (args: RecordProps) => <Record {...args} />;

FailedRecord.args = {
  cycleId: 3,
  emojiIndex: 0,
  colorIndex: 1,
  startTime: '2022-07-01T17:00:00',
  cycleDetails: [
    {
      cycleDetailId: 1,
      progressImage:
        'https://post-phinf.pstatic.net/MjAyMDA2MDlfMjEw/MDAxNTkxNjk1MTM4OTI4.LvldwiT0_pjrP8xtQeJUifXhtvO4WFXeEz6xwJmz0J8g.k9LyAkihXYNAGlO-LNG2aDh4MHT1uEw7jdZmssrRddsg.JPEG/documentTitle_8515930031591694382355.jpg?type=w1200',
    },
    {
      cycleDetailId: 2,
      progressImage:
        'https://post-phinf.pstatic.net/MjAyMDA2MDlfMjEw/MDAxNTkxNjk1MTM4OTI4.LvldwiT0_pjrP8xtQeJUifXhtvO4WFXeEz6xwJmz0J8g.k9LyAkihXYNAGlO-LNG2aDh4MHT1uEw7jdZmssrRddsg.JPEG/documentTitle_8515930031591694382355.jpg?type=w1200',
    },
  ],
};
