import { Route, Routes } from 'react-router';
import WelcomePage from '../pages/WelcomePage.tsx';
import Layout from '../components/layouts/Layout.tsx';
import PublicLayout from '../components/layouts/PublicLayout.tsx';
import CheckDomainPage from '../pages/CheckDomainPage.tsx';
import NotFound from '../pages/NotFound.tsx';
import AuthLayout from '../components/layouts/AuthLayout.tsx';
import SignInPage from '../pages/auth/SignInPage.tsx';
import SignUpPage from '../pages/auth/SignUpPage.tsx';
import ForgetPasswordPage from '../pages/auth/ForgetPasswordPage.tsx';
import CheckEmailPage from '../pages/auth/CheckEmailPage.tsx';
import TFAPage from '../pages/auth/TFAPage.tsx';
import TFATotpPage from '../pages/auth/TFATotpPage.tsx';
import TFAWebAuthnPage from '../pages/auth/TFAWebAuthnPage.tsx';
import VerificateEmailPage from '../pages/auth/VerificateEmailPage.tsx';
import AppLayout from '../components/layouts/AppLayout.tsx';
import DashboardPage from '../pages/app/DashboardPage.tsx';
import DomainsPage from '../pages/app/DomainsPage.tsx';
import EventsPage from '../pages/app/EventsPage.tsx';
import CartPage from '../pages/app/CartPage.tsx';
import DNSPage from '../pages/app/DNSPage.tsx';
import ProfilePage from '../pages/app/ProfilePage.tsx';
import AdminLayout from '~/components/layouts/AdminLayout.tsx';
import AdminDashboardPage from '~/pages/admin/AdminDashboardPage.tsx';
import UsersDomainsPage from '~/pages/admin/UsersDomainsPage.tsx';
import UsersPage from '~/pages/admin/UsersPage.tsx';
import SystemEventsPage from '~/pages/admin/SystemEventsPage.tsx';
import FinancesPage from '~/pages/admin/FinancesPage.tsx';
import ZonesPage from '~/pages/admin/ZonesPage.tsx';

const Index ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  return (
    <Routes>
      <Route element${DB_USER:***REMOVED***}{<Layout />}>
        <Route path${DB_USER:***REMOVED***}{'*'} element${DB_USER:***REMOVED***}{<NotFound fullPage />} />
        <Route path${DB_USER:***REMOVED***}{'/'} element${DB_USER:***REMOVED***}{<PublicLayout />}>
          <Route index element${DB_USER:***REMOVED***}{<WelcomePage />} />
          <Route path${DB_USER:***REMOVED***}{'/check-domain'} element${DB_USER:***REMOVED***}{<CheckDomainPage />} />
        </Route>
        <Route path${DB_USER:***REMOVED***}{'/auth'} element${DB_USER:***REMOVED***}{<AuthLayout />}>
          <Route index element${DB_USER:***REMOVED***}{<NotFound />} />
          <Route path${DB_USER:***REMOVED***}{'sign-in'} element${DB_USER:***REMOVED***}{<SignInPage />} />
          <Route path${DB_USER:***REMOVED***}{'sign-up'} element${DB_USER:***REMOVED***}{<SignUpPage />} />
          <Route path${DB_USER:***REMOVED***}{'forget-password'} element${DB_USER:***REMOVED***}{<ForgetPasswordPage />} />
          <Route path${DB_USER:***REMOVED***}{'check-email'} element${DB_USER:***REMOVED***}{<CheckEmailPage />} />
          <Route path${DB_USER:***REMOVED***}{'verify-email'} element${DB_USER:***REMOVED***}{<VerificateEmailPage />} />
          <Route path${DB_USER:***REMOVED***}{'2fa'} element${DB_USER:***REMOVED***}{<TFAPage />} />
          <Route path${DB_USER:***REMOVED***}{'2fa/totp'} element${DB_USER:***REMOVED***}{<TFATotpPage />} />
          <Route path${DB_USER:***REMOVED***}{'2fa/webauthn'} element${DB_USER:***REMOVED***}{<TFAWebAuthnPage />} />
        </Route>
        <Route path${DB_USER:***REMOVED***}{'/app'} element${DB_USER:***REMOVED***}{<AppLayout />}>
          <Route index element${DB_USER:***REMOVED***}{<DashboardPage />} />
          <Route path${DB_USER:***REMOVED***}{'domains'} element${DB_USER:***REMOVED***}{<DomainsPage />} />
          <Route path${DB_USER:***REMOVED***}{'events'} element${DB_USER:***REMOVED***}{<EventsPage />} />
          <Route path${DB_USER:***REMOVED***}{'cart'} element${DB_USER:***REMOVED***}{<CartPage />} />
          <Route path${DB_USER:***REMOVED***}{'dns'} element${DB_USER:***REMOVED***}{<DNSPage />} />
          <Route path${DB_USER:***REMOVED***}{'me'} element${DB_USER:***REMOVED***}{<ProfilePage />} />
        </Route>
        <Route path${DB_USER:***REMOVED***}{'/admin'} element${DB_USER:***REMOVED***}{<AdminLayout />}>
          <Route index element${DB_USER:***REMOVED***}{<AdminDashboardPage />} />
          <Route path${DB_USER:***REMOVED***}{'domains'} element${DB_USER:***REMOVED***}{<UsersDomainsPage />} />
          <Route path${DB_USER:***REMOVED***}{'users'} element${DB_USER:***REMOVED***}{<UsersPage />} />
          <Route path${DB_USER:***REMOVED***}{'events'} element${DB_USER:***REMOVED***}{<SystemEventsPage />} />
          <Route path${DB_USER:***REMOVED***}{'finances'} element${DB_USER:***REMOVED***}{<FinancesPage />} />
          <Route path${DB_USER:***REMOVED***}{'me'} element${DB_USER:***REMOVED***}{<ProfilePage />} />
          <Route path${DB_USER:***REMOVED***}{'zones'} element${DB_USER:***REMOVED***}{<ZonesPage />} />
        </Route>
      </Route>
    </Routes>
  );
};

export default Index;
