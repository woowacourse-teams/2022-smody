import { Cycle } from 'commonType';

export type CertItemProps = Cycle;

export type useCertItemProps = Pick<CertItemProps, 'startTime' | 'progressCount'>;
