import { MappedKeyToUnion } from 'commonType';

import COLOR from 'styles/color';

export type AvailablePickedColor = MappedKeyToUnion<typeof COLOR>;
export type FontSizeType = 10 | 12 | 14 | 16 | 20 | 24 | 32 | 40 | 48;
