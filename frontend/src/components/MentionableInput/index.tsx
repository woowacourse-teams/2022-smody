import { MembersPopoverProps, MentionableInputProps } from './type';
import { MemberItemProps } from './type';
import useMentionableInput from './useMentionableInput';
import { cloneElement } from 'react';
import styled, { css } from 'styled-components';

import useThemeContext from 'hooks/useThemeContext';

import { Popover, InfiniteScroll, LoadingSpinner } from 'components';
import { FlexBox, Text } from 'components';

export const MentionableInput = ({
  editableElementRef,
  setContent,
  mentionedMemberIds,
  setMentionedMemberIds,
  editableElement,
}: MentionableInputProps) => {
  const {
    isPopoverOpen,
    handleClosePopover,
    membersData,
    hasNextMembersPage,
    fetchNextMembersPage,
    selectMember,
    handleKeydownCommentInput,
    handleInputCommentInput,
  } = useMentionableInput({
    editableElementRef,
    setContent,
    mentionedMemberIds,
    setMentionedMemberIds,
  });

  return (
    <>
      {cloneElement(editableElement, {
        ref: editableElementRef,
        contentEditable: true,
        onKeyDown: handleKeydownCommentInput,
        onInput: handleInputCommentInput,
        style: {
          whiteSpace: 'pre-wrap',
          wordWrap: 'break-word',
          wordBreak: 'break-all',
        },
      })}
      {isPopoverOpen && (
        <MembersPopover
          handleClosePopover={handleClosePopover}
          membersData={membersData}
          hasNextMembersPage={hasNextMembersPage}
          fetchNextMembersPage={fetchNextMembersPage}
          selectMember={selectMember}
        />
      )}
    </>
  );
};

const MembersPopover = ({
  fetchNextMembersPage,
  hasNextMembersPage,
  handleClosePopover,
  membersData,
  selectMember,
}: MembersPopoverProps) => {
  return (
    <Popover handleClosePopover={handleClosePopover}>
      <InfiniteScroll
        loadMore={fetchNextMembersPage}
        hasMore={hasNextMembersPage}
        loader={<LoadingSpinner />}
      >
        <ul style={{ listStyle: 'none' }}>
          {membersData?.pages.map((page) =>
            page?.data.map((member) => (
              <li
                key={member.memberId}
                onClick={() => {
                  selectMember(member.memberId, member.nickname);
                  handleClosePopover();
                }}
              >
                <MemberItem nickname={member.nickname} picture={member.picture} />
              </li>
            )),
          )}
        </ul>
      </InfiniteScroll>
    </Popover>
  );
};

const MemberItem = ({ nickname, picture }: MemberItemProps) => {
  const themeContext = useThemeContext();

  return (
    <Wrapper gap="0.8rem">
      <ProfileImg
        aria-label="프로필 이미지"
        src={picture}
        alt={`${nickname} 프로필 사진`}
      />
      <Text size={16} color={themeContext.onBackground}>
        {nickname}
      </Text>
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox)`
  ${({ theme }) => css`
    padding: 0.2rem 1rem;
    cursor: pointer;
    &:hover {
      background-color: ${theme.primary};
      & p {
        color: ${theme.onPrimary};
      }
    }
  `}
`;

const ProfileImg = styled.img`
  width: 1.5rem;
  height: 1.5rem;
  border-radius: 50%;
  object-fit: cover;
`;
