import { Alert, Button, Heading, HStack, PinInput, Stack } from '@chakra-ui/react';
import type { AxiosError } from 'axios';
import { observer } from 'mobx-react-lite';
import { useEffect, useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router';
import { getUserById, login } from '~/api/services/auth';
import { useStores } from '~/store';
import { setTokens } from '~/utils/authTokens';

const TFATotpPage ${DB_USER:***REMOVED***} observer(() ${DB_USER:***REMOVED***}> {
  const [value, setValue] ${DB_USER:***REMOVED***} useState(['', '', '', '', '', '']);
  const [error, setError] ${DB_USER:***REMOVED***} useState('');
  const [isSubmitting, setIsSubmitting] ${DB_USER:***REMOVED***} useState(false);
  const { authStore, userStore } ${DB_USER:***REMOVED***} useStores();
  const { email, password } ${DB_USER:***REMOVED***} authStore;
  const navigate ${DB_USER:***REMOVED***} useNavigate();

  useEffect(() ${DB_USER:***REMOVED***}> {
    if (!email || !password) {
      navigate('/auth/sign-in');
    }
  }, [email, password, navigate]);

  const handleSubmit ${DB_USER:***REMOVED***} async (event: FormEvent<HTMLFormElement>) ${DB_USER:***REMOVED***}> {
    event.preventDefault();

    const totpCode ${DB_USER:***REMOVED***} value.join('').trim();
    if (totpCode.length !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 6) {
      setError('Введите 6-значный код.');
      return;
    }

    setIsSubmitting(true);
    setError('');

    try {
      const response ${DB_USER:***REMOVED***} await login({
        email,
        password,
        totpCode,
      });
      const accessToken ${DB_USER:***REMOVED***} response?.data?.accessToken;
      const refreshToken ${DB_USER:***REMOVED***} response?.data?.refreshToken;
      const userId ${DB_USER:***REMOVED***} response?.data?.userId;

      if (!accessToken || !refreshToken || !userId) {
        setError(response?.message ?? 'Не удалось войти в систему. Повторите попытку.');
        return;
      }

      setTokens({ access: accessToken, refresh: refreshToken });
      const user ${DB_USER:***REMOVED***} await getUserById(userId);
      if (user.data) userStore.setUser(user.data);
      authStore.setEmail('');
      authStore.setPassword('');
      navigate('/app');
    } catch (e) {
      const apiError ${DB_USER:***REMOVED***} e as AxiosError<{ message?: string }>;
      setError(apiError?.response?.data?.message ?? 'Не удалось войти в систему. Повторите попытку.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit${DB_USER:***REMOVED***}{handleSubmit}>
      <Stack gap${DB_USER:***REMOVED***}{5}>
        <Heading>Введите 6-значный код из приложения-аутентификатора.</Heading>
        {error && (
          <Alert.Root status${DB_USER:***REMOVED***}{'error'}>
            <Alert.Indicator />
            <Alert.Content>
              <Alert.Description>{error}</Alert.Description>
            </Alert.Content>
          </Alert.Root>
        )}
        <HStack justifyContent${DB_USER:***REMOVED***}{'center'}>
          <PinInput.Root
            size${DB_USER:***REMOVED***}"xl"
            otp
            attached
            value${DB_USER:***REMOVED***}{value}
            onValueChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setValue(e.value)}
          >
            <PinInput.HiddenInput />
            <PinInput.Control>
              <PinInput.Input index${DB_USER:***REMOVED***}{0} />
              <PinInput.Input index${DB_USER:***REMOVED***}{1} />
              <PinInput.Input index${DB_USER:***REMOVED***}{2} />
              <PinInput.Input index${DB_USER:***REMOVED***}{3} />
              <PinInput.Input index${DB_USER:***REMOVED***}{4} />
              <PinInput.Input index${DB_USER:***REMOVED***}{5} />
            </PinInput.Control>
          </PinInput.Root>
        </HStack>
        <Button type${DB_USER:***REMOVED***}{'submit'} colorPalette${DB_USER:***REMOVED***}{'accent'} loading${DB_USER:***REMOVED***}{isSubmitting}>
          Проверить
        </Button>
      </Stack>
    </form>
  );
});

export default TFATotpPage;
