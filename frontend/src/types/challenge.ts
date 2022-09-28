export type Challenge = {
  challengeId: number;
  challengeName: string;
  emojiIndex: number;
  colorIndex: number;
};

export type ChallengeDetail = Challenge & {
  description: string;
};

export type UserChallengeStat = {
  totalCount: number;
  successCount: number;
};

export type AdditionalChallengeInfo = Challenge & {
  challengerCount: number;
  isInProgress: boolean;
};
