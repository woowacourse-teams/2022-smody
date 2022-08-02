import { Cycle } from 'commonType';

export interface CertImageWrapperProps {
  isSelectImage: boolean;
}

export interface CertFormPageLocationState
  extends Pick<Cycle, 'cycleId' | 'progressCount' | 'challengeId' | 'challengeName'> {}
