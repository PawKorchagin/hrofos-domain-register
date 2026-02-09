import {
  Alert,
  Button,
  Checkbox,
  Field,
  Heading,
  HStack,
  Input,
  Link,
  Stack,
} from '@chakra-ui/react';
import AppLink from '../../components/AppLink';
import type { AxiosError } from 'axios';
import { useNavigate } from 'react-router';
import { useForm } from 'react-hook-form';
import { register as apiRegister } from '~/api/services/auth';
import { useState } from 'react';
import { useStores } from '~/store';

const SignUpPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const navigate ${DB_USER:***REMOVED***} useNavigate();
  const [error, setError] ${DB_USER:***REMOVED***} useState('');
  const [isSubmitting, setIsSubmitting] ${DB_USER:***REMOVED***} useState(false);
  const { authStore: registrationStore } ${DB_USER:***REMOVED***} useStores();

  const { register, handleSubmit, getValues } ${DB_USER:***REMOVED***} useForm<{
    email: string;
    password: string;
    confirmPassword: string;
    policyAccepted: boolean;
    marketingAccepted: boolean;
  }>();

  const onSubmit ${DB_USER:***REMOVED***} handleSubmit(async (data) ${DB_USER:***REMOVED***}> {
    const email ${DB_USER:***REMOVED***} data.email.trim().toLowerCase();
    setIsSubmitting(true);
    setError('');

    try {
      await apiRegister({
        email,
        password: data.password,
      });
      registrationStore.setEmail(email);
      navigate('/auth/check-email', { state: { email } });
    } catch (e) {
      const apiError ${DB_USER:***REMOVED***} e as AxiosError<{ message?: string }>;
      setError(
        apiError?.response?.data?.message ??
          'Не удалось зарегистрироваться. Проверьте данные и попробуйте снова.'
      );
    } finally {
      setIsSubmitting(false);
    }
  });

  return (
    <Stack gap${DB_USER:***REMOVED***}{5}>
      <Heading>Регистрация</Heading>
      <form onSubmit${DB_USER:***REMOVED***}{onSubmit}>
        <Stack gap${DB_USER:***REMOVED***}{4}>
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
              placeholder${DB_USER:***REMOVED***}"Введите email"
              type${DB_USER:***REMOVED***}"email"
              {...register('email', { required: true })}
            />
          </Field.Root>
          <Field.Root required>
            <Field.Label>
              Пароль <Field.RequiredIndicator />
            </Field.Label>
            <Input
              placeholder${DB_USER:***REMOVED***}"Придумайте пароль"
              type${DB_USER:***REMOVED***}"password"
              {...register('password', { required: true })}
            />
          </Field.Root>
          <Field.Root required>
            <Field.Label>
              Подтверждение пароля <Field.RequiredIndicator />
            </Field.Label>
            <Input
              placeholder${DB_USER:***REMOVED***}"Повторите пароль"
              type${DB_USER:***REMOVED***}"password"
              {...register('confirmPassword', {
                required: true,
                validate: (value) ${DB_USER:***REMOVED***}> value ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} getValues('password'),
              })}
            />
          </Field.Root>

          <Checkbox.Root colorPalette${DB_USER:***REMOVED***}{'accent'}>
            <Checkbox.HiddenInput
              {...register('policyAccepted', { required: true })}
            />
            <Checkbox.Control />
            <Checkbox.Label>
              Я принимаю условия сервиса и политику конфиденциальности.{' '}
              <Link href${DB_USER:***REMOVED***}"" target${DB_USER:***REMOVED***}"_blank">
                Условия
              </Link>
            </Checkbox.Label>
          </Checkbox.Root>
          <Checkbox.Root colorPalette${DB_USER:***REMOVED***}{'accent'}>
            <Checkbox.HiddenInput
              {...register('marketingAccepted', { required: true })}
            />
            <Checkbox.Control />
            <Checkbox.Label>
              Хочу получать маркетинговые рассылки.{' '}
              <Link href${DB_USER:***REMOVED***}"" target${DB_USER:***REMOVED***}"_blank">
                Политика рассылок
              </Link>
            </Checkbox.Label>
          </Checkbox.Root>

          <Button
            type${DB_USER:***REMOVED***}{'submit'}
            colorPalette${DB_USER:***REMOVED***}{'accent'}
            loading${DB_USER:***REMOVED***}{isSubmitting}
          >
            Создать аккаунт
          </Button>
        </Stack>
      </form>
      <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
        <AppLink to${DB_USER:***REMOVED***}{'/auth/sign-in'}>Вход</AppLink>
      </HStack>
    </Stack>
  );
};

export default SignUpPage;
