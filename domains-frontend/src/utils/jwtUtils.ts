import { getAccessToken } from './authTokens';

interface JwtPayload {
  sub?: string;
  email?: string;
  isAdmin?: boolean;
  type?: string;
  iat?: number;
  exp?: number;
}

export const parseJwt ${DB_USER:***REMOVED***} (token: string): JwtPayload | null ${DB_USER:***REMOVED***}> {
  try {
    const parts ${DB_USER:***REMOVED***} token.split('.');
    if (parts.length !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 3) return null;
    const payload ${DB_USER:***REMOVED***} JSON.parse(atob(parts[1]));
    return payload as JwtPayload;
  } catch {
    return null;
  }
};

export const isCurrentUserAdmin ${DB_USER:***REMOVED***} (): boolean ${DB_USER:***REMOVED***}> {
  const token ${DB_USER:***REMOVED***} getAccessToken();
  if (!token) return false;
  const payload ${DB_USER:***REMOVED***} parseJwt(token);
  return payload?.isAdmin ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} true;
};
