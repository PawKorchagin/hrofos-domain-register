import { Stack, Heading, Spinner, VStack, Text } from '@chakra-ui/react';
import type { AxiosError } from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router';
import { verifyEmail } from '~/api/services/auth';

const VerificateEmailPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [param, _] ${DB_USER:***REMOVED***} useSearchParams();
  const token ${DB_USER:***REMOVED***} param.get('token');
  const [loading, setLoading] ${DB_USER:***REMOVED***} useState(true);
  const [error, setError] ${DB_USER:***REMOVED***} useState<string>('');
  const navigate ${DB_USER:***REMOVED***} useNavigate();

  useEffect(() ${DB_USER:***REMOVED***}> {
    if (!token) {
      setError(
        'Проверка не удалась. Ссылка может быть недействительной или истекшим сроком действия.'
      );
      setLoading(false);
      return;
    }

    verifyEmail({ token: token })
      .then(() ${DB_USER:***REMOVED***}> {
        navigate('/auth/sign-in?success');
      })
      .catch((e: AxiosError<{ message: string }>) ${DB_USER:***REMOVED***}> {
        setError(
          e?.response?.data?.message ??
            'Проверка не удалась. Ссылка может быть недействительной или истекшим сроком действия.'
        );
        setLoading(false);
      });
  }, [navigate, token]);

  return (
    <Stack>
      <Heading>Подтверждение адреса электронной почты</Heading>
      {loading ? (
        <VStack mt${DB_USER:***REMOVED***}{4}>
          <Spinner color${DB_USER:***REMOVED***}{'secondary.solid'} size${DB_USER:***REMOVED***}{'lg'} />
        </VStack>
      ) : (
        <>
          <Text>Мы не смогли подтвердить ваш адрес электронной почты.</Text>
          <Text>{error}</Text>
        </>
      )}
    </Stack>
  );
};

export default VerificateEmailPage;
