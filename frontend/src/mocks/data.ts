export const challengeData = [
  {
    challengeId: 1,
    challengeName: '미라클 모닝',
    challengerCount: 35,
    isInProgress: true,
    emoji: '🌄',
  },
  {
    challengeId: 2,
    challengeName: '1일 1시간 공부',
    challengerCount: 60,
    isInProgress: false,
    emoji: '✏',
  },
  {
    challengeId: 3,
    challengeName: '책읽기',
    challengerCount: 13,
    isInProgress: false,
    emoji: '📚',
  },
  {
    challengeId: 4,
    challengeName: '일찍 잠자기',
    challengerCount: 80,
    isInProgress: false,
    emoji: '🕙',
  },
  {
    challengeId: 5,
    challengeName: '미라클 모닝',
    challengerCount: 35,
    isInProgress: false,
    emoji: '🌄',
  },
  {
    challengeId: 6,
    challengeName: '1일 1시간 공부',
    challengerCount: 60,
    isInProgress: false,
    emoji: '✏',
  },
  {
    challengeId: 7,
    challengeName: '책읽기',
    emoji: '📚',
    challengerCount: 13,
    isInProgress: false,
  },
  {
    challengeId: 8,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
    challengerCount: 80,
    isInProgress: false,
  },
  {
    challengeId: 9,
    challengeName: '미라클 모닝',
    challengerCount: 35,
    isInProgress: false,
    emoji: '🌄',
  },
  {
    challengeId: 10,
    challengeName: '1일 1시간 공부',
    emoji: '✏',
    challengerCount: 60,
    isInProgress: false,
  },
  {
    challengeId: 11,
    challengeName: '책읽기',
    emoji: '📚',
    challengerCount: 13,
    isInProgress: false,
  },
  {
    challengeId: 12,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
    challengerCount: 80,
    isInProgress: false,
  },
  {
    challengeId: 13,
    challengeName: '미라클 모닝',
    emoji: '🌄',
    challengerCount: 35,
    isInProgress: false,
  },
  {
    challengeId: 14,
    challengeName: '1일 1시간 공부',
    emoji: '✏',
    challengerCount: 60,
    isInProgress: false,
  },
  {
    challengeId: 15,
    challengeName: '책읽기',
    emoji: '📚',
    challengerCount: 13,
    isInProgress: false,
  },
  {
    challengeId: 16,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
    challengerCount: 80,
    isInProgress: false,
  },
  {
    challengeId: 17,
    challengeName: '미라클 모닝',
    emoji: '🌄',
    challengerCount: 35,
    isInProgress: false,
  },
  {
    challengeId: 18,
    challengeName: '1일 1시간 공부',
    emoji: '✏',
    challengerCount: 60,
    isInProgress: false,
  },
  {
    challengeId: 19,
    challengeName: '1일 1시간 공부',
    emoji: '✏',
    challengerCount: 60,
    isInProgress: false,
  },

  {
    challengeId: 20,
    challengeName: '책읽기',
    emoji: '📚',
    challengerCount: 13,
    isInProgress: false,
  },
  {
    challengeId: 21,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
    challengerCount: 80,
    isInProgress: false,
  },
  {
    challengeId: 22,
    challengeName: '미라클 모닝',
    emoji: '🌄',
    challengerCount: 35,
    isInProgress: false,
  },
  {
    challengeId: 23,
    challengeName: '1일 1시간 공부',
    emoji: '✏',
    challengerCount: 60,
    isInProgress: false,
  },
  {
    challengeId: 24,
    challengeName: '책읽기',
    emoji: '📚',
    challengerCount: 13,
    isInProgress: false,
  },
  {
    challengeId: 25,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
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
    emoji: '🌄',
    progressCount: 2,
    startTime: twoDaysAgoStartTime,
    successCount: 3,
  },
  {
    cycleId: 2,
    challengeId: 2,
    challengeName: '운동',
    emoji: '💪',
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 3,
    challengeId: 3,
    challengeName: '샤워',
    emoji: '💦',
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 4,
    challengeId: 4,
    challengeName: '취침',
    emoji: '🕙',
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 5,
    challengeId: 5,
    challengeName: '화이팅',
    emoji: '💪',
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 6,
    challengeId: 6,
    challengeName: '어제 오늘 내일은 뭐 먹지?',
    progressCount: 0,
    emoji: '💭',
    startTime,
    successCount: 3,
  },
  {
    cycleId: 7,
    challengeId: 7,
    challengeName: '마르코 화이팅',
    progressCount: 0,
    emoji: '💪',
    startTime,
    successCount: 3,
  },
  {
    cycleId: 8,
    challengeId: 8,
    challengeName: '우연 화이팅',
    progressCount: 0,
    emoji: '💪',
    startTime,
    successCount: 3,
  },
  {
    cycleId: 9,
    challengeId: 9,
    challengeName: '빅터 화이팅',
    progressCount: 0,
    emoji: '💪',
    startTime,
    successCount: 3,
  },
  {
    cycleId: 10,
    challengeId: 10,
    challengeName: '우린 멋져',
    progressCount: 0,
    emoji: '✨',
    startTime,
    successCount: 3,
  },
];

export const cycleByIdData = [
  {
    cycleId: 1,
    challengeId: 2,
    challengeName: '미라클 모닝',
    emoji: '🌄',
    progressCount: 3,
    startTime: '2022-07-01T17:00:00',
    successCount: 3,
    cycleDetails: [
      {
        progressImage: 'https://emoji-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명1.',
      },
      {
        progressImage: 'https://emoji-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명2.',
      },
      {
        progressImage: 'https://emoji-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명3.',
      },
    ],
  },
  {
    cycleId: 2,
    challengeId: 3,
    challengeName: '운동',
    emoji: '💪',
    progressCount: 2,
    startTime: '2022-07-01T17:00:00',
    successCount: 5,
    cycleDetails: [
      {
        progressImage: 'https://emoji-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명1.',
      },
      {
        progressImage: 'https://emoji-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명2.',
      },
    ],
  },
  {
    cycleId: 4,
    challengeId: 4,
    challengeName: '공부',
    emoji: '✏',
    progressCount: 3,
    startTime: '2022-07-01T17:00:00',
    successCount: 3,
    cycleDetails: [
      {
        progressImage: 'https://emoji-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명1.',
      },
      {
        progressImage: 'https://emoji-copy.com/wp-content/uploads/1f64a.png',
        progressTime: '2022-07-01T17:00:00',
        description: '설명2.',
      },
      {
        progressImage: 'https://emoji-copy.com/wp-content/uploads/1f64a.png',
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
    emoji: '🌄',
    successCount: 3,
  },
  {
    challengeId: 2,
    challengeName: '1일 1시간 공부',
    emoji: '✏',
    successCount: 2,
  },
  {
    challengeId: 3,
    challengeName: '책읽기',
    emoji: '📚',
    successCount: 1,
  },
  {
    challengeId: 4,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
    successCount: 100,
  },
  {
    challengeId: 5,
    challengeName: '미라클 모닝',
    emoji: '🌄',
    successCount: 3,
  },
  {
    challengeId: 6,
    challengeName: '1일 1시간 공부',
    emoji: '✏',
    successCount: 2,
  },
  {
    challengeId: 7,
    challengeName: '책읽기',
    emoji: '📚',
    successCount: 1,
  },
  {
    challengeId: 8,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
    successCount: 100,
  },
  {
    challengeId: 9,
    challengeName: '미라클 모닝',
    emoji: '🌄',
    successCount: 3,
  },
  {
    challengeId: 10,
    challengeName: '1일 1시간 공부',
    emoji: '✏',
    successCount: 2,
  },
  {
    challengeId: 11,
    challengeName: '책읽기',
    emoji: '📚',
    successCount: 1,
  },
  {
    challengeId: 12,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
    successCount: 100,
  },
  {
    challengeId: 13,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
    successCount: 100,
  },
  {
    challengeId: 14,
    challengeName: '미라클 모닝',
    emoji: '🌄',
    successCount: 3,
  },
  {
    challengeId: 15,
    challengeName: '1일 1시간 공부',
    emoji: '✏',
    successCount: 2,
  },
  {
    challengeId: 16,
    challengeName: '책읽기',
    emoji: '📚',
    successCount: 1,
  },
  {
    challengeId: 17,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
    successCount: 100,
  },
  {
    challengeId: 18,
    challengeName: '미라클 모닝',
    emoji: '🌄',
    successCount: 3,
  },
  {
    challengeId: 19,
    challengeName: '1일 1시간 공부',
    emoji: '✏',
    successCount: 2,
  },
  {
    challengeId: 20,
    challengeName: '책읽기',
    emoji: '📚',
    successCount: 1,
  },
  {
    challengeId: 21,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
    successCount: 100,
  },
  {
    challengeId: 22,
    challengeName: '미라클 모닝',
    emoji: '🌄',
    successCount: 3,
  },
  {
    challengeId: 23,
    challengeName: '1일 1시간 공부',
    emoji: '✏',
    successCount: 2,
  },
  {
    challengeId: 24,
    challengeName: '책읽기',
    emoji: '📚',
    successCount: 1,
  },
  {
    challengeId: 25,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
    successCount: 100,
  },
  {
    challengeId: 26,
    challengeName: '미라클 모닝',
    emoji: '🌄',
    successCount: 3,
  },
  {
    challengeId: 27,
    challengeName: '1일 1시간 공부',
    emoji: '✏',
    successCount: 2,
  },
  {
    challengeId: 28,
    challengeName: '책읽기',
    emoji: '📚',
    successCount: 1,
  },
  {
    challengeId: 29,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
    successCount: 100,
  },
  {
    challengeId: 30,
    challengeName: '미라클 모닝',
    emoji: '🌄',
    successCount: 3,
  },
  {
    challengeId: 31,
    challengeName: '1일 1시간 공부',
    emoji: '✏',
    successCount: 2,
  },
  {
    challengeId: 32,
    challengeName: '책읽기',
    emoji: '📚',
    successCount: 1,
  },
  {
    challengeId: 33,
    challengeName: '일찍 잠자기',
    emoji: '🕙',
    successCount: 100,
  },
];

export const userData = {
  nickname: '테스트 닉네임',
  introduction: '테스트 자기소개',
  picture: 'https://emoji-copy.com/wp-content/uploads/1f64a.png',
  email: 'test@gmail.com',
};

export const accessTokenData = 'testAccessToken';

export const cycleDetailData = {
  cycleId: 1,
  challengeId: 2,
  challengeName: '미라클 모닝',
  emoji: '🌄',
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
  emoji: '🌄',
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

export const db = [];
