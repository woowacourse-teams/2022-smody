import { MappedKeyToUnion } from 'smody-library';

import COLOR from 'styles/color';

export type AvailablePickedColor = MappedKeyToUnion<typeof COLOR>;

export type FontSizeType = 10 | 11 | 12 | 14 | 16 | 20 | 24 | 32 | 40 | 48 | 70;
