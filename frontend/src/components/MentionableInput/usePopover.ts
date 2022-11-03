import { usePopoverProps } from './type';
import { useState } from 'react';
import { insertAfter } from 'utils';

const usePopover = ({
  commentInputRef,
  mentionedMemberIds,
  setMentionedMemberIds,
  lastMentionSymbolPosition,
  filterValue,
  setFilterValue,
  isFilterValueInitiated,
}: usePopoverProps) => {
  const [isPopoverOpen, setIsPopoverOpen] = useState(false);

  const handleClosePopover = () => {
    setIsPopoverOpen(false);
  };

  const handleOpenPopover = () => {
    setIsPopoverOpen(true);
  };

  // selectMember
  const selectMember = (memberId: number, nickname: string) => {
    if (commentInputRef.current === null) {
      return;
    }

    mentionedMemberIds.push(memberId);
    setMentionedMemberIds(mentionedMemberIds);

    const childNodes = commentInputRef.current.childNodes;

    let accLength = 0;

    const currentNodeIndex = Array.from(childNodes).findIndex((childNode) => {
      const text = childNode.textContent!;
      if (lastMentionSymbolPosition.current <= accLength + text.length) {
        return true;
      }

      accLength += text.length;
      return false;
    });

    const currentNode = childNodes[currentNodeIndex];
    const currentText = currentNode.textContent;

    const targetStart = lastMentionSymbolPosition.current - accLength - 1;
    const targetEnd = lastMentionSymbolPosition.current - accLength + filterValue.length;

    const frontTextNode = document.createTextNode(
      currentText!.substring(0, targetStart)!,
    );

    const nicknameSpan = document.createElement('span');
    nicknameSpan.innerText = `@${nickname}`;
    nicknameSpan.setAttribute('contenteditable', 'false');
    nicknameSpan.style.backgroundColor = '#7b61fe';
    nicknameSpan.style.color = '#fff';
    nicknameSpan.style.borderRadius = '5px';
    nicknameSpan.style.lineHeight = '2';
    nicknameSpan.style.padding = '2px 4px';
    nicknameSpan.setAttribute('data-member-id', String(memberId));

    const backTextNode = document.createTextNode(currentText!.substring(targetEnd)!);

    if (currentNodeIndex === 0) {
      commentInputRef.current.appendChild(frontTextNode);
      commentInputRef.current.appendChild(nicknameSpan);
      commentInputRef.current.appendChild(backTextNode);
    } else {
      const prevNode = childNodes[currentNodeIndex - 1];

      insertAfter(prevNode, frontTextNode);
      insertAfter(frontTextNode, nicknameSpan);
      insertAfter(nicknameSpan, backTextNode);
    }

    commentInputRef.current.removeChild(currentNode);
    const ABSENCE_SYMBOL_POSITION = -1;

    // init 삼형제
    lastMentionSymbolPosition.current = ABSENCE_SYMBOL_POSITION;
    setFilterValue('');
    isFilterValueInitiated.current = true;

    // contenteditable div에서 cursor position에 focus 하기
    const element = commentInputRef.current;
    const selection = window.getSelection();
    const range = document.createRange();
    selection!.removeAllRanges();
    range.selectNodeContents(element);
    range.collapse(false);
    selection!.addRange(range);
    element.focus();
  };
  // selectMember 끝
  return { isPopoverOpen, handleOpenPopover, handleClosePopover, selectMember };
};

export default usePopover;
