import React, { useState } from 'react';
import type { DomainResponse } from '../../api/models';
import { Button, Grid, GridItem, HStack, Text } from '@chakra-ui/react';
import DateText from '../DateText';
import Axios from 'axios';
import { getAccessToken } from '~/utils/authTokens';
import { PAYMENT_URL } from '~/api/Constants';

const PAYMENT_ID_STORAGE_KEY ${DB_USER:***REMOVED***} 'payment:lastId';

const MONTHLY_PRICE ${DB_USER:***REMOVED***} 200;
const YEARLY_DISCOUNT ${DB_USER:***REMOVED***} 0.7;

interface PaymentCreateResponse {
  paymentId: string;
  paymentUrl: string;
  operationId?: string;
  status?: string;
  amount?: number;
  currency?: string;
}

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
      const amountInRubles ${DB_USER:***REMOVED***} period ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'YEAR' 
        ? Math.round(MONTHLY_PRICE * 12 * YEARLY_DISCOUNT)
        : MONTHLY_PRICE;
      const amountInKopecks ${DB_USER:***REMOVED***} amountInRubles * 100;

      const token ${DB_USER:***REMOVED***} getAccessToken();
      const { data } ${DB_USER:***REMOVED***} await Axios.post<PaymentCreateResponse>(
        `${PAYMENT_URL}/`,
        {
          l3Domains: [fqdn],
          period,
          amount: amountInKopecks,
          currency: 'RUB',
          description: `Продление домена ${fqdn} на ${period ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'MONTH' ? '1 месяц' : '1 год'}`,
        },
        {
          headers: token ? { Authorization: `Bearer ${token}` } : {},
        }
      );

      if (data?.paymentUrl && data?.paymentId) {
        localStorage.setItem(PAYMENT_ID_STORAGE_KEY, data.paymentId);
        window.location.assign(data.paymentUrl);
      } else {
        throw new Error('Payment link missing');
      }
    } catch {
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
