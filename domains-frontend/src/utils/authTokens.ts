type TokenState ${DB_USER:***REMOVED***} {
  access?: string;
  refresh?: string;
};

const STORAGE_KEY ${DB_USER:***REMOVED***} 'hrofors_refresh';

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

  return localStorage.getItem(STORAGE_KEY) || undefined;
};

export const getAccessToken ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> getWindowStore()?.access;

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

  if (typeof window !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'undefined' && refresh !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} undefined) {
    if (refresh) {
      localStorage.setItem(STORAGE_KEY, refresh);
    } else {
      localStorage.removeItem(STORAGE_KEY);
    }
  }
};
