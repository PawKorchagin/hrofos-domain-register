import {
  Heading,
  HStack,
  Spinner,
  Stack,
  Text,
} from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import Axios from 'axios';
import { getAccessToken } from '~/utils/authTokens';
import { AXIOS_INSTANCE } from '~/api/apiClientDomains';

const UsersPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [usersCount, setUsersCount] ${DB_USER:***REMOVED***} useState<number | null>(null);
  const [activeUsers, setActiveUsers] ${DB_USER:***REMOVED***} useState<number | null>(null);
  const [loading, setLoading] ${DB_USER:***REMOVED***} useState(true);

  useEffect(() ${DB_USER:***REMOVED***}> {
    (async () ${DB_USER:***REMOVED***}> {
      try {
        const token ${DB_USER:***REMOVED***} getAccessToken();
        const headers ${DB_USER:***REMOVED***} token ? { Authorization: `Bearer ${token}` } : {};

        const [usersRes, statsRes] ${DB_USER:***REMOVED***} await Promise.all([
          Axios.get<{ success: boolean; data: number }>('/api/auth/stats/users-count', { headers }),
          AXIOS_INSTANCE.get<{ activeUsersCount: number; registeredDomainsCount: number }>('/stats'),
        ]);

        setUsersCount(usersRes.data?.data ?? 0);
        setActiveUsers(statsRes.data?.activeUsersCount ?? 0);
      } catch {
        // ignore
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  return (
    <Stack gap${DB_USER:***REMOVED***}{5}>
      <Heading>Пользователи</Heading>
      {loading ? (
        <HStack><Spinner size${DB_USER:***REMOVED***}{'sm'} /><Text>Загрузка...</Text></HStack>
      ) : (
        <Stack bg${DB_USER:***REMOVED***}{'accent.muted'} p${DB_USER:***REMOVED***}{5} borderRadius${DB_USER:***REMOVED***}{'md'} gap${DB_USER:***REMOVED***}{3}>
          <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Всего зарегистрировано</Text>
            <Text fontSize${DB_USER:***REMOVED***}{'xl'}>{usersCount ?? '—'}</Text>
          </HStack>
          <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Активных (с доменами)</Text>
            <Text fontSize${DB_USER:***REMOVED***}{'xl'}>{activeUsers ?? '—'}</Text>
          </HStack>
        </Stack>
      )}
    </Stack>
  );
};

export default UsersPage;
