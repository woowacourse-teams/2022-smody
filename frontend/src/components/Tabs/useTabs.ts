import { HandleClickTabFunc } from './type';
import { useLocation, useNavigate } from 'react-router-dom';

const useTabs = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();

  const handleClickTab: HandleClickTabFunc = (path) => {
    navigate(path);
  };

  return { pathname, handleClickTab };
};

export default useTabs;
