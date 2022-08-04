import { Cycle } from 'commonType';

export interface CertImageWrapperProps {
  isSelectImage: boolean;
}

export interface TextAreaProps {
  isDark: boolean;
}

export type CertFormPageLocationState = Pick<
  Cycle,
  'cycleId' | 'challengeId' | 'challengeName' | 'successCount' | 'progressCount'
>;
