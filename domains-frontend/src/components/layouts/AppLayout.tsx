import {
  Box,
  Button,
  Grid,
  GridItem,
  Heading,
  HStack,
  Spinner,
  Stack,
  Text,
} from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import AppLink from '../AppLink';
import { Outlet, useNavigate } from 'react-router';
import { getAccessToken, getRefreshToken, setTokens } from '~/utils/authTokens';
import { refreshToken as apiRefreshToken } from '~/api/services/auth';
import { useStores } from '~/store';
import { isCurrentUserAdmin } from '~/utils/jwtUtils';
import { Shield } from 'lucide-react';

const AppLayout ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const navigate ${DB_USER:***REMOVED***} useNavigate();
  const [isAuthReady, setIsAuthReady] ${DB_USER:***REMOVED***} useState(false);
  const { userStore } ${DB_USER:***REMOVED***} useStores();
  const { fetchMe } ${DB_USER:***REMOVED***} userStore;

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
        fetchMe();
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
        <Heading fontSize${DB_USER:***REMOVED***}{30} p${DB_USER:***REMOVED***}{5} bg${DB_USER:***REMOVED***}{'accent.subtle'}>
          <AppLink to${DB_USER:***REMOVED***}"/app">Хрофорс Домены</AppLink>
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
          gap${DB_USER:***REMOVED***}{4}
        >
          {isCurrentUserAdmin() && (
            <Button
              size${DB_USER:***REMOVED***}{'sm'}
              colorPalette${DB_USER:***REMOVED***}{'red'}
              variant${DB_USER:***REMOVED***}{'subtle'}
              onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> navigate('/admin')}
            >
              <Shield size${DB_USER:***REMOVED***}{16} /> Админ-панель
            </Button>
          )}
          <AppLink to${DB_USER:***REMOVED***}{'/app/me'}>пользователь</AppLink>
          <Button
            size${DB_USER:***REMOVED***}{'sm'}
            variant${DB_USER:***REMOVED***}{'ghost'}
            onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> {
              setTokens({ access: '', refresh: '' });
              navigate('/');
            }}
          >
            Выйти
          </Button>
        </HStack>
      </GridItem>
      <GridItem>
        <Stack height${DB_USER:***REMOVED***}{'100%'} width${DB_USER:***REMOVED***}{'100%'} bg${DB_USER:***REMOVED***}{'accent.muted'} p${DB_USER:***REMOVED***}{5}>
          <AppLink to${DB_USER:***REMOVED***}{'/app/domains'}>Домены</AppLink>
          <AppLink to${DB_USER:***REMOVED***}{'/app/dns'}>DNS</AppLink>
          <AppLink to${DB_USER:***REMOVED***}{'/app/cart'}>Корзина</AppLink>
          <AppLink to${DB_USER:***REMOVED***}{'/app/events'}>События</AppLink>
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

export default AppLayout;
