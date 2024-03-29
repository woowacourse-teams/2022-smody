type EventChallengeListIndexSignature = {
  [key: number]: {
    name: string;
    link: string;
    challengeName: string;
  };
};

export const EVENT_CHALLENGE_ID_LIST = [78, 79, 80, 81, 82, 83, 84, 85, 88, 89, 90];

export const EVENT_CHALLENGES: EventChallengeListIndexSignature = {
  78: {
    name: 'naepyeon',
    link: 'https://www.naepyeon.site/',
    challengeName: '내편에서 메세지 쓰기',
  },
  79: {
    name: 'gongseek',
    link: 'https://gongseek.site/',
    challengeName: '공식에서 질문과 토론하기',
  },
  80: {
    name: 'dallok',
    link: 'https://dallog.me/',
    challengeName: '달록에서 우테코 일정 확인하기',
  },
  81: {
    name: 'ducks',
    link: 'https://ducks.kr/',
    challengeName: '회고덕에서 템플릿을 활용하여 회고해보기',
  },
  82: {
    name: 'kkogkkog',
    link: 'https://kkogkkog.com/',
    challengeName: '꼭꼭으로 고마움 표현하기',
  },
  83: {
    name: 'momo',
    link: 'https://www.moyeora.site/',
    challengeName: '모모에서 모임 생성하기',
  },
  84: {
    name: 'teatime',
    link: 'https://teatime.pe.kr/',
    challengeName: '티타임에서 면담 신청하기',
  },
  85: {
    name: 'checkmate',
    link: 'https://checkmate.today/',
    challengeName: '체크메이트에서 출석 체크하기',
  },
  9: {
    name: 'gongcheck',
    link: 'https://www.gongcheck.day/',
    challengeName: '공책으로 우리 캠퍼스 아끼기',
  },
  90: {
    name: 'thankoo',
    link: 'https://thankoo.co.kr/',
    challengeName: '땡쿠에서 사람들 콕 찔러보기',
  },
  11: { name: 'f12', link: 'https://f12.app/', challengeName: '챌린지명' },
  12: { name: 'levellog', link: 'https://levellog.app/', challengeName: '챌린지명' },
  13: { name: 'ternoko', link: 'https://ternoko.site/', challengeName: '챌린지명' },
  89: {
    name: 'sokdaksokdak',
    link: 'https://sokdaksokdak.com/',
    challengeName: '속닥속닥에 글 작성하기',
  },
  15: { name: 'morak', link: 'https://mo-rak.com/', challengeName: '챌린지명' },
  16: {
    name: 'smody',
    link: 'https://www.smody.co.kr/',
    challengeName: '스모디에서 챌린지 인증하기',
  },

  88: {
    name: 'moamoa',
    link: 'https://moamoa.space/',
    challengeName: '모아모아에서 스터디 진행하기',
  },

  18: { name: 'jupjup', link: 'https://jupjup.site/', challengeName: '챌린지명' },
};
