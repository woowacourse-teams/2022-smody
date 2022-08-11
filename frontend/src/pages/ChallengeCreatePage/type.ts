import { ChangeEvent } from 'react';

export interface ColorDivProps {
  color: string;
}

export interface ColorListProps {
  colorSelectedIndex: number;
  handleSelectColor: (event: ChangeEvent<HTMLInputElement>) => void;
}

export interface EmojiListProps {
  emojiSelectedIndex: number;
  handleSelectEmoji: (event: ChangeEvent<HTMLInputElement>) => void;
}
