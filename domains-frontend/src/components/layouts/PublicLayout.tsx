import { Button, Heading, HStack, Stack } from '@chakra-ui/react';
import { Outlet, useNavigate } from 'react-router';
import AppLink from '../AppLink';

const PublicLayout ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const navigate ${DB_USER:***REMOVED***} useNavigate();

  return (
    <Stack height${DB_USER:***REMOVED***}{'100dvh'} justifyContent${DB_USER:***REMOVED***}{'space-between'}>
      <HStack
        justifyContent${DB_USER:***REMOVED***}"space-between"
        alignItems${DB_USER:***REMOVED***}"center"
        px${DB_USER:***REMOVED***}{10}
        py${DB_USER:***REMOVED***}{5}
        bg${DB_USER:***REMOVED***}{'bg'}
      >
        <Heading fontSize${DB_USER:***REMOVED***}{30}>
          <AppLink to${DB_USER:***REMOVED***}"/">Хрофорс Домены</AppLink>
        </Heading>
        <Button
          colorPalette${DB_USER:***REMOVED***}{'accent'}
          onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> navigate('/auth/sign-in')}
        >
          Войти или зарегистрироваться
        </Button>
      </HStack>
      <Outlet />
      <HStack gap${DB_USER:***REMOVED***}{5} px${DB_USER:***REMOVED***}{10} py${DB_USER:***REMOVED***}{5} bg${DB_USER:***REMOVED***}{'secondary.subtle'}>
        <AppLink to${DB_USER:***REMOVED***}{'/todo'}>Контакты</AppLink>
        <AppLink to${DB_USER:***REMOVED***}{'/todo'}>Политика конфиденциальности</AppLink>
        <AppLink to${DB_USER:***REMOVED***}{'/todo'}>Правила использования</AppLink>
      </HStack>
    </Stack>
  );
};

export default PublicLayout;
