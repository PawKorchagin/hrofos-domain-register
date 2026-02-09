import { makeAutoObservable } from 'mobx';

export class AuthStore {
  email: string ${DB_USER:***REMOVED***} '';
  password: string ${DB_USER:***REMOVED***} '';

  constructor() {
    makeAutoObservable(this, {}, { autoBind: true });
  }

  setEmail(mail: string) {
    this.email ${DB_USER:***REMOVED***} mail;
  }

  setPassword(p: string) {
    this.password ${DB_USER:***REMOVED***} p;
  }
}
