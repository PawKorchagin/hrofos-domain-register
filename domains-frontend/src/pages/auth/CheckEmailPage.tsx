import { Heading, Stack, Text } from '@chakra-ui/react';
import type { AxiosError } from 'axios';
import { observer } from 'mobx-react-lite';
import { useCallback, useEffect, useMemo, useState } from 'react';
import { resendVerificationEmail } from '~/api/services/auth';
import { toaster } from '~/components/ui/toaster';
import { useStores } from '~/store';

const CheckEmailPage ${DB_USER:***REMOVED***} observer(() ${DB_USER:***REMOVED***}> {
  const [wait, setWait] ${DB_USER:***REMOVED***} useState(120);
  const waitText ${DB_USER:***REMOVED***} useMemo(() ${DB_USER:***REMOVED***}> `Через ${wait} секунд`, [wait]);
  const { authStore: registrationStore } ${DB_USER:***REMOVED***} useStores();

  const handleClick ${DB_USER:***REMOVED***} useCallback(() ${DB_USER:***REMOVED***}> {
    if (wait > 0) return;
    resendVerificationEmail({ email: registrationStore.email })
      .then(() ${DB_USER:***REMOVED***}> {
        toaster.create({
          title: 'Отправлено подтверждающее письмо по электронной почте',
        });
      })
      .catch((e: AxiosError<{ message: string }>) ${DB_USER:***REMOVED***}> {
        toaster.create({
          title: 'Не удалось повторно отправить подтверждающее письмо',
          description: e?.response?.data?.message,
          type: 'error',
        });
      });
  }, [registrationStore.email, wait]);

  useEffect(() ${DB_USER:***REMOVED***}> {
    if (wait <${DB_USER:***REMOVED***} 0) return;
    const intervalId ${DB_USER:***REMOVED***} setInterval(() ${DB_USER:***REMOVED***}> {
      setWait((prev) ${DB_USER:***REMOVED***}> (prev > 0 ? prev - 1 : 0));
    }, 1000);

    return () ${DB_USER:***REMOVED***}> clearInterval(intervalId);
  }, [wait]);

  return (
    <Stack>
      <Heading>Проверьте свою электронную почту</Heading>
      <Text>
        Мы отправили подтверждающую ссылку на ваш адрес электронной почты.
        Перейдите по ссылке, чтобы завершить регистрацию.
      </Text>
      <Text>
        Если вы не получили электронное письмо, вы можете запросить новое.{' '}
        <Text
          onClick${DB_USER:***REMOVED***}{handleClick}
          style${DB_USER:***REMOVED***}{{ cursor: 'pointer' }}
          as${DB_USER:***REMOVED***}{'span'}
          textDecor${DB_USER:***REMOVED***}{'underline'}
          color${DB_USER:***REMOVED***}{wait > 0 ? 'fg.subtle' : 'secondary.solid'}
        >
          Повторно отправить электронное письмо
        </Text>{' '}
        {waitText}.
      </Text>
      <Text>
        Проверьте папку со спамом или попробуйте снова через несколько минут,
        если письмо не пришло.
      </Text>
    </Stack>
  );
});

export default CheckEmailPage;
