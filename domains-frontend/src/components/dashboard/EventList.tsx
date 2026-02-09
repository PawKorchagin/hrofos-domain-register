import React from 'react';
import type { DomainResponse } from '../../api/models';
import {
  Badge,
  Button,
  Grid,
  GridItem,
  HStack,
  Stack,
  Text,
} from '@chakra-ui/react';
import DateText from '../DateText';
import { ArrowRight, Proportions } from 'lucide-react';
import type { Dayjs } from 'dayjs';

type Props ${DB_USER:***REMOVED***} {
  events: {
    id: string;
    type: 'SYSTEM' | 'USER';
    message: string;
    at: string | Date | Dayjs;
  }[];
};

const EventList ${DB_USER:***REMOVED***} (props: Props) ${DB_USER:***REMOVED***}> {
  return (
    <Grid
      templateColumns${DB_USER:***REMOVED***}{'auto 1fr auto'}
      gap${DB_USER:***REMOVED***}{2}
      bg${DB_USER:***REMOVED***}{'accent.muted'}
      alignItems${DB_USER:***REMOVED***}{'center'}
      px${DB_USER:***REMOVED***}{5}
      py${DB_USER:***REMOVED***}{2.5}
      borderRadius${DB_USER:***REMOVED***}{'sm'}
    >
      {props.events.map((event) ${DB_USER:***REMOVED***}> (
        <React.Fragment key${DB_USER:***REMOVED***}{event.id}>
          <GridItem>
            <Badge colorPalette${DB_USER:***REMOVED***}{'secondary'}>
              {event.type ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'SYSTEM' ? 'Система' : 'Пользователь'}
            </Badge>
          </GridItem>
          <GridItem>{event.message}</GridItem>
          <GridItem>
            <DateText>{event.at}</DateText>
          </GridItem>
        </React.Fragment>
      ))}
    </Grid>
  );
};

export default EventList;
