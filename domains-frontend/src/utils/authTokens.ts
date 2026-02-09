type TokenState ${DB_USER:***REMOVED***} {
  access?: string;
  refresh?: string;
};

const STORAGE_KEY_REFRESH ${DB_USER:***REMOVED***} 'hrofors_refresh';
const STORAGE_KEY_ACCESS ${DB_USER:***REMOVED***} 'hrofors_access';

const getWindowStore ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  if (typeof window ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'undefined') {
    return undefined;
  }

  if (!window._hrofors) {
    window._hrofors ${DB_USER:***REMOVED***} {};
  }

  return window._hrofors;
};

const readStoredRefresh ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  if (typeof window ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'undefined') {
    return undefined;
  }

  return localStorage.getItem(STORAGE_KEY_REFRESH) || undefined;
};

const readStoredAccess ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  if (typeof window ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'undefined') {
    return undefined;
  }

  return localStorage.getItem(STORAGE_KEY_ACCESS) || undefined;
};

export const getAccessToken ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const store ${DB_USER:***REMOVED***} getWindowStore();
  const stored ${DB_USER:***REMOVED***} readStoredAccess();

  if (store && !store.access && stored) {
    store.access ${DB_USER:***REMOVED***} stored;
  }

  return store?.access ?? stored;
};

export const getRefreshToken ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const store ${DB_USER:***REMOVED***} getWindowStore();
  const stored ${DB_USER:***REMOVED***} readStoredRefresh();

  if (store && !store.refresh && stored) {
    store.refresh ${DB_USER:***REMOVED***} stored;
  }

  return store?.refresh ?? stored;
};

export const setTokens ${DB_USER:***REMOVED***} ({ access, refresh }: TokenState) ${DB_USER:***REMOVED***}> {
  const store ${DB_USER:***REMOVED***} getWindowStore();

  if (store) {
    if (access !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} undefined) {
      store.access ${DB_USER:***REMOVED***} access;
    }
    if (refresh !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} undefined) {
      store.refresh ${DB_USER:***REMOVED***} refresh;
    }
  }

  if (typeof window !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'undefined') {
    if (access !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} undefined) {
      if (access) {
        localStorage.setItem(STORAGE_KEY_ACCESS, access);
      } else {
        localStorage.removeItem(STORAGE_KEY_ACCESS);
      }
    }
    if (refresh !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} undefined) {
      if (refresh) {
        localStorage.setItem(STORAGE_KEY_REFRESH, refresh);
      } else {
        localStorage.removeItem(STORAGE_KEY_REFRESH);
      }
    }
  }
};
