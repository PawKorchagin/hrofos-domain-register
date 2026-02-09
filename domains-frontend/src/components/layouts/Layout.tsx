import { Provider } from '../ui/provider.tsx';
import { Outlet } from 'react-router';
import { Toaster } from '../ui/toaster.tsx';
import 'dayjs/locale/ru';
import { StoreProvider } from '~/store/index.tsx';

const Layout ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  return (
    <StoreProvider>
      <Provider>
        <Toaster />
        <Outlet />
      </Provider>
    </StoreProvider>
  );
};

export default Layout;
