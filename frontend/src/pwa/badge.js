// navigator에 사용 메서드 type이 존재하지 않아서 js로 작성
// https://developer.mozilla.org/en-US/docs/Web/API/Navigator/setAppBadge

export const setBadge = (number) => {
  if (navigator.setAppBadge) {
    navigator.setAppBadge(number);
  } else if (navigator.setExperimentalAppBadge) {
    navigator.setExperimentalAppBadge(number);
  } else if (window.ExperimentalBadge) {
    window.ExperimentalBadge.set(number);
  }
};

export const clearBadge = () => {
  if (navigator.clearAppBadge) {
    navigator.clearAppBadge();
  } else if (navigator.clearExperimentalAppBadge) {
    navigator.clearExperimentalAppBadge();
  } else if (window.ExperimentalBadge) {
    window.ExperimentalBadge.clear();
  }
};
