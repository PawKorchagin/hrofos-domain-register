import {
  Button,
  Heading,
  HStack,
  Spinner,
  Stack,
  Text,
} from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import { AXIOS_INSTANCE } from '~/api/apiClientDomains';
import Axios from 'axios';
import { getAccessToken } from '~/utils/authTokens';

const authHeaders ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const token ${DB_USER:***REMOVED***} getAccessToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
};

const AdminDashboardPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [usersCount, setUsersCount] ${DB_USER:***REMOVED***} useState<number | null>(null);
  const [domainsCount, setDomainsCount] ${DB_USER:***REMOVED***} useState<number | null>(null);
  const [activeUsers, setActiveUsers] ${DB_USER:***REMOVED***} useState<number | null>(null);
  const [loading, setLoading] ${DB_USER:***REMOVED***} useState(true);
  const [reportLoading, setReportLoading] ${DB_USER:***REMOVED***} useState(false);

  useEffect(() ${DB_USER:***REMOVED***}> {
    const load ${DB_USER:***REMOVED***} async () ${DB_USER:***REMOVED***}> {
      setLoading(true);
      try {
        const [usersRes, statsRes] ${DB_USER:***REMOVED***} await Promise.all([
          Axios.get<{ success: boolean; data: number }>('/api/auth/stats/users-count', { headers: authHeaders() }),
          AXIOS_INSTANCE.get<{ activeUsersCount: number; registeredDomainsCount: number }>('/stats'),
        ]);
        setUsersCount(usersRes.data?.data ?? 0);
        setDomainsCount(statsRes.data?.registeredDomainsCount ?? 0);
        setActiveUsers(statsRes.data?.activeUsersCount ?? 0);
      } catch {
        // ignore
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const handleDownloadReport ${DB_USER:***REMOVED***} async () ${DB_USER:***REMOVED***}> {
    setReportLoading(true);
    try {
      const res ${DB_USER:***REMOVED***} await Axios.get('/api/admin/report', {
        headers: authHeaders(),
        responseType: 'blob',
      });
      const url ${DB_USER:***REMOVED***} URL.createObjectURL(res.data);
      const a ${DB_USER:***REMOVED***} document.createElement('a');
      a.href ${DB_USER:***REMOVED***} url;
      a.download ${DB_USER:***REMOVED***} 'report.md';
      a.click();
      URL.revokeObjectURL(url);
    } catch {
      // ignore
    } finally {
      setReportLoading(false);
    }
  };

  return (
    <Stack gap${DB_USER:***REMOVED***}{5}>
      <Heading>Админ-панель</Heading>

      {loading ? (
        <HStack><Spinner size${DB_USER:***REMOVED***}{'sm'} /> <Text>Загрузка статистики...</Text></HStack>
      ) : (
        <HStack gap${DB_USER:***REMOVED***}{5}>
          <Stack>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Всего пользователей</Text>
            <Stack
              width${DB_USER:***REMOVED***}{'12em'}
              bg${DB_USER:***REMOVED***}{'accent.muted'}
              borderRadius${DB_USER:***REMOVED***}{'md'}
              py${DB_USER:***REMOVED***}{5}
              alignItems${DB_USER:***REMOVED***}{'center'}
            >
              <Text fontSize${DB_USER:***REMOVED***}{'xl'}>{usersCount ?? '—'}</Text>
            </Stack>
          </Stack>

          <Stack>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Активных пользователей</Text>
            <Stack
              width${DB_USER:***REMOVED***}{'12em'}
              bg${DB_USER:***REMOVED***}{'accent.muted'}
              borderRadius${DB_USER:***REMOVED***}{'md'}
              py${DB_USER:***REMOVED***}{5}
              alignItems${DB_USER:***REMOVED***}{'center'}
            >
              <Text fontSize${DB_USER:***REMOVED***}{'xl'}>{activeUsers ?? '—'}</Text>
            </Stack>
          </Stack>

          <Stack>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Зарег. доменов</Text>
            <Stack
              width${DB_USER:***REMOVED***}{'12em'}
              bg${DB_USER:***REMOVED***}{'accent.muted'}
              borderRadius${DB_USER:***REMOVED***}{'md'}
              py${DB_USER:***REMOVED***}{5}
              alignItems${DB_USER:***REMOVED***}{'center'}
            >
              <Text fontSize${DB_USER:***REMOVED***}{'xl'}>{domainsCount ?? '—'}</Text>
            </Stack>
          </Stack>
        </HStack>
      )}

      <Stack alignItems${DB_USER:***REMOVED***}{'flex-start'} gap${DB_USER:***REMOVED***}{3}>
        <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Отчёт</Text>
        <Button
          colorPalette${DB_USER:***REMOVED***}{'accent'}
          onClick${DB_USER:***REMOVED***}{handleDownloadReport}
          loading${DB_USER:***REMOVED***}{reportLoading}
        >
          Скачать отчёт
        </Button>
      </Stack>
    </Stack>
  );
};

export default AdminDashboardPage;
