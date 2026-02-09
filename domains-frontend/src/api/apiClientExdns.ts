import Axios, { type AxiosRequestConfig } from 'axios';
import { EXDNS_TOKEN, EXDNS_URL } from './Constants';

const AUTH_HEADER ${DB_USER:***REMOVED***} 'authentication';

export const AXIOS_INSTANCE ${DB_USER:***REMOVED***} Axios.create({
  baseURL: EXDNS_URL,
});

const applyToken ${DB_USER:***REMOVED***} (config: AxiosRequestConfig) ${DB_USER:***REMOVED***}> {
  if (!EXDNS_TOKEN) return;

  const headers ${DB_USER:***REMOVED***} config.headers ?? {};
  if (
    typeof (headers as { set?: (k: string, v: string) ${DB_USER:***REMOVED***}> void }).set ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}
    'function'
  ) {
    (headers as { set: (k: string, v: string) ${DB_USER:***REMOVED***}> void }).set(
      AUTH_HEADER,
      `Bearer ${EXDNS_TOKEN}`
    );
  } else {
    (headers as Record<string, string>)[AUTH_HEADER] ${DB_USER:***REMOVED***} `Bearer ${EXDNS_TOKEN}`;
  }
  config.headers ${DB_USER:***REMOVED***} headers;
};

AXIOS_INSTANCE.interceptors.request.use((config) ${DB_USER:***REMOVED***}> {
  applyToken(config);
  return config;
});

export const customInstance ${DB_USER:***REMOVED***} <T>(
  config: AxiosRequestConfig,
  options?: AxiosRequestConfig
): Promise<T> ${DB_USER:***REMOVED***}> {
  const promise ${DB_USER:***REMOVED***} AXIOS_INSTANCE({
    ...config,
    ...options,
  }).then(({ data }) ${DB_USER:***REMOVED***}> data);

  return promise;
};
