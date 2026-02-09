import React from 'react';
import type { DomainResponse } from '../../api/models';
import { Button, Grid, GridItem, HStack, Stack, Text } from '@chakra-ui/react';
import DateText from '../DateText';
import { ArrowRight } from 'lucide-react';

type Props ${DB_USER:***REMOVED***} {
  domains: DomainResponse[];
};

const DomainList ${DB_USER:***REMOVED***} (props: Props) ${DB_USER:***REMOVED***}> {
  return (
    <Grid
      templateColumns${DB_USER:***REMOVED***}{'50% 20% auto'}
      rowGap${DB_USER:***REMOVED***}{2}
      bg${DB_USER:***REMOVED***}{'accent.muted'}
      alignItems${DB_USER:***REMOVED***}{'center'}
      px${DB_USER:***REMOVED***}{5}
      py${DB_USER:***REMOVED***}{2.5}
      borderRadius${DB_USER:***REMOVED***}{'sm'}
    >
      {props.domains.map((domain) ${DB_USER:***REMOVED***}> (
        <React.Fragment key${DB_USER:***REMOVED***}{domain.id}>
          <GridItem key${DB_USER:***REMOVED***}{domain.id}>
            <Text>{domain.fqdn}</Text>
          </GridItem>
          <GridItem>
            <Text>
              до <DateText as${DB_USER:***REMOVED***}{'span'}>{domain.expiresAt}</DateText>
            </Text>
          </GridItem>
          <GridItem>
            <HStack justifyContent${DB_USER:***REMOVED***}{'flex-end'}>
              <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'secondary'}>
                продлить
              </Button>
              <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'secondary'}>
                DNS <ArrowRight />
              </Button>
            </HStack>
          </GridItem>
        </React.Fragment>
      ))}
    </Grid>
  );
};

export default DomainList;
