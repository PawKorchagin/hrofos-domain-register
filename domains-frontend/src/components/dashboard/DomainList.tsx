import React, { useState } from 'react';
import type { DomainResponse } from '../../api/models';
import { Button, Grid, GridItem, HStack, Text } from '@chakra-ui/react';
import DateText from '../DateText';
import { ORDER_AXIOS_INSTANCE } from '~/api/apiClientOrders';

type Props ${DB_USER:***REMOVED***} {
  domains: DomainResponse[];
  onRenewed?: () ${DB_USER:***REMOVED***}> void;
};

const DomainList ${DB_USER:***REMOVED***} (props: Props) ${DB_USER:***REMOVED***}> {
  const [renewingFqdn, setRenewingFqdn] ${DB_USER:***REMOVED***} useState<string | null>(null);
  const [renewedFqdns, setRenewedFqdns] ${DB_USER:***REMOVED***} useState<Set<string>>(new Set());

  const handleRenew ${DB_USER:***REMOVED***} async (fqdn: string, period: 'MONTH' | 'YEAR') ${DB_USER:***REMOVED***}> {
    setRenewingFqdn(fqdn);
    try {
      await ORDER_AXIOS_INSTANCE.post('/domains/renew', {
        l3Domains: [fqdn],
        period,
      });
      setRenewedFqdns((prev) ${DB_USER:***REMOVED***}> new Set(prev).add(fqdn));
      props.onRenewed?.();
    } catch {
      // ignore
    } finally {
      setRenewingFqdn(null);
    }
  };

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
      {props.domains.map((domain) ${DB_USER:***REMOVED***}> {
        const fqdn ${DB_USER:***REMOVED***} domain.fqdn ?? '';
        const isRenewing ${DB_USER:***REMOVED***} renewingFqdn ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} fqdn;
        const wasRenewed ${DB_USER:***REMOVED***} renewedFqdns.has(fqdn);

        return (
          <React.Fragment key${DB_USER:***REMOVED***}{domain.id ?? fqdn}>
            <GridItem>
              <Text>{fqdn}</Text>
            </GridItem>
            <GridItem>
              {domain.expiresAt ? (
                <Text>
                  до <DateText as${DB_USER:***REMOVED***}{'span'}>{domain.expiresAt}</DateText>
                </Text>
              ) : (
                <Text color${DB_USER:***REMOVED***}"fg.muted">—</Text>
              )}
            </GridItem>
            <GridItem>
              <HStack justifyContent${DB_USER:***REMOVED***}{'flex-end'}>
                {wasRenewed ? (
                  <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'green'} variant${DB_USER:***REMOVED***}{'solid'} disabled>
                    Продлён
                  </Button>
                ) : (
                  <>
                    <Button
                      size${DB_USER:***REMOVED***}{'sm'}
                      colorPalette${DB_USER:***REMOVED***}{'secondary'}
                      onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> handleRenew(fqdn, 'MONTH')}
                      loading${DB_USER:***REMOVED***}{isRenewing}
                      disabled${DB_USER:***REMOVED***}{renewingFqdn !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null && !isRenewing}
                    >
                      +1 мес
                    </Button>
                    <Button
                      size${DB_USER:***REMOVED***}{'sm'}
                      colorPalette${DB_USER:***REMOVED***}{'secondary'}
                      onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> handleRenew(fqdn, 'YEAR')}
                      loading${DB_USER:***REMOVED***}{isRenewing}
                      disabled${DB_USER:***REMOVED***}{renewingFqdn !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null && !isRenewing}
                    >
                      +1 год
                    </Button>
                  </>
                )}
              </HStack>
            </GridItem>
          </React.Fragment>
        );
      })}
    </Grid>
  );
};

export default DomainList;
