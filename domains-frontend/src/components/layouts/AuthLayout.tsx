import { Stack } from '@chakra-ui/react';
import { Outlet } from 'react-router';

const AuthLayout ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  return (
    <Stack
      height${DB_USER:***REMOVED***}{'100dvh'}
      justifyContent${DB_USER:***REMOVED***}{'center'}
      alignItems${DB_USER:***REMOVED***}{'center'}
      bg${DB_USER:***REMOVED***}{'secondary.subtle'}
    >
      <Stack p${DB_USER:***REMOVED***}{10} bg${DB_USER:***REMOVED***}{'bg'} borderRadius${DB_USER:***REMOVED***}{'md'} minW${DB_USER:***REMOVED***}{'40em'} gap${DB_USER:***REMOVED***}{5}>
        <Outlet />
      </Stack>
    </Stack>
  );
};

export default AuthLayout;
