import {
  Alert,
  Button,
  Field,
  Heading,
  HStack,
  Input,
  Stack,
} from '@chakra-ui/react';
import AppLink from '../../components/AppLink';
import type { AxiosError } from 'axios';
import { useNavigate, useSearchParams } from 'react-router';
import { useForm } from 'react-hook-form';
import { getUserById, login } from '~/api/services/auth';
import { setTokens } from '~/utils/authTokens';
import { useState } from 'react';
import { useStores } from '~/store';

const SignInPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [params, _] ${DB_USER:***REMOVED***} useSearchParams();
  const [error, setError] ${DB_USER:***REMOVED***} useState('');
  const [isSubmitting, setIsSubmitting] ${DB_USER:***REMOVED***} useState(false);
  const { userStore, authStore } ${DB_USER:***REMOVED***} useStores();

  const navigate ${DB_USER:***REMOVED***} useNavigate();
  const { register, handleSubmit } ${DB_USER:***REMOVED***} useForm<{
    email: string;
    password: string;
  }>();

  const onSubmit ${DB_USER:***REMOVED***} handleSubmit(async (formData) ${DB_USER:***REMOVED***}> {
    setIsSubmitting(true);
    setError('');

    try {
      const response ${DB_USER:***REMOVED***} await login({
        ...formData,
      });
      const accessToken ${DB_USER:***REMOVED***} response?.data?.accessToken;
      const refreshToken ${DB_USER:***REMOVED***} response?.data?.refreshToken;
      const userId ${DB_USER:***REMOVED***} response?.data?.userId;

      if (!accessToken || !refreshToken || !userId) {
        setError(
          response?.message ??
            'Не удалось войти в систему. Проверьте свои учетные данные и повторите попытку.'
        );
        return;
      }

      setTokens({ access: accessToken, refresh: refreshToken });
      const user ${DB_USER:***REMOVED***} await getUserById(userId);
      if (user.data) userStore.setUser(user.data);
      navigate('/app');
    } catch (e) {
      const apiError ${DB_USER:***REMOVED***} e as AxiosError<{ error: { message?: string, code?: string } }>;
      if (apiError.response?.data?.error?.code ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'TOTP_REQUIRED') {
        authStore.setEmail(formData.email);
        authStore.setPassword(formData.password);
        navigate('/auth/2fa/totp');
        return;
      }
      setError(
        apiError?.response?.data?.error?.message ??
          'Не удалось войти в систему. Проверьте свои учетные данные и повторите попытку.'
      );
    } finally {
      setIsSubmitting(false);
    }
  });

  return (
    <Stack>
      <Heading>Войти</Heading>

      {params.has('success') && (
        <Alert.Root status${DB_USER:***REMOVED***}{'success'}>
          <Alert.Indicator />
          <Alert.Content>
            <Alert.Title>Учетная запись подтверждена</Alert.Title>
            <Alert.Description>
              Не удалось войти в систему. Проверьте свои учетные данные и
              повторите попытку.
            </Alert.Description>
          </Alert.Content>
        </Alert.Root>
      )}

      <form onSubmit${DB_USER:***REMOVED***}{onSubmit}>
        <Stack gap${DB_USER:***REMOVED***}{3}>
          {error && (
            <Alert.Root status${DB_USER:***REMOVED***}{'error'}>
              <Alert.Indicator />
              <Alert.Content>
                <Alert.Description>{error}</Alert.Description>
              </Alert.Content>
            </Alert.Root>
          )}
          <Field.Root required>
            <Field.Label>
              Email <Field.RequiredIndicator />
            </Field.Label>
            <Input
              placeholder${DB_USER:***REMOVED***}"Введите свой адрес электронной почты"
              type${DB_USER:***REMOVED***}"email"
              {...register('email', { required: true })}
            />
          </Field.Root>
          <Field.Root required>
            <Field.Label>
              Пароль <Field.RequiredIndicator />
            </Field.Label>
            <Input
              placeholder${DB_USER:***REMOVED***}"Введите свой пароль"
              type${DB_USER:***REMOVED***}"password"
              {...register('password', { required: true })}
            />
          </Field.Root>
          <Button
            type${DB_USER:***REMOVED***}{'submit'}
            colorPalette${DB_USER:***REMOVED***}{'accent'}
            loading${DB_USER:***REMOVED***}{isSubmitting}
          >
            Войти
          </Button>
        </Stack>
      </form>
      <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
        <AppLink to${DB_USER:***REMOVED***}{'/auth/sign-up'}>Зарегистрироваться</AppLink>
        <AppLink to${DB_USER:***REMOVED***}{'/auth/forget-password'}>Забыли пароль</AppLink>
      </HStack>
    </Stack>
  );
};

export default SignInPage;
