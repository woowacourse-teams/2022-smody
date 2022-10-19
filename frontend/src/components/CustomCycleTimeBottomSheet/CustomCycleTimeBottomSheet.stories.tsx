import { CustomCycleTimeBottomSheet } from '.';
import { CustomCycleTimeBottomSheetProps } from './type';

import { action } from '@storybook/addon-actions';

const backdropRoot = document.createElement('div');
backdropRoot.setAttribute('id', 'backdrop-root');
document.body.append(backdropRoot);

const modalRoot = document.createElement('div');
modalRoot.setAttribute('id', 'overlay-root');
document.body.append(modalRoot);

export default {
  title: 'Components/CustomCycleTimeBottomSheet',
  component: CustomCycleTimeBottomSheet,
};

export const DefaultCustomCycleTimeBottomSheet = (
  args: CustomCycleTimeBottomSheetProps,
) => <CustomCycleTimeBottomSheet {...args} />;

DefaultCustomCycleTimeBottomSheet.args = {
  challengeName: '테스트용 챌린지',
  joinChallenge: action('clicked'),
  handleCloseBottomSheet: action('clicked'),
};
