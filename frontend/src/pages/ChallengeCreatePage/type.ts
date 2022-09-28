import { ChangeEvent } from 'react';

export type ColorDivProps = {
  color: string;
};

export type ColorListProps = {
  colorSelectedIndex: number;
  handleSelectColor: (event: ChangeEvent<HTMLInputElement>) => void;
};

export type EmojiListProps = {
  emojiSelectedIndex: number;
  handleSelectEmoji: (event: ChangeEvent<HTMLInputElement>) => void;
};
