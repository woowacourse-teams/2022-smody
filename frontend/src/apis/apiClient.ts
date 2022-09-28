import axios, { AxiosInstance, HeadersDefaults } from 'axios';
import { BASE_URL } from 'env';

type AxiosHeaders = HeadersDefaults & {
  Authorization: string;
};

class apiClientClass {
  axios: AxiosInstance;
  constructor() {
    this.axios = axios.create({
      baseURL: BASE_URL,
    });
  }
}

const accessToken = localStorage.getItem('accessToken');

class authApiClientClass extends apiClientClass {
  constructor() {
    super();
    this.axios.defaults.headers = {
      Authorization: `Bearer ${accessToken}`,
    } as AxiosHeaders;
  }

  updateAuth(accessToken: string) {
    localStorage.setItem('accessToken', accessToken);
    this.axios.defaults.headers = {
      Authorization: `Bearer ${accessToken}`,
    } as AxiosHeaders;
  }

  deleteAuth() {
    localStorage.removeItem('accessToken');
    this.axios.defaults.headers = { Authorization: `` } as AxiosHeaders;
  }
}

export const apiClient = new apiClientClass();
export const authApiClient = new authApiClientClass();
