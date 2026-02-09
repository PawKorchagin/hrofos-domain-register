import { AuthStore } from './AutoStore';
import { UserStore } from './UserStore';

export class RootStore {
  authStore: AuthStore;
  userStore: UserStore;

  constructor() {
    this.authStore ${DB_USER:***REMOVED***} new AuthStore();
    this.userStore ${DB_USER:***REMOVED***} new UserStore();
  }
}
