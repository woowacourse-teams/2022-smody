export const challengeData = [
  {
    challengeId: 1,
    challengeName: '미라클 모닝',
    challengerCount: 35,
    inProgress: false,
  },
  {
    challengeId: 2,
    challengeName: '1일 1시간 공부',
    challengerCount: 60,
    inProgress: false,
  },
  {
    challengeId: 3,
    challengeName: '책읽기',
    challengerCount: 13,
    inProgress: false,
  },
  {
    challengeId: 4,
    challengeName: '일찍 잠자기',
    challengerCount: 80,
    inProgress: false,
  },
  {
    challengeId: 5,
    challengeName: '미라클 모닝',
    challengerCount: 35,
    inProgress: false,
  },
  {
    challengeId: 6,
    challengeName: '1일 1시간 공부',
    challengerCount: 60,
    inProgress: false,
  },
  {
    challengeId: 7,
    challengeName: '책읽기',
    challengerCount: 13,
    inProgress: false,
  },
  {
    challengeId: 8,
    challengeName: '일찍 잠자기',
    challengerCount: 80,
    inProgress: false,
  },
  {
    challengeId: 9,
    challengeName: '미라클 모닝',
    challengerCount: 35,
    inProgress: false,
  },
  {
    challengeId: 10,
    challengeName: '1일 1시간 공부',
    challengerCount: 60,
    inProgress: false,
  },
  {
    challengeId: 11,
    challengeName: '책읽기',
    challengerCount: 13,
    inProgress: false,
  },
  {
    challengeId: 12,
    challengeName: '일찍 잠자기',
    challengerCount: 80,
    inProgress: false,
  },
  {
    challengeId: 13,
    challengeName: '미라클 모닝',
    challengerCount: 35,
    inProgress: false,
  },
  {
    challengeId: 14,
    challengeName: '1일 1시간 공부',
    challengerCount: 60,
    inProgress: false,
  },
  {
    challengeId: 15,
    challengeName: '책읽기',
    challengerCount: 13,
    inProgress: false,
  },
  {
    challengeId: 16,
    challengeName: '일찍 잠자기',
    challengerCount: 80,
    inProgress: false,
  },
  {
    challengeId: 17,
    challengeName: '미라클 모닝',
    challengerCount: 35,
    inProgress: false,
  },
  {
    challengeId: 18,
    challengeName: '1일 1시간 공부',
    challengerCount: 60,
    inProgress: false,
  },
  {
    challengeId: 19,
    challengeName: '1일 1시간 공부',
    challengerCount: 60,
    inProgress: false,
  },

  {
    challengeId: 20,
    challengeName: '책읽기',
    challengerCount: 13,
    inProgress: false,
  },
  {
    challengeId: 21,
    challengeName: '일찍 잠자기',
    challengerCount: 80,
    inProgress: false,
  },
  {
    challengeId: 22,
    challengeName: '미라클 모닝',
    challengerCount: 35,
    inProgress: false,
  },
  {
    challengeId: 23,
    challengeName: '1일 1시간 공부',
    challengerCount: 60,
    inProgress: false,
  },
  {
    challengeId: 24,
    challengeName: '책읽기',
    challengerCount: 13,
    inProgress: false,
  },
  {
    challengeId: 25,
    challengeName: '일찍 잠자기',
    challengerCount: 80,
    inProgress: false,
  },
];

const date = new Date();
date.setHours(date.getHours() + 9);

const [startTime, _] = date.toISOString().split('.');

export const cycleData = [
  {
    cycleId: 1,
    challengeId: 2,
    challengeName: '미라클 모닝',
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 2,
    challengeId: 2,
    challengeName: '운동',
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 3,
    challengeId: 2,
    challengeName: '샤워',
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 4,
    challengeId: 2,
    challengeName: '취침',
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 5,
    challengeId: 2,
    challengeName: '화이팅',
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 6,
    challengeId: 2,
    challengeName: '어제 오늘 내일은 뭐 먹지?',
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 7,
    challengeId: 2,
    challengeName: '마르코 화이팅',
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 8,
    challengeId: 2,
    challengeName: '우연 화이팅',
    progressCount: 0,
    startTime,
    successCount: 3,
  },
  {
    cycleId: 9,
    challengeId: 2,
    challengeName: '우린 멋져',
    progressCount: 0,
    startTime,
    successCount: 3,
  },
];

// export const mySuccessChallengeData = [
//   {
//     challengeId: 1,
//     challengeName: '미라클 모닝',
//     successCount: 45,
//     bgColor: '#FED6D6',
//     emoji: '🌞',
//   },
//   {
//     challengeId: 2,
//     challengeName: '헬스장 가기',
//     successCount: 39,
//     bgColor: '#D6E1FE',
//     emoji: '💪',
//   },
//   {
//     challengeId: 3,
//     challengeName: '하루 독서 30분',
//     successCount: 33,
//     bgColor: '#D6FEDA',
//     emoji: '📚',
//   },
//   {
//     challengeId: 4,
//     challengeName: '공식문서 읽기',
//     successCount: 28,
//     bgColor: '#D6CCF3',
//     emoji: '📰',
//   },
//   {
//     challengeId: 5,
//     challengeName: '알고리즘 1문제 풀기',
//     successCount: 15,
//     bgColor: '#F6FCB8',
//     emoji: '🏫',
//   },
//   {
//     challengeId: 6,
//     challengeName: '만보 걷기',
//     successCount: 25,
//     bgColor: '#D6F7FE',
//     emoji: '💦',
//   },
// ];

export const mySuccessChallengeData = [
  {
    challengeId: 1,
    challengeName: '미라클 모닝',
    successCount: 3,
  },
  {
    challengeId: 2,
    challengeName: '1일 1시간 공부',
    successCount: 2,
  },
  {
    challengeId: 3,
    challengeName: '책읽기',
    successCount: 1,
  },
  {
    challengeId: 4,
    challengeName: '일찍 잠자기',
    successCount: 100,
  },
];
