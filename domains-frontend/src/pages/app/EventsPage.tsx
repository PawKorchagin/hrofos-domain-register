import { Heading, Stack } from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import EventList from '~/components/dashboard/EventList';
import Axios from 'axios';
import { getAccessToken } from '~/utils/authTokens';

interface AuditEventDto {
  id: number;
  description: string;
  eventTime: string;
}

const EventsPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [events, setEvents] ${DB_USER:***REMOVED***} useState<
    { id: string; type: 'SYSTEM' | 'USER'; message: string; at: string }[]
  >([]);

  useEffect(() ${DB_USER:***REMOVED***}> {
    let isMounted ${DB_USER:***REMOVED***} true;

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
            (data ?? []).map((e) ${DB_USER:***REMOVED***}> ({
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

    loadEvents();

    return () ${DB_USER:***REMOVED***}> {
      isMounted ${DB_USER:***REMOVED***} false;
    };
  }, []);

  return (
    <Stack gap${DB_USER:***REMOVED***}{4}>
      <Heading>События</Heading>
      <EventList events${DB_USER:***REMOVED***}{events} />
    </Stack>
  );
};

export default EventsPage;
