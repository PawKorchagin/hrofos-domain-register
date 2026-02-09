import { Stack } from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import AppLink from '../../components/AppLink';
import { ArrowRight } from 'lucide-react';
import DomainList from '../../components/dashboard/DomainList';
import EventList from '../../components/dashboard/EventList';
import { AXIOS_INSTANCE } from '~/api/apiClientDomains';
import type { DomainResponse } from '~/api/models/domain-order';
import Axios from 'axios';
import { getAccessToken } from '~/utils/authTokens';

interface UserDomainDetailed {
  id?: number;
  fqdn?: string;
  zoneName?: string;
  activatedAt?: string;
  expiresAt?: string;
}

interface AuditEventDto {
  id: number;
  description: string;
  eventTime: string;
}

const DashboardPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [domains, setDomains] ${DB_USER:***REMOVED***} useState<DomainResponse[]>([]);
  const [events, setEvents] ${DB_USER:***REMOVED***} useState<
    { id: string; type: 'SYSTEM' | 'USER'; message: string; at: string }[]
  >([]);

  useEffect(() ${DB_USER:***REMOVED***}> {
    let isMounted ${DB_USER:***REMOVED***} true;

    const loadDomains ${DB_USER:***REMOVED***} async () ${DB_USER:***REMOVED***}> {
      try {
        const { data } ${DB_USER:***REMOVED***} await AXIOS_INSTANCE.get<UserDomainDetailed[]>(
          '/userDomains/detailed'
        );
        if (isMounted) {
          setDomains(
            (data ?? []).map((d) ${DB_USER:***REMOVED***}> ({
              id: d.id?.toString(),
              fqdn: d.fqdn,
              zoneName: d.zoneName,
              activatedAt: d.activatedAt,
              expiresAt: d.expiresAt,
            }))
          );
        }
      } catch {
        if (isMounted) setDomains([]);
      }
    };

    const loadEvents ${DB_USER:***REMOVED***} async () ${DB_USER:***REMOVED***}> {
      try {
        const token ${DB_USER:***REMOVED***} getAccessToken();
        const { data } ${DB_USER:***REMOVED***} await Axios.get<AuditEventDto[]>(
          '/api/audit/events/my',
          {
            headers: token ? { Authorization: `Bearer ${token}` } : {},
          }
        );
        if (isMounted) {
          setEvents(
            (data ?? []).slice(0, 10).map((e) ${DB_USER:***REMOVED***}> ({
              id: e.id.toString(),
              type: 'SYSTEM' as const,
              message: e.description,
              at: e.eventTime,
            }))
          );
        }
      } catch {
        if (isMounted) setEvents([]);
      }
    };

    loadDomains();
    loadEvents();

    return () ${DB_USER:***REMOVED***}> {
      isMounted ${DB_USER:***REMOVED***} false;
    };
  }, []);

  return (
    <Stack gap${DB_USER:***REMOVED***}{10}>
      <Stack>
        <AppLink to${DB_USER:***REMOVED***}{'/app/domains'}>
          Мои домены <ArrowRight />{' '}
        </AppLink>
        <DomainList domains${DB_USER:***REMOVED***}{domains} />
      </Stack>

      <Stack>
        <AppLink to${DB_USER:***REMOVED***}{'/app/events'}>
          Мои события <ArrowRight />{' '}
        </AppLink>
        <EventList events${DB_USER:***REMOVED***}{events} />
      </Stack>
    </Stack>
  );
};

export default DashboardPage;
