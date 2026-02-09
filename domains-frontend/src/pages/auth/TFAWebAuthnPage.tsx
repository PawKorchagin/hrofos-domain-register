import { Button, Heading, Stack } from '@chakra-ui/react';
import { KeyRound } from 'lucide-react';
import React from 'react';

const TFAWebAuthnPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  return (
    <Stack>
      <Heading>Пройдите аутентификацию при помощи PassKey</Heading>
      <Button colorPalette${DB_USER:***REMOVED***}{'secondary'}>
        <KeyRound /> Аутентифицироваться
      </Button>
    </Stack>
  );
};

export default TFAWebAuthnPage;
