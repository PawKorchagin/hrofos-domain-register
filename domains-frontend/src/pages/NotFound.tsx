import { Heading, Link, Stack } from '@chakra-ui/react';
import React from 'react';
import { Link as RLink } from 'react-router';

type Props ${DB_USER:***REMOVED***} {
  fullPage?: boolean;
};

const NotFound ${DB_USER:***REMOVED***} (props: Props) ${DB_USER:***REMOVED***}> {
  return (
    <Stack
      gap${DB_USER:***REMOVED***}{2}
      justifyContent${DB_USER:***REMOVED***}{'center'}
      height${DB_USER:***REMOVED***}{props.fullPage ? '100dvh' : 'auto'}
      alignItems${DB_USER:***REMOVED***}{'center'}
    >
      <Heading fontSize${DB_USER:***REMOVED***}{'9xl'} lineHeight${DB_USER:***REMOVED***}{0.8}>
        404
      </Heading>
      <Heading>Такой страницы не существует</Heading>
      {/* @ts-ignore */}
      <Link as${DB_USER:***REMOVED***}{RLink} to${DB_USER:***REMOVED***}{'/'}>
        Вернуться на главную
      </Link>
    </Stack>
  );
};

export default NotFound;
