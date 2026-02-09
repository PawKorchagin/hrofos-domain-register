import React, { createContext, useContext } from 'react';
import { RootStore } from './RootStore';

const rootStore ${DB_USER:***REMOVED***} new RootStore();
const StoreContext ${DB_USER:***REMOVED***} createContext(rootStore);

export const StoreProvider ${DB_USER:***REMOVED***} ({ children }: { children: React.ReactNode }) ${DB_USER:***REMOVED***}> (
  <StoreContext.Provider value${DB_USER:***REMOVED***}{rootStore}>{children}</StoreContext.Provider>
);

export const useStores ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> useContext(StoreContext);
