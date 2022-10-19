import { action } from '@storybook/addon-actions';

import { SuccessModal } from 'components/SuccessModal';
import { SuccessModalProps } from 'components/SuccessModal/type';

const backdropRoot = document.createElement('div');
backdropRoot.setAttribute('id', 'backdrop-root');
document.body.append(backdropRoot);

const modalRoot = document.createElement('div');
modalRoot.setAttribute('id', 'overlay-root');
document.body.append(modalRoot);

export default {
  title: 'components/SuccessModal',
  component: SuccessModal,
};

export const DefaultSuccessModal = ({ ...args }: SuccessModalProps) => (
  <SuccessModal {...args} />
);

DefaultSuccessModal.args = {
  handleCloseModal: action('clicked'),
  cycleId: 1,
  challengeName: '미라클 모닝',
  successCount: 7,
  challengeId: 1,
  progressCount: 1,
  emojiIndex: 1,
};
