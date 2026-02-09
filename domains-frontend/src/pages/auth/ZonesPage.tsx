import { Grid, GridItem, Heading, HStack, Stack, Text } from '@chakra-ui/react';
import { Fragment, useCallback, useEffect, useState } from 'react';
import type { Zone } from '~/api/models/domain-order';
import { getAllZones } from '~/api/services/domain-order';
import CreateZoneDialog from '~/components/admin/zones/CreateZoneDialog';
import ManageZoneDialog from '~/components/admin/zones/ManageZoneDialog';

const ZonesPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [zones, setZones] ${DB_USER:***REMOVED***} useState<Zone[]>([]);
  const [isLoading, setIsLoading] ${DB_USER:***REMOVED***} useState(false);

  const loadZones ${DB_USER:***REMOVED***} useCallback(async () ${DB_USER:***REMOVED***}> {
    setIsLoading(true);
    try {
      const response ${DB_USER:***REMOVED***} await getAllZones({
        pageable: {
          page: 0,
          size: 50,
        },
      });
      setZones(response?.data?.content ?? []);
    } catch {
      setZones([]);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() ${DB_USER:***REMOVED***}> {
    loadZones();
  }, [loadZones]);

  return (
    <Stack gap${DB_USER:***REMOVED***}{5}>
      <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
        <Heading>Зоны 2-го уровня</Heading>
        <HStack>
          <Text>{zones.length} зон</Text>
          <CreateZoneDialog onCreated${DB_USER:***REMOVED***}{loadZones} />
        </HStack>
      </HStack>
      {isLoading ? (
        <Text>Загрузка...</Text>
      ) : (
        <Grid
          templateColumns${DB_USER:***REMOVED***}{'40% 20% auto'}
          rowGap${DB_USER:***REMOVED***}{2}
          bg${DB_USER:***REMOVED***}{'accent.muted'}
          alignItems${DB_USER:***REMOVED***}{'center'}
          px${DB_USER:***REMOVED***}{5}
          py${DB_USER:***REMOVED***}{2.5}
          borderRadius${DB_USER:***REMOVED***}{'sm'}
        >
          <GridItem>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Зона</Text>
          </GridItem>
          <GridItem>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Цена</Text>
          </GridItem>
          <GridItem />
          {zones.map((zone) ${DB_USER:***REMOVED***}> (
            <Fragment key${DB_USER:***REMOVED***}{zone.id ?? zone.name}>
              <GridItem>
                <Text>{zone.name ?? '-'}</Text>
              </GridItem>
              <GridItem>
                <Text>{`${zone.price ?? '-'} рублей / месяц`}</Text>
              </GridItem>
              <GridItem>
                <HStack justifyContent${DB_USER:***REMOVED***}{'flex-end'}>
                  <ManageZoneDialog zone${DB_USER:***REMOVED***}{zone} onUpdated${DB_USER:***REMOVED***}{loadZones} />
                </HStack>
              </GridItem>
            </Fragment>
          ))}
        </Grid>
      )}
    </Stack>
  );
};

export default ZonesPage;
