import { useEffect } from 'react';
import { apiClient } from 'apis/apiClient';

const getUrlParameter = (name: string) => {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    let regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    let results = regex.exec(window.location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
};

const getCallback = async (code: string) => {
    await apiClient.axios.get(`/oauth/login/google?code=${code}`);
}

export const Home = () => {
    useEffect(() => {
        const code = getUrlParameter('code');

        if (code.length === 0) {
            return;
        }

        getCallback(code);
    }, []);

    return (
    <>
      <p>안녕하세요. SMODY 입니다.</p>
    </>
  );
};
