import { Grid, GridItem, Heading, HStack, Spinner, Stack, Text } from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import DateText from '~/components/DateText';
import Axios from 'axios';
import { getAccessToken } from '~/utils/authTokens';

interface AuditEvent {
  id: number;
  description: string;
  userId: string | null;
  eventTime: string;
}

const SystemEventsPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [events, setEvents] ${DB_USER:***REMOVED***} useState<AuditEvent[]>([]);
  const [loading, setLoading] ${DB_USER:***REMOVED***} useState(true);

  useEffect(() ${DB_USER:***REMOVED***}> {
    (async () ${DB_USER:***REMOVED***}> {
      try {
        const token ${DB_USER:***REMOVED***} getAccessToken();
        const { data } ${DB_USER:***REMOVED***} await Axios.get<AuditEvent[]>('/api/audit/events/all?limit${DB_USER:***REMOVED***}200', {
          headers: token ? { Authorization: `Bearer ${token}` } : {},
        });
        setEvents(data ?? []);
      } catch {
        setEvents([]);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  return (
    <Stack>
      <Heading>События ({events.length})</Heading>
      {loading ? (
        <HStack><Spinner size${DB_USER:***REMOVED***}{'sm'} /><Text>Загрузка...</Text></HStack>
      ) : events.length ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 0 ? (
        <Text color${DB_USER:***REMOVED***}{'fg.muted'}>Нет событий</Text>
      ) : (
        <Grid
          templateColumns${DB_USER:***REMOVED***}{'auto 1fr auto'}
          rowGap${DB_USER:***REMOVED***}{2}
          columnGap${DB_USER:***REMOVED***}{5}
          bg${DB_USER:***REMOVED***}{'accent.muted'}
          p${DB_USER:***REMOVED***}{5}
          borderRadius${DB_USER:***REMOVED***}{'md'}
          alignItems${DB_USER:***REMOVED***}{'center'}
        >
          <GridItem>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>пользователь</Text>
          </GridItem>
          <GridItem>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>действие</Text>
          </GridItem>
          <GridItem>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>дата</Text>
          </GridItem>

          {events.map((e) ${DB_USER:***REMOVED***}> (
            <>
              <GridItem key${DB_USER:***REMOVED***}{`${e.id}-user`}>
                <Text fontSize${DB_USER:***REMOVED***}{'sm'} color${DB_USER:***REMOVED***}{'fg.muted'}>
                  {e.userId ? e.userId.substring(0, 8) + '...' : 'система'}
                </Text>
              </GridItem>
              <GridItem key${DB_USER:***REMOVED***}{`${e.id}-desc`}>
                <Text fontSize${DB_USER:***REMOVED***}{'sm'}>{e.description}</Text>
              </GridItem>
              <GridItem key${DB_USER:***REMOVED***}{`${e.id}-time`}>
                {e.eventTime && <DateText>{e.eventTime}</DateText>}
              </GridItem>
            </>
          ))}
        </Grid>
      )}
    </Stack>
  );
};

export default SystemEventsPage;
