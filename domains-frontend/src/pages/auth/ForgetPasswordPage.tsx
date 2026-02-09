import {
  Button,
  Field,
  Heading,
  HStack,
  Input,
  Stack,
  Text,
} from '@chakra-ui/react';
import AppLink from '../../components/AppLink';
import { useCallback, useState, type FormEvent } from 'react';

const ForgetPasswordPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [email, setEmail] ${DB_USER:***REMOVED***} useState('');

  const handleSubmit ${DB_USER:***REMOVED***} useCallback(
    (e: FormEvent<HTMLFormElement>) ${DB_USER:***REMOVED***}> {
      e.preventDefault();
    },
    [email]
  );

  return (
    <Stack>
      <Heading>Сбросить пароль</Heading>
      <Text>
        Введите свой адрес электронной почты, и мы вышлем вам ссылку для сброса
        пароля.
      </Text>
      <form onSubmit${DB_USER:***REMOVED***}{handleSubmit}>
        <Stack gap${DB_USER:***REMOVED***}{4}>
          <Field.Root required>
            <Field.Label>
              Email <Field.RequiredIndicator />
            </Field.Label>
            <Input
              type${DB_USER:***REMOVED***}"email"
              placeholder${DB_USER:***REMOVED***}"Введите свой адрес электронной почты"
              required
              value${DB_USER:***REMOVED***}{email}
              onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setEmail(e.target.value)}
            />
          </Field.Root>
          <Button type${DB_USER:***REMOVED***}{'submit'} colorPalette${DB_USER:***REMOVED***}{'accent'}>
            Отправить ссылку для сброса
          </Button>
        </Stack>
      </form>
      <HStack justifyContent${DB_USER:***REMOVED***}{'flex-end'}>
        <AppLink to${DB_USER:***REMOVED***}{'/auth/sign-in'}>Войти</AppLink>
      </HStack>
    </Stack>
  );
};

export default ForgetPasswordPage;
