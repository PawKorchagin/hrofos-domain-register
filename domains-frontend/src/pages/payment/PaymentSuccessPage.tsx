import { Button, Heading, HStack, Spinner, Stack, Text } from '@chakra-ui/react';
import { useCallback, useEffect, useMemo, useState } from 'react';
import { useLocation, useNavigate } from 'react-router';
import Axios from 'axios';
import { getAccessToken } from '~/utils/authTokens';

const PAYMENT_ID_STORAGE_KEY ${DB_USER:***REMOVED***} 'payment:lastId';

interface PaymentStatusResponse {
  paymentId: string;
  status: string;
  paid: boolean;
  domainsCreated: boolean;
  operationStatus?: string;
  paymentUrl?: string;
  amount?: number;
  currency?: string;
}

const PaymentSuccessPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const navigate ${DB_USER:***REMOVED***} useNavigate();
  const location ${DB_USER:***REMOVED***} useLocation();
  const [isLoading, setIsLoading] ${DB_USER:***REMOVED***} useState(true);
  const [status, setStatus] ${DB_USER:***REMOVED***} useState<PaymentStatusResponse | null>(null);
  const [error, setError] ${DB_USER:***REMOVED***} useState('');

  const paymentId ${DB_USER:***REMOVED***} useMemo(() ${DB_USER:***REMOVED***}> {
    const params ${DB_USER:***REMOVED***} new URLSearchParams(location.search);
    return params.get('paymentId') || localStorage.getItem(PAYMENT_ID_STORAGE_KEY);
  }, [location.search]);

  const checkPayment ${DB_USER:***REMOVED***} useCallback(async () ${DB_USER:***REMOVED***}> {
    if (!paymentId) {
      setError('Не удалось определить платеж.');
      setIsLoading(false);
      return;
    }

    setIsLoading(true);
    setError('');
    try {
      const token ${DB_USER:***REMOVED***} getAccessToken();
      const { data } ${DB_USER:***REMOVED***} await Axios.post<PaymentStatusResponse>(
        `/api/payments/${paymentId}/check`,
        {},
        {
          headers: token ? { Authorization: `Bearer ${token}` } : {},
        }
      );
      setStatus(data ?? null);
      if (data?.paid) {
        localStorage.removeItem(PAYMENT_ID_STORAGE_KEY);
      }
    } catch {
      setError('Не удалось проверить платеж.');
    } finally {
      setIsLoading(false);
    }
  }, [paymentId]);

  useEffect(() ${DB_USER:***REMOVED***}> {
    checkPayment();
  }, [checkPayment]);

  const getHeading ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
    if (status?.paid) return 'Платеж успешно завершен';
    if (status?.status ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'FAILED') return 'Платеж не прошел';
    return 'Платеж в обработке';
  };

  const getDescription ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
    if (status?.paid) return 'Домены будут добавлены в ваш аккаунт.';
    if (status?.status ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'FAILED') {
      return 'Оплата не была завершена. Если вы не завершили оплату на странице ЮKassa, попробуйте оплатить снова.';
    }
    if (status?.status ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'PENDING') {
      return 'Оплата еще не завершена. Если вы завершили оплату на странице ЮKassa, подождите несколько секунд и нажмите "Проверить снова".';
    }
    return 'Мы проверяем оплату. Попробуйте обновить статус через пару минут.';
  };

  return (
    <Stack minHeight${DB_USER:***REMOVED***}{'100dvh'} alignItems${DB_USER:***REMOVED***}{'center'} justifyContent${DB_USER:***REMOVED***}{'center'}>
      <Stack
        maxW${DB_USER:***REMOVED***}{'560px'}
        width${DB_USER:***REMOVED***}{'100%'}
        bg${DB_USER:***REMOVED***}{'accent.muted'}
        borderRadius${DB_USER:***REMOVED***}{'lg'}
        p${DB_USER:***REMOVED***}{8}
        gap${DB_USER:***REMOVED***}{4}
      >
        {isLoading ? (
          <HStack justifyContent${DB_USER:***REMOVED***}{'center'}>
            <Spinner color${DB_USER:***REMOVED***}{'secondary.solid'} />
            <Text>Проверяем оплату...</Text>
          </HStack>
        ) : (
          <>
            <Heading size${DB_USER:***REMOVED***}{'lg'}>{error ? 'Ошибка' : getHeading()}</Heading>
            <Text color${DB_USER:***REMOVED***}{'fg.muted'}>{error ? error : getDescription()}</Text>
            {status && !error && (
              <>
                <Text color${DB_USER:***REMOVED***}{'fg.subtle'}>
                  Статус: {status.status}
                  {status.operationStatus ? ` (${status.operationStatus})` : ''}
                </Text>
                {status.paymentUrl && status.status ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'PENDING' && (
                  <Button
                    colorPalette${DB_USER:***REMOVED***}{'accent'}
                    onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> window.location.assign(status.paymentUrl!)}
                  >
                    Вернуться к оплате
                  </Button>
                )}
              </>
            )}
            <HStack gap${DB_USER:***REMOVED***}{3} flexWrap${DB_USER:***REMOVED***}{'wrap'}>
              <Button
                colorPalette${DB_USER:***REMOVED***}{'secondary'}
                onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> navigate('/app/domains')}
                disabled${DB_USER:***REMOVED***}{!status?.paid}
              >
                К моим доменам
              </Button>
              <Button variant${DB_USER:***REMOVED***}{'subtle'} onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> navigate('/app/cart')}>
                В корзину
              </Button>
              <Button variant${DB_USER:***REMOVED***}{'outline'} onClick${DB_USER:***REMOVED***}{checkPayment}>
                Проверить снова
              </Button>
            </HStack>
          </>
        )}
      </Stack>
    </Stack>
  );
};

export default PaymentSuccessPage;
