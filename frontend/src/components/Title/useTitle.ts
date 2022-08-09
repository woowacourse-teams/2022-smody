import { UseTitleProps } from './type';
import { useNavigate } from 'react-router-dom';

const useTitle = ({ linkTo }: UseTitleProps) => {
  const navigate = useNavigate();
  const backToPreviousPage = () => {
    if (linkTo) {
      navigate(linkTo);
      return;
    }

    navigate(-1);
  };
  return { backToPreviousPage };
};

export default useTitle;
