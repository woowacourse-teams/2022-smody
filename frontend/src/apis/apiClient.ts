import axios, { AxiosInstance, HeadersDefaults } from 'axios';

import { DEV_BASE_URL } from 'constants/path';

interface AxiosHeaders extends HeadersDefaults {
  Authorization: string;
}

class apiClientClass {
  axios: AxiosInstance;
  constructor() {
    this.axios = axios.create({
      baseURL: process.env.NODE_ENV === 'development' ? DEV_BASE_URL : DEV_BASE_URL,
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
    localStorage.deleteItem('accessToken');
    this.axios.defaults.headers = { Authorization: `` } as AxiosHeaders;
  }
}

export const apiClient = new apiClientClass();
export const authApiClient = new authApiClientClass();
