import { MappedKeyToUnion } from 'commonType';

import COLOR from 'styles/color';

export type AvailablePickedColor = MappedKeyToUnion<typeof COLOR>;
