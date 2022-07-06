import { Cycle } from 'commonType';

import { CertItem } from 'components/CertItem';

export default {
  title: 'Components/CertItem',
  component: CertItem,
  argTypes: {
    progressCount: {
      options: [0, 1, 2, 3],
      control: 'select',
    },
    cycleId: {
      table: {
        disable: true,
      },
    },
    challengeId: {
      table: {
        disable: true,
      },
    },
  },
};

export const DefaultCertItem = (args: Cycle) => <CertItem {...args} />;

DefaultCertItem.args = {
  challengeName: '미라클모닝',
  progressCount: 3,
  startTime: '2022-07-06T17:00:00',
  successCount: 3,
};
