import { makeAutoObservable, runInAction } from 'mobx';
import type { UserResponse } from '~/api/models/auth';
import { getCurrentUser } from '~/api/services/auth';

export class UserStore {
  user!: UserResponse;

  constructor() {
    makeAutoObservable(this, {}, { autoBind: true });
  }

  setUser(user: UserResponse) {
    this.user ${DB_USER:***REMOVED***} user;
  }

  async fetchMe() {
    const user ${DB_USER:***REMOVED***} await getCurrentUser();
    runInAction(() ${DB_USER:***REMOVED***}> {
      if (user.data) this.user ${DB_USER:***REMOVED***} user.data;
    });
  }
}
