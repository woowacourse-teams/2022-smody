import { useEffect } from 'react';
import { useParams, useLocation } from "react-router-dom";
import { apiClient } from 'apis/apiClient';

const getUrlParameter = (name: string) => {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    let regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    let results = regex.exec(window.location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
};

const getCallback = async (code: string) => {
    await apiClient.axios.get(`/oauth/callback?code=${code}`);
}

export const Home = () => {
    const { code } = useParams();
    const { search } = useLocation();

    useEffect(() => {
        const code = getUrlParameter('code');
        // const code = '4%2F0AdQt8qjssq9lmDElGU7r2Gunm53w8_16Tdp0-bGnIQ3LnmGT9PffwRVrJWUMn0OxLVC3_w';

        if (code.length === 0) {
            return;
        }

        getCallback(code);
    }, []);

    // const query = new URLSearchParams(search);
    //
    // const paramField = query.get('field');
    // const paramValue = query.get('value');
    //
    // console.log('search: ', search);
    // console.log('query: ', query);
    // console.log('paramField은 ? ', paramField);
    // console.log('paramValue ? ', paramValue);
    //
    // console.log('getUrlParameter: ', getUrlParameter('code'));
    //

    return (
    <>
      <p>안녕하세요. SMODY 입니다.</p>
    </>
  );
};
