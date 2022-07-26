type ErrorMessageType = { [key: number]: string };

export const ERROR_MESSAGE: ErrorMessageType = {
  2002: '로그인이 필요한 서비스입니다',
  2004: 'OAuth 인증 코드가 올바르지 않습니다',
  3001: '[챌린지 인증 실패] 이미 성공한 사이클입니다',
  3002: '[챌린지 인증 실패] 인증할 수 있는 시간이 아닙니다',
  3003: '이미 진행중인 챌린지입니다',
  3004: '유효하지 않은 시작시간입니다',
  9001: '인가 관련 서버내부의 오류가 발생했습니다',
};
