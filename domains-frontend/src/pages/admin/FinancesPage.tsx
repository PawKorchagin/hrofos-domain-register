import {
  Heading,
  HStack,
  Spinner,
  Stack,
  Text,
} from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import { AXIOS_INSTANCE } from '~/api/apiClientDomains';

const FinancesPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [domainsCount, setDomainsCount] ${DB_USER:***REMOVED***} useState<number | null>(null);
  const [loading, setLoading] ${DB_USER:***REMOVED***} useState(true);

  useEffect(() ${DB_USER:***REMOVED***}> {
    (async () ${DB_USER:***REMOVED***}> {
      try {
        const { data } ${DB_USER:***REMOVED***} await AXIOS_INSTANCE.get<{
          activeUsersCount: number;
          registeredDomainsCount: number;
        }>('/stats');
        setDomainsCount(data?.registeredDomainsCount ?? 0);
      } catch {
        // ignore
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  const monthlyPrice ${DB_USER:***REMOVED***} 200;
  const monthlyRevenue ${DB_USER:***REMOVED***} (domainsCount ?? 0) * monthlyPrice;

  return (
    <Stack gap${DB_USER:***REMOVED***}{5}>
      <Heading>Финансы</Heading>
      {loading ? (
        <HStack><Spinner size${DB_USER:***REMOVED***}{'sm'} /><Text>Загрузка...</Text></HStack>
      ) : (
        <Stack bg${DB_USER:***REMOVED***}{'accent.muted'} p${DB_USER:***REMOVED***}{5} borderRadius${DB_USER:***REMOVED***}{'md'} gap${DB_USER:***REMOVED***}{3}>
          <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Активных доменов</Text>
            <Text fontSize${DB_USER:***REMOVED***}{'xl'}>{domainsCount ?? '—'}</Text>
          </HStack>
          <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Ежемесячный доход (оценка)</Text>
            <Text fontSize${DB_USER:***REMOVED***}{'xl'}>{monthlyRevenue} ₽</Text>
          </HStack>
          <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Годовой доход (оценка)</Text>
            <Text fontSize${DB_USER:***REMOVED***}{'xl'}>{monthlyRevenue * 12} ₽</Text>
          </HStack>
        </Stack>
      )}
    </Stack>
  );
};

export default FinancesPage;
