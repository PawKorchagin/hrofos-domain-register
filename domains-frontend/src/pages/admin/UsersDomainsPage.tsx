import {
  Button,
  Grid,
  GridItem,
  Heading,
  HStack,
  Spinner,
  Stack,
  Text,
} from '@chakra-ui/react';
import { useCallback, useEffect, useState } from 'react';
import DateText from '~/components/DateText';
import { AXIOS_INSTANCE } from '~/api/apiClientDomains';

interface ExpiringDomain {
  userId: string;
  domainName: string;
  finishedAt: string;
}

const UsersDomainsPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [expiring7, setExpiring7] ${DB_USER:***REMOVED***} useState<ExpiringDomain[]>([]);
  const [expiring30, setExpiring30] ${DB_USER:***REMOVED***} useState<ExpiringDomain[]>([]);
  const [loading, setLoading] ${DB_USER:***REMOVED***} useState(true);
  const [cleanupLoading, setCleanupLoading] ${DB_USER:***REMOVED***} useState(false);
  const [cleanupResult, setCleanupResult] ${DB_USER:***REMOVED***} useState<string | null>(null);

  const loadData ${DB_USER:***REMOVED***} useCallback(async () ${DB_USER:***REMOVED***}> {
    setLoading(true);
    try {
      const [res7, res30] ${DB_USER:***REMOVED***} await Promise.all([
        AXIOS_INSTANCE.get<ExpiringDomain[]>('/userDomains/expiring?days${DB_USER:***REMOVED***}7'),
        AXIOS_INSTANCE.get<ExpiringDomain[]>('/userDomains/expiring?days${DB_USER:***REMOVED***}30'),
      ]);
      setExpiring7(res7.data ?? []);
      setExpiring30(res30.data ?? []);
    } catch {
      // ignore
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() ${DB_USER:***REMOVED***}> {
    loadData();
  }, [loadData]);

  const handleCleanup ${DB_USER:***REMOVED***} async () ${DB_USER:***REMOVED***}> {
    setCleanupLoading(true);
    setCleanupResult(null);
    try {
      const { data } ${DB_USER:***REMOVED***} await AXIOS_INSTANCE.delete<number>('/userDomains/expired');
      setCleanupResult(`Удалено ${data} просроченных доменов`);
      await loadData();
    } catch {
      setCleanupResult('Ошибка при очистке');
    } finally {
      setCleanupLoading(false);
    }
  };

  const renderTable ${DB_USER:***REMOVED***} (domains: ExpiringDomain[]) ${DB_USER:***REMOVED***}> (
    <Grid
      templateColumns${DB_USER:***REMOVED***}{'1fr 1fr auto'}
      rowGap${DB_USER:***REMOVED***}{2}
      columnGap${DB_USER:***REMOVED***}{5}
      bg${DB_USER:***REMOVED***}{'accent.muted'}
      p${DB_USER:***REMOVED***}{5}
      borderRadius${DB_USER:***REMOVED***}{'md'}
      alignItems${DB_USER:***REMOVED***}{'center'}
    >
      <GridItem><Text fontWeight${DB_USER:***REMOVED***}{'bold'}>домен</Text></GridItem>
      <GridItem><Text fontWeight${DB_USER:***REMOVED***}{'bold'}>пользователь</Text></GridItem>
      <GridItem><Text fontWeight${DB_USER:***REMOVED***}{'bold'}>истекает</Text></GridItem>

      {domains.map((d, i) ${DB_USER:***REMOVED***}> (
        <>
          <GridItem key${DB_USER:***REMOVED***}{`${i}-name`}>{d.domainName}</GridItem>
          <GridItem key${DB_USER:***REMOVED***}{`${i}-user`}>
            <Text fontSize${DB_USER:***REMOVED***}{'sm'} color${DB_USER:***REMOVED***}{'fg.muted'}>{d.userId?.substring(0, 8)}...</Text>
          </GridItem>
          <GridItem key${DB_USER:***REMOVED***}{`${i}-date`}>
            {d.finishedAt && <DateText>{d.finishedAt}</DateText>}
          </GridItem>
        </>
      ))}
    </Grid>
  );

  return (
    <Stack gap${DB_USER:***REMOVED***}{5}>
      <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
        <Heading>Домены</Heading>
        <HStack>
          <Button
            size${DB_USER:***REMOVED***}{'sm'}
            colorPalette${DB_USER:***REMOVED***}{'red'}
            onClick${DB_USER:***REMOVED***}{handleCleanup}
            loading${DB_USER:***REMOVED***}{cleanupLoading}
          >
            Удалить просроченные
          </Button>
        </HStack>
      </HStack>

      {cleanupResult && <Text color${DB_USER:***REMOVED***}{'green.500'}>{cleanupResult}</Text>}

      {loading ? (
        <HStack><Spinner size${DB_USER:***REMOVED***}{'sm'} /><Text>Загрузка...</Text></HStack>
      ) : (
        <>
          <Stack>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Истекают в ближайшие 7 дней ({expiring7.length})</Text>
            {expiring7.length > 0 ? renderTable(expiring7) : <Text color${DB_USER:***REMOVED***}{'fg.muted'}>Нет</Text>}
          </Stack>

          <Stack>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Истекают в ближайшие 30 дней ({expiring30.length})</Text>
            {expiring30.length > 0 ? renderTable(expiring30) : <Text color${DB_USER:***REMOVED***}{'fg.muted'}>Нет</Text>}
          </Stack>
        </>
      )}
    </Stack>
  );
};

export default UsersDomainsPage;
