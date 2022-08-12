export const challengeData = [
  {
    challengeId: 1,
    challengeName: '미라클 모닝',
    challengerCount: 35,
    isInProgress: true,
    emojiIndex: 1,
    colorIndex: 1,
  },
  {
    challengeId: 2,
    challengeName: '1일 1시간 공부',
    challengerCount: 60,
    isInProgress: false,
    emojiIndex: 2,
    colorIndex: 2,
  },
  {
    challengeId: 3,
    challengeName: '책읽기',
    challengerCount: 13,
    isInProgress: false,
    emojiIndex: 3,
    colorIndex: 3,
  },
  {
    challengeId: 4,
    challengeName: '일찍 잠자기',
    challengerCount: 80,
    isInProgress: false,
    emojiIndex: 4,
    colorIndex: 4,
  },
  {
    challengeId: 5,
    challengeName: '미라클 모닝',
    challengerCount: 35,
    isInProgress: false,
    emojiIndex: 1,
    colorIndex: 1,
  },
  {
    challengeId: 6,
    challengeName: '1일 1시간 공부',
    challengerCount: 60,
    isInProgress: false,
    emojiIndex: 2,
    colorIndex: 2,
  },
  {
    challengeId: 7,
    challengeName: '책읽기',
    emojiIndex: 3,
    colorIndex: 3,
    challengerCount: 13,
    isInProgress: false,
  },
  {
    challengeId: 8,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    challengerCount: 80,
    isInProgress: false,
  },
  {
    challengeId: 9,
    challengeName: '미라클 모닝',
    challengerCount: 35,
    isInProgress: false,
    emojiIndex: 1,
    colorIndex: 1,
  },
  {
    challengeId: 10,
    challengeName: '1일 1시간 공부',
    emojiIndex: 2,
    colorIndex: 2,
    challengerCount: 60,
    isInProgress: false,
  },
  {
    challengeId: 11,
    challengeName: '책읽기',
    emojiIndex: 3,
    colorIndex: 3,
    challengerCount: 13,
    isInProgress: false,
  },
  {
    challengeId: 12,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    challengerCount: 80,
    isInProgress: false,
  },
  {
    challengeId: 13,
    challengeName: '미라클 모닝',
    emojiIndex: 1,
    colorIndex: 1,
    challengerCount: 35,
    isInProgress: false,
  },
  {
    challengeId: 14,
    challengeName: '1일 1시간 공부',
    emojiIndex: 2,
    colorIndex: 2,
    challengerCount: 60,
    isInProgress: false,
  },
  {
    challengeId: 15,
    challengeName: '책읽기',
    emojiIndex: 3,
    colorIndex: 3,
    challengerCount: 13,
    isInProgress: false,
  },
  {
    challengeId: 16,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    challengerCount: 80,
    isInProgress: false,
  },
  {
    challengeId: 17,
    challengeName: '미라클 모닝',
    emojiIndex: 1,
    colorIndex: 1,
    challengerCount: 35,
    isInProgress: false,
  },
  {
    challengeId: 18,
    challengeName: '1일 1시간 공부',
    emojiIndex: 2,
    colorIndex: 2,
    challengerCount: 60,
    isInProgress: false,
  },
  {
    challengeId: 19,
    challengeName: '1일 1시간 공부',
    emojiIndex: 2,
    colorIndex: 2,
    challengerCount: 60,
    isInProgress: false,
  },

  {
    challengeId: 20,
    challengeName: '책읽기',
    emojiIndex: 3,
    colorIndex: 3,
    challengerCount: 13,
    isInProgress: false,
  },
  {
    challengeId: 21,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    challengerCount: 80,
    isInProgress: false,
  },
  {
    challengeId: 22,
    challengeName: '미라클 모닝',
    emojiIndex: 1,
    colorIndex: 1,
    challengerCount: 35,
    isInProgress: false,
  },
  {
    challengeId: 23,
    challengeName: '1일 1시간 공부',
    emojiIndex: 2,
    colorIndex: 2,
    challengerCount: 60,
    isInProgress: false,
  },
  {
    challengeId: 24,
    challengeName: '책읽기',
    emojiIndex: 3,
    colorIndex: 3,
    challengerCount: 13,
    isInProgress: false,
  },
  {
    challengeId: 25,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    challengerCount: 80,
    isInProgress: false,
  },
];

const date = new Date();
date.setHours(date.getHours() + 9);

const [startTime, _] = date.toISOString().split('.');

const twoDaysAgo = new Date();
twoDaysAgo.setHours(twoDaysAgo.getHours() + 9 - 48);
const [twoDaysAgoStartTime, __] = twoDaysAgo.toISOString().split('.');

export const cycleData = [
  {
    cycleId: 1,
    challengeId: 1,
    challengeName: '미라클 모닝',
    emojiIndex: 1,
    colorIndex: 1,
    progressCount: 2,
    startTime: twoDaysAgoStartTime,
    successCount: 3,
  },
  {
    cycleId: 2,
    challengeId: 2,
    challengeName: '운동',
    emojiIndex: 5,
    colorIndex: 5,
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 3,
    challengeId: 3,
    challengeName: '샤워',
    emojiIndex: 6,
    colorIndex: 6,
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 4,
    challengeId: 4,
    challengeName: '취침',
    emojiIndex: 4,
    colorIndex: 4,
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 5,
    challengeId: 5,
    challengeName: '화이팅',
    emojiIndex: 7,
    colorIndex: 7,
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 6,
    challengeId: 6,
    challengeName: '어제 오늘 내일은 뭐 먹지?',
    progressCount: 0,
    emojiIndex: 8,
    colorIndex: 8,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 7,
    challengeId: 7,
    challengeName: '마르코 화이팅',
    progressCount: 0,
    emojiIndex: 9,
    colorIndex: 9,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 8,
    challengeId: 8,
    challengeName: '우연 화이팅',
    progressCount: 0,
    emojiIndex: 10,
    colorIndex: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 9,
    challengeId: 9,
    challengeName: '빅터 화이팅',
    progressCount: 0,
    emojiIndex: 11,
    colorIndex: 1,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 10,
    challengeId: 10,
    challengeName: '우린 멋져',
    progressCount: 0,
    emojiIndex: 12,
    colorIndex: 2,
    startTime,
    successCount: 3,
  },
];

export const cycleByIdData = [
  {
    cycleId: 1,
    challengeId: 2,
    challengeName: '미라클 모닝',
    emojiIndex: 1,
    colorIndex: 1,
    progressCount: 3,
    startTime: '2022-07-01T17:00:00',
    successCount: 3,
    cycleDetails: [
      {
        progressImage: 'https://emojiIndex-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명1.',
      },
      {
        progressImage: 'https://emojiIndex-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명2.',
      },
      {
        progressImage: 'https://emojiIndex-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명3.',
      },
    ],
  },
  {
    cycleId: 2,
    challengeId: 3,
    challengeName: '운동',
    emojiIndex: 5,
    colorIndex: 5,
    progressCount: 2,
    startTime: '2022-07-01T17:00:00',
    successCount: 5,
    cycleDetails: [
      {
        progressImage: 'https://emojiIndex-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명1.',
      },
      {
        progressImage: 'https://emojiIndex-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명2.',
      },
    ],
  },
  {
    cycleId: 4,
    challengeId: 4,
    challengeName: '공부',
    emojiIndex: 2,
    colorIndex: 2,
    progressCount: 3,
    startTime: '2022-07-01T17:00:00',
    successCount: 3,
    cycleDetails: [
      {
        progressImage: 'https://emojiIndex-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명1.',
      },
      {
        progressImage: 'https://emojiIndex-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명2.',
      },
      {
        progressImage: 'https://emojiIndex-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명3.',
      },
    ],
  },
];

export const mySuccessChallengeData = [
  {
    challengeId: 1,
    challengeName: '미라클 모닝',
    emojiIndex: 1,
    colorIndex: 1,
    successCount: 3,
  },
  {
    challengeId: 2,
    challengeName: '1일 1시간 공부',
    emojiIndex: 2,
    colorIndex: 2,
    successCount: 2,
  },
  {
    challengeId: 3,
    challengeName: '책읽기',
    emojiIndex: 3,
    colorIndex: 3,
    successCount: 1,
  },
  {
    challengeId: 4,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    successCount: 100,
  },
  {
    challengeId: 5,
    challengeName: '미라클 모닝',
    emojiIndex: 1,
    colorIndex: 1,
    successCount: 3,
  },
  {
    challengeId: 6,
    challengeName: '1일 1시간 공부',
    emojiIndex: 2,
    colorIndex: 2,
    successCount: 2,
  },
  {
    challengeId: 7,
    challengeName: '책읽기',
    emojiIndex: 3,
    colorIndex: 3,
    successCount: 1,
  },
  {
    challengeId: 8,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    successCount: 100,
  },
  {
    challengeId: 9,
    challengeName: '미라클 모닝',
    emojiIndex: 1,
    colorIndex: 1,
    successCount: 3,
  },
  {
    challengeId: 10,
    challengeName: '1일 1시간 공부',
    emojiIndex: 2,
    colorIndex: 2,
    successCount: 2,
  },
  {
    challengeId: 11,
    challengeName: '책읽기',
    emojiIndex: 3,
    colorIndex: 3,
    successCount: 1,
  },
  {
    challengeId: 12,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    successCount: 100,
  },
  {
    challengeId: 13,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    successCount: 100,
  },
  {
    challengeId: 14,
    challengeName: '미라클 모닝',
    emojiIndex: 1,
    colorIndex: 1,
    successCount: 3,
  },
  {
    challengeId: 15,
    challengeName: '1일 1시간 공부',
    emojiIndex: 2,
    colorIndex: 2,
    successCount: 2,
  },
  {
    challengeId: 16,
    challengeName: '책읽기',
    emojiIndex: 3,
    colorIndex: 3,
    successCount: 1,
  },
  {
    challengeId: 17,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    successCount: 100,
  },
  {
    challengeId: 18,
    challengeName: '미라클 모닝',
    emojiIndex: 1,
    colorIndex: 1,
    successCount: 3,
  },
  {
    challengeId: 19,
    challengeName: '1일 1시간 공부',
    emojiIndex: 2,
    colorIndex: 2,
    successCount: 2,
  },
  {
    challengeId: 20,
    challengeName: '책읽기',
    emojiIndex: 3,
    colorIndex: 3,
    successCount: 1,
  },
  {
    challengeId: 21,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    successCount: 100,
  },
  {
    challengeId: 22,
    challengeName: '미라클 모닝',
    emojiIndex: 1,
    colorIndex: 1,
    successCount: 3,
  },
  {
    challengeId: 23,
    challengeName: '1일 1시간 공부',
    emojiIndex: 2,
    colorIndex: 2,
    successCount: 2,
  },
  {
    challengeId: 24,
    challengeName: '책읽기',
    emojiIndex: 3,
    colorIndex: 3,
    successCount: 1,
  },
  {
    challengeId: 25,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    successCount: 100,
  },
  {
    challengeId: 26,
    challengeName: '미라클 모닝',
    emojiIndex: 1,
    colorIndex: 1,
    successCount: 3,
  },
  {
    challengeId: 27,
    challengeName: '1일 1시간 공부',
    emojiIndex: 2,
    colorIndex: 2,
    successCount: 2,
  },
  {
    challengeId: 28,
    challengeName: '책읽기',
    emojiIndex: 3,
    colorIndex: 3,
    successCount: 1,
  },
  {
    challengeId: 29,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    successCount: 100,
  },
  {
    challengeId: 30,
    challengeName: '미라클 모닝',
    emojiIndex: 1,
    colorIndex: 1,
    successCount: 3,
  },
  {
    challengeId: 31,
    challengeName: '1일 1시간 공부',
    emojiIndex: 2,
    colorIndex: 2,
    successCount: 2,
  },
  {
    challengeId: 32,
    challengeName: '책읽기',
    emojiIndex: 3,
    colorIndex: 3,
    successCount: 1,
  },
  {
    challengeId: 33,
    challengeName: '일찍 잠자기',
    emojiIndex: 4,
    colorIndex: 4,
    successCount: 100,
  },
];

export const userData = {
  nickname: '테스트닉네임',
  introduction: '테스트 자기소개',
  picture: 'https://src.hidoc.co.kr/image/lib/2022/5/4/1651651323632_0.jpg',
  email: 'test@gmail.com',
};

export const accessTokenData = 'testAccessToken';

export const cycleDetailData = {
  cycleId: 1,
  challengeId: 2,
  challengeName: '미라클 모닝',
  emojiIndex: 1,
  colorIndex: 1,
  progressCount: 2,
  startTime: '2022-07-01T17:00:00',
  successCount: 3,
  cycleDetails: [
    {
      progressImage:
        'https://health.chosun.com/site/data/img_dir/2018/03/07/2018030700812_2.jpg',
      progressTime: '2022-07-01T17:00:00',
      description: '알찼다.',
    },
    {
      progressImage:
        'https://health.chosun.com/site/data/img_dir/2018/03/07/2018030700812_2.jpg',
      progressTime: '2022-07-02T17:00:00',
      description: '알찼다.',
    },
    {
      progressImage:
        'https://health.chosun.com/site/data/img_dir/2018/03/07/2018030700812_2.jpg',
      progressTime: '2022-07-03T17:00:00',
      description: '알찼다.',
    },
  ],
};

export const cycleNonDetailData = {
  cycleId: 1,
  challengeId: 2,
  challengeName: '미라클 모닝',
  emojiIndex: 1,
  colorIndex: 1,
  progressCount: 2,
  startTime: '2022-07-01T17:00:00',
  successCount: 3,
  cycleDetails: [],
};

export const challengers = [
  {
    memberId: 1,
    nickName: '챌린저1',
    progressCount: 0,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 2,
    nickName: '챌린저2',
    progressCount: 1,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 3,
    nickName: '챌린저3',
    progressCount: 2,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 4,
    nickName: '챌린저4',
    progressCount: 0,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 5,
    nickName: '챌린저5',
    progressCount: 1,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },

  {
    memberId: 6,
    nickName: '챌린저6',
    progressCount: 0,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 7,
    nickName: '챌린저7',
    progressCount: 0,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 8,
    nickName: '챌린저8',
    progressCount: 0,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 9,
    nickName: '챌린저9',
    progressCount: 0,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 10,
    nickName: '챌린저10',
    progressCount: 0,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 11,
    nickName: '챌린저11',
    progressCount: 0,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 12,
    nickName: '챌린저12',
    progressCount: 1,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 13,
    nickName: '챌린저13',
    progressCount: 2,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 14,
    nickName: '챌린저14',
    progressCount: 0,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 15,
    nickName: '챌린저15',
    progressCount: 1,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },

  {
    memberId: 16,
    nickName: '챌린저16',
    progressCount: 0,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 17,
    nickName: '챌린저17',
    progressCount: 0,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 18,
    nickName: '챌린저18',
    progressCount: 0,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
  {
    memberId: 19,
    nickName: '챌린저19',
    progressCount: 0,
    picture: 'https://i.pinimg.com/736x/2c/2c/60/2c2c60b20cb817a80afd381ae23dab05.jpg',
    introduction: '안녕하세요',
  },
];

export const feedData = [
  {
    cycleDetailId: 1,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://i.ibb.co/mJwjFq1/progress-Image.png',
    description: '바다가 너무 예쁘네요^_^',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[0].challengeId,
    challengeName: challengeData[0].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 2,
    memberId: 1,
    picture: userData.picture,
    nickname: userData.nickname,
    progressImage:
      'https://cdn.pixabay.com/photo/2019/07/21/13/42/nature-conservation-4352793_1280.jpg',
    description: '오늘 날씨가 너무 좋아요~!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[0].challengeId,
    challengeName: challengeData[0].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 3,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2016/11/18/16/56/book-1835799_1280.jpg',
    description: '오늘도 독서 완료!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[2].challengeId,
    challengeName: challengeData[2].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 4,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage:
      'https://cdn.pixabay.com/photo/2015/07/13/16/49/night-table-lamp-843461_1280.jpg',
    description: '오늘 일찍 잠~~',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[3].challengeId,
    challengeName: challengeData[3].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 5,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2015/07/28/22/05/child-865116_1280.jpg',
    description: '오늘도 열심히 공부했어요~~!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[1].challengeId,
    challengeName: challengeData[1].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 6,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2015/03/09/18/34/beach-666122_1280.jpg',
    description: '바다가 너무 예쁘네요^_^',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[0].challengeId,
    challengeName: challengeData[0].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 7,
    memberId: 1,
    picture: userData.picture,
    nickname: userData.nickname,
    progressImage:
      'https://cdn.pixabay.com/photo/2019/07/21/13/42/nature-conservation-4352793_1280.jpg',
    description: '오늘 날씨가 너무 좋아요~!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[0].challengeId,
    challengeName: challengeData[0].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 8,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2016/11/18/16/56/book-1835799_1280.jpg',
    description: '오늘도 독서 완료!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[2].challengeId,
    challengeName: challengeData[2].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 9,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage:
      'https://cdn.pixabay.com/photo/2015/07/13/16/49/night-table-lamp-843461_1280.jpg',
    description: '오늘 일찍 잠~~',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[3].challengeId,
    challengeName: challengeData[3].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 10,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2015/07/28/22/05/child-865116_1280.jpg',
    description: '오늘도 열심히 공부했어요~~!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[1].challengeId,
    challengeName: challengeData[1].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 11,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2015/03/09/18/34/beach-666122_1280.jpg',
    description: '바다가 너무 예쁘네요^_^',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[0].challengeId,
    challengeName: challengeData[0].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 12,
    memberId: 1,
    picture: userData.picture,
    nickname: userData.nickname,
    progressImage:
      'https://cdn.pixabay.com/photo/2019/07/21/13/42/nature-conservation-4352793_1280.jpg',
    description: '오늘 날씨가 너무 좋아요~!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[0].challengeId,
    challengeName: challengeData[0].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 13,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2016/11/18/16/56/book-1835799_1280.jpg',
    description: '오늘도 독서 완료!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[2].challengeId,
    challengeName: challengeData[2].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 14,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage:
      'https://cdn.pixabay.com/photo/2015/07/13/16/49/night-table-lamp-843461_1280.jpg',
    description: '오늘 일찍 잠~~',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[3].challengeId,
    challengeName: challengeData[3].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 15,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2015/07/28/22/05/child-865116_1280.jpg',
    description: '오늘도 열심히 공부했어요~~!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[1].challengeId,
    challengeName: challengeData[1].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 16,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2015/03/09/18/34/beach-666122_1280.jpg',
    description: '바다가 너무 예쁘네요^_^',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[0].challengeId,
    challengeName: challengeData[0].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 17,
    memberId: 1,
    picture: userData.picture,
    nickname: userData.nickname,
    progressImage:
      'https://cdn.pixabay.com/photo/2019/07/21/13/42/nature-conservation-4352793_1280.jpg',
    description: '오늘 날씨가 너무 좋아요~!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[0].challengeId,
    challengeName: challengeData[0].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 18,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2016/11/18/16/56/book-1835799_1280.jpg',
    description: '오늘도 독서 완료!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[2].challengeId,
    challengeName: challengeData[2].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 19,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage:
      'https://cdn.pixabay.com/photo/2015/07/13/16/49/night-table-lamp-843461_1280.jpg',
    description: '오늘 일찍 잠~~',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[3].challengeId,
    challengeName: challengeData[3].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 20,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2015/07/28/22/05/child-865116_1280.jpg',
    description: '오늘도 열심히 공부했어요~~!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[1].challengeId,
    challengeName: challengeData[1].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 21,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2015/03/09/18/34/beach-666122_1280.jpg',
    description: '바다가 너무 예쁘네요^_^',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[0].challengeId,
    challengeName: challengeData[0].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 22,
    memberId: 1,
    picture: userData.picture,
    nickname: userData.nickname,
    progressImage:
      'https://cdn.pixabay.com/photo/2019/07/21/13/42/nature-conservation-4352793_1280.jpg',
    description: '오늘 날씨가 너무 좋아요~!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[0].challengeId,
    challengeName: challengeData[0].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 23,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2016/11/18/16/56/book-1835799_1280.jpg',
    description: '오늘도 독서 완료!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[2].challengeId,
    challengeName: challengeData[2].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 24,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage:
      'https://cdn.pixabay.com/photo/2015/07/13/16/49/night-table-lamp-843461_1280.jpg',
    description: '오늘 일찍 잠~~',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[3].challengeId,
    challengeName: challengeData[3].challengeName,
    commentCount: 4,
  },
  {
    cycleDetailId: 25,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    progressImage: 'https://cdn.pixabay.com/photo/2015/07/28/22/05/child-865116_1280.jpg',
    description: '오늘도 열심히 공부했어요~~!',
    progressTime: '2022-08-08T10:00:00',
    challengeId: challengeData[1].challengeId,
    challengeName: challengeData[1].challengeName,
    commentCount: 4,
  },
];

export const commentData = [
  {
    commentId: 1,
    memberId: 1,
    picture: userData.picture,
    nickname: userData.nickname,
    content:
      '와우 대단한데요~~! 많은 자극 받고 갑니다ㅎㅎ 저도 내일부터 다시 도전해 봐야겠어요!!와우 대단한데요~~! 많은 자극 받고 갑니다ㅎㅎ 저도 내일부터 다시 도전해 봐야겠어요!!와우 대단한데요~~! 많은 자극 받고 갑니다ㅎㅎ 저도 내일부터 다시 도전해 봐야겠어요!!와우 대단한데요~~! 많은 자극 받고 갑니다ㅎㅎ 저도 내일부터 다시 도전해 봐야겠어요!!와우 대단한데요~~! 많은 자극 받고 갑니다ㅎㅎ 저도 내일부터 다시 도전해 봐야겠어요!!와우 대단한데요~~! 많은 자극 받고 갑니다ㅎㅎ 저도 내일부터 다시 도전해 봐야겠어요!!와우 대단한데요~~! 많은 자극 받고 갑니다ㅎㅎ 저도 내일부터 다시 도전해 봐야겠어요!!와우 대단한데요~~! 많은 자극 받고 갑니다ㅎㅎ 저도 내일부터 다시 도전해 봐야겠어요!!',
    createdAt: '2022-08-08T10:00:00',
  },
  {
    commentId: 2,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    content:
      'sadfasdfasfsdfsafdsfdsfsafsadfasdfasfsdfsafdsfdsfsafsadfasdfasfsdfsafdsfdsfsafsadfasdfasfsdfsafdsfdsfsafsadfasdfasfsdfsafdsfdsfsafsadfasdfasfsdfsafdsfdsfsafsadfasdfasfsdfsafdsfdsfsafsadfasdfasfsdfsafdsfdsfsafsadfasdfasfsdfsafdsfdsfsafsadfasdfasfsdfsafdsf',
    createdAt: '2022-08-08T10:00:00',
  },
  {
    commentId: 3,
    memberId: 1,
    picture: userData.picture,
    nickname: userData.nickname,
    content:
      '와우 대단한데요~~! 많은 자극 받고 갑니다ㅎㅎ 저도 내일부터 다시 도전해 봐야겠어요!!',
    createdAt: '2022-08-08T10:00:00',
  },
  {
    commentId: 4,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    content: '꾸준히 도전하시는 모습이 멋있네요!! 응원하겠습니다~!',
    createdAt: '2022-08-08T10:00:00',
  },
  {
    commentId: 5,
    memberId: 1,
    picture: userData.picture,
    nickname: userData.nickname,
    content:
      '와우 대단한데요~~! 많은 자극 받고 갑니다ㅎㅎ 저도 내일부터 다시 도전해 봐야겠어요!!',
    createdAt: '2022-08-08T10:00:00',
  },
  {
    commentId: 6,
    memberId: 2,
    picture:
      'https://cdn.pixabay.com/photo/2022/08/03/04/41/beech-leaves-7361863_1280.jpg',
    nickname: '유저1',
    content: '꾸준히 도전하시는 모습이 멋있네요!! 응원하겠습니다~!',
    createdAt: '2022-08-08T10:00:00',
  },
];

export const db = [
  { nickname: 'marco', subscription: {} },
  { nickname: 'does', subscription: { endpoint: 'test' } },
];
