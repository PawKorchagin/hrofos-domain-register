import { Button, Heading, Stack } from '@chakra-ui/react';
import { Clock, KeyRound } from 'lucide-react';
import React from 'react';
import { useNavigate } from 'react-router';

const TFAPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const navigate ${DB_USER:***REMOVED***} useNavigate();

  return (
    <Stack>
      <Heading>Выберите метод двухфакторной аутентификации</Heading>
      <Button colorPalette${DB_USER:***REMOVED***}{'accent'} onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> navigate('totp')}>
        <Clock />
        TOTP
      </Button>
      <Button colorPalette${DB_USER:***REMOVED***}{'accent'} onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> navigate('webauthn')}>
        <KeyRound /> Passkey
      </Button>
    </Stack>
  );
};

export default TFAPage;
