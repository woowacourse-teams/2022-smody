import axios, { AxiosInstance, HeadersDefaults } from 'axios';

interface AxiosHeaders extends HeadersDefaults {
  Authorization: string;
}

class apiClientClass {
  axios: AxiosInstance;
  constructor() {
    this.axios = axios.create({
      baseURL:
        process.env.NODE_ENV === 'development'
          ? 'http://localhost:8080'
          : 'http://localhost:8080',
    });
  }
}

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

const accessToken = localStorage.getItem('accessToken');

export const apiClient = new apiClientClass();
export const authApiClient = new authApiClientClass();
