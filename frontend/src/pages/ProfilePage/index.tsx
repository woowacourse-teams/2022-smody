import CuteCatWithSmody from 'assets/cute_cat_with_smody.png';
import styled from 'styled-components';

import { FlexBox } from 'components';

export const ProfilePage = () => {
  return (
    <Wrapper>
      <img src={CuteCatWithSmody} alt="스무디와 고양이" width={500} />
    </Wrapper>
  );
};

const Wrapper = styled(FlexBox).attrs({
  justifyContent: 'center',
})``;
