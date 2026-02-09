import Axios, { type AxiosRequestConfig } from 'axios';
import { getAccessToken, getRefreshToken, setTokens } from '~/utils/authTokens';
import { ORDER_URL } from './Constants';

const AUTH_HEADER ${DB_USER:***REMOVED***} 'Authorization';

export const ORDER_AXIOS_INSTANCE ${DB_USER:***REMOVED***} Axios.create({
  baseURL: ORDER_URL,
});

const refreshClient ${DB_USER:***REMOVED***} Axios.create({
  baseURL: ORDER_URL,
});

const isRefreshRequest ${DB_USER:***REMOVED***} (url?: string) ${DB_USER:***REMOVED***}> url?.includes('/auth/refresh');

const applyAccessToken ${DB_USER:***REMOVED***} (config: AxiosRequestConfig, accessToken?: string) ${DB_USER:***REMOVED***}> {
  if (!accessToken) return;
  if (isRefreshRequest(config.url)) return;

  const headers ${DB_USER:***REMOVED***} config.headers ?? {};
  if (
    typeof (headers as { set?: (k: string, v: string) ${DB_USER:***REMOVED***}> void }).set ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}
    'function'
  ) {
    (headers as { set: (k: string, v: string) ${DB_USER:***REMOVED***}> void }).set(
      AUTH_HEADER,
      `Bearer ${accessToken}`
    );
  } else {
    (headers as Record<string, string>)[AUTH_HEADER] ${DB_USER:***REMOVED***} `Bearer ${accessToken}`;
  }
  config.headers ${DB_USER:***REMOVED***} headers;
};

const refreshAccessToken ${DB_USER:***REMOVED***} async () ${DB_USER:***REMOVED***}> {
  const refreshToken ${DB_USER:***REMOVED***} getRefreshToken();
  if (!refreshToken) {
    return null;
  }

  const { data } ${DB_USER:***REMOVED***} await refreshClient.post('/auth/refresh', {
    refreshToken,
  });

  const accessToken ${DB_USER:***REMOVED***} data?.data?.accessToken;
  const nextRefreshToken ${DB_USER:***REMOVED***} data?.data?.refreshToken ?? refreshToken;

  if (!accessToken) {
    return null;
  }

  setTokens({ access: accessToken, refresh: nextRefreshToken });

  return accessToken;
};

ORDER_AXIOS_INSTANCE.interceptors.request.use((config) ${DB_USER:***REMOVED***}> {
  applyAccessToken(config, getAccessToken());
  return config;
});

let refreshPromise: Promise<string | null> | null ${DB_USER:***REMOVED***} null;

ORDER_AXIOS_INSTANCE.interceptors.response.use(
  (response) ${DB_USER:***REMOVED***}> response,
  async (error) ${DB_USER:***REMOVED***}> {
    const status ${DB_USER:***REMOVED***} error?.response?.status;
    const originalConfig ${DB_USER:***REMOVED***} error?.config as
      | (AxiosRequestConfig & { _retry?: boolean })
      | undefined;

    if (!originalConfig || status !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 403 || originalConfig._retry) {
      return Promise.reject(error);
    }

    if (isRefreshRequest(originalConfig.url)) {
      return Promise.reject(error);
    }

    originalConfig._retry ${DB_USER:***REMOVED***} true;

    if (!refreshPromise) {
      refreshPromise ${DB_USER:***REMOVED***} refreshAccessToken().finally(() ${DB_USER:***REMOVED***}> {
        refreshPromise ${DB_USER:***REMOVED***} null;
      });
    }

    const accessToken ${DB_USER:***REMOVED***} await refreshPromise;
    if (!accessToken) {
      return Promise.reject(error);
    }

    applyAccessToken(originalConfig, accessToken);
    return ORDER_AXIOS_INSTANCE(originalConfig);
  }
);

export const orderCustomInstance ${DB_USER:***REMOVED***} <T>(
  config: AxiosRequestConfig,
  options?: AxiosRequestConfig
): Promise<T> ${DB_USER:***REMOVED***}> {
  const promise ${DB_USER:***REMOVED***} ORDER_AXIOS_INSTANCE({
    ...config,
    ...options,
  }).then(({ data }) ${DB_USER:***REMOVED***}> data);

  return promise;
};
