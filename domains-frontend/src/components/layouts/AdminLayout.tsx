import {
  Box,
  Grid,
  GridItem,
  Heading,
  HStack,
  Spinner,
  Stack,
} from '@chakra-ui/react';
import { useEffect, useLayoutEffect, useRef, useState } from 'react';
import AppLink from '../AppLink';
import { Outlet, useNavigate } from 'react-router';
import { refreshToken as apiRefreshToken } from '~/api/services/auth';
import { getAccessToken, getRefreshToken, setTokens } from '~/utils/authTokens';

const AdminLayout ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const navigate ${DB_USER:***REMOVED***} useNavigate();
  const [isAuthReady, setIsAuthReady] ${DB_USER:***REMOVED***} useState(false);
  const headerRef ${DB_USER:***REMOVED***} useRef<HTMLDivElement>(null);
  const navRef ${DB_USER:***REMOVED***} useRef<HTMLDivElement>(null);

  useEffect(() ${DB_USER:***REMOVED***}> {
    let isActive ${DB_USER:***REMOVED***} true;

    const ensureAuth ${DB_USER:***REMOVED***} async () ${DB_USER:***REMOVED***}> {
      const accessToken ${DB_USER:***REMOVED***} getAccessToken();
      if (accessToken) {
        setIsAuthReady(true);
        return;
      }

      const refreshToken ${DB_USER:***REMOVED***} getRefreshToken();
      if (!refreshToken) {
        navigate('/');
        setIsAuthReady(true);
        return;
      }

      try {
        const response ${DB_USER:***REMOVED***} await apiRefreshToken({ refreshToken });
        const nextAccessToken ${DB_USER:***REMOVED***} response?.data?.accessToken;
        const nextRefreshToken ${DB_USER:***REMOVED***} response?.data?.refreshToken ?? refreshToken;

        if (!nextAccessToken) {
          navigate('/');
          setIsAuthReady(true);
          return;
        }

        setTokens({ access: nextAccessToken, refresh: nextRefreshToken });
        setIsAuthReady(true);
      } catch {
        if (isActive) {
          navigate('/');
        }
        setIsAuthReady(true);
      }
    };

    ensureAuth();

    return () ${DB_USER:***REMOVED***}> {
      isActive ${DB_USER:***REMOVED***} false;
    };
  }, [navigate]);

  useLayoutEffect(() ${DB_USER:***REMOVED***}> {
    if (navRef.current?.style.width)
      navRef.current.style.width ${DB_USER:***REMOVED***} headerRef.current?.offsetWidth
        ? `${headerRef.current?.offsetWidth}px`
        : 'auto';
  }, []);

  if (!isAuthReady) {
    return (
      <Stack height${DB_USER:***REMOVED***}"100dvh" alignItems${DB_USER:***REMOVED***}"center" justifyContent${DB_USER:***REMOVED***}"center">
        <Spinner color${DB_USER:***REMOVED***}{'secondary.solid'} size${DB_USER:***REMOVED***}{'lg'} />
      </Stack>
    );
  }

  return (
    <Grid
      height${DB_USER:***REMOVED***}{'100dvh'}
      templateColumns${DB_USER:***REMOVED***}{'auto 1fr'}
      templateRows${DB_USER:***REMOVED***}{'auto 1fr'}
    >
      <GridItem>
        <Heading fontSize${DB_USER:***REMOVED***}{30} p${DB_USER:***REMOVED***}{5} bg${DB_USER:***REMOVED***}{'accent.subtle'} ref${DB_USER:***REMOVED***}{headerRef}>
          <AppLink to${DB_USER:***REMOVED***}"/admin">Хрофорс Домены</AppLink>
        </Heading>
      </GridItem>
      <GridItem>
        <HStack
          bg${DB_USER:***REMOVED***}{'accent.muted'}
          width${DB_USER:***REMOVED***}{'100%'}
          height${DB_USER:***REMOVED***}{'100%'}
          px${DB_USER:***REMOVED***}{5}
          justifyContent${DB_USER:***REMOVED***}{'flex-end'}
          alignItems${DB_USER:***REMOVED***}{'center'}
        >
          <AppLink to${DB_USER:***REMOVED***}{'/admin/me'}>администратор</AppLink>
        </HStack>
      </GridItem>
      <GridItem>
        <Stack height${DB_USER:***REMOVED***}{'100%'} width${DB_USER:***REMOVED***}{'100%'} bg${DB_USER:***REMOVED***}{'accent.muted'} p${DB_USER:***REMOVED***}{5}>
          <AppLink to${DB_USER:***REMOVED***}{'/admin/domains'}>Домены</AppLink>
          <AppLink to${DB_USER:***REMOVED***}{'/admin/users'}>Пользователи</AppLink>
          <AppLink to${DB_USER:***REMOVED***}{'/admin/finances'}>Финансы</AppLink>
          <AppLink to${DB_USER:***REMOVED***}{'/admin/events'}>События</AppLink>
          <AppLink to${DB_USER:***REMOVED***}{'/admin/zones'}>Зоны</AppLink>
        </Stack>
      </GridItem>
      <GridItem>
        <Box width${DB_USER:***REMOVED***}{'100%'} height${DB_USER:***REMOVED***}{'100%'} overflow${DB_USER:***REMOVED***}{'auto'} p${DB_USER:***REMOVED***}{5}>
          <Outlet />
        </Box>
      </GridItem>
    </Grid>
  );
};

export default AdminLayout;
