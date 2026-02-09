import { Button, HStack, Input, Spinner, Stack, Text } from '@chakra-ui/react';
import axios from 'axios';
import { useCallback, useEffect, useState, type FormEvent } from 'react';
import { useSearchParams } from 'react-router';
import type { DomainQuery } from '~/api/models/DomainQuery';
import { DOMAIN_ORDER_URL } from '~/api/Constants';
import { validateDomain } from '../utils/validateDomain';
import { $ok } from '../common/atoms';
import DomainsTable from '~/components/domainsTable/DomainsTable';

const MONTHLY_PRICE ${DB_USER:***REMOVED***} 200;

const CheckDomainPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [searchParams] ${DB_USER:***REMOVED***} useSearchParams();
  const query ${DB_USER:***REMOVED***} searchParams.get('q');
  const [input, setInput] ${DB_USER:***REMOVED***} useState(query ?? '');
  const [error, setError] ${DB_USER:***REMOVED***} useState('');
  const [loading, setLoading] ${DB_USER:***REMOVED***} useState(false);
  const [domains, setDomains] ${DB_USER:***REMOVED***} useState<DomainQuery[]>([]);

  const fetchDomains ${DB_USER:***REMOVED***} useCallback(async (name: string) ${DB_USER:***REMOVED***}> {
    setLoading(true);
    setError('');
    setDomains([]);

    try {
      const [freeRes, zonesRes] ${DB_USER:***REMOVED***} await Promise.all([
        axios.get<string[]>(`${DOMAIN_ORDER_URL}/l3Domains/${encodeURIComponent(name)}/free`),
        axios.get<{ name: string }[]>(`${DOMAIN_ORDER_URL}/l2Domains`),
      ]);

      const freeDomains ${DB_USER:***REMOVED***} new Set(freeRes.data ?? []);
      const zones ${DB_USER:***REMOVED***} zonesRes.data ?? [];

      const results: DomainQuery[] ${DB_USER:***REMOVED***} zones.map((zone) ${DB_USER:***REMOVED***}> {
        const fqdn ${DB_USER:***REMOVED***} `${name}.${zone.name}`;
        return {
          fqdn,
          price: MONTHLY_PRICE,
          free: freeDomains.has(fqdn),
        };
      });

      results.sort((a, b) ${DB_USER:***REMOVED***}> (a.free ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} b.free ? 0 : a.free ? -1 : 1));
      setDomains(results);
    } catch (e) {
      setError('Не удалось проверить доступность доменов');
      console.error(e);
    } finally {
      setLoading(false);
    }
  }, []);

  const handleSubmit ${DB_USER:***REMOVED***} useCallback(
    (e: FormEvent<HTMLFormElement>) ${DB_USER:***REMOVED***}> {
      e.preventDefault();

      const domain ${DB_USER:***REMOVED***} input.trim().toLocaleLowerCase();
      const [result, reason] ${DB_USER:***REMOVED***} validateDomain(domain);

      if (result ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} $ok) {
        fetchDomains(domain);
      } else {
        setError(reason);
      }
    },
    [input, fetchDomains]
  );

  useEffect(() ${DB_USER:***REMOVED***}> {
    if (!query) return;
    fetchDomains(query);
  }, []);

  return (
    <Stack px${DB_USER:***REMOVED***}{10} py${DB_USER:***REMOVED***}{5} flex${DB_USER:***REMOVED***}{1}>
      <form onSubmit${DB_USER:***REMOVED***}{handleSubmit}>
        <HStack>
          <Input
            fontSize${DB_USER:***REMOVED***}{20}
            p${DB_USER:***REMOVED***}{6}
            placeholder${DB_USER:***REMOVED***}"ваш домен"
            value${DB_USER:***REMOVED***}{input}
            onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> {
              setInput(e.target.value);
              setError('');
            }}
          />
          <Button fontSize${DB_USER:***REMOVED***}{20} p${DB_USER:***REMOVED***}{6} type${DB_USER:***REMOVED***}{'submit'} colorPalette${DB_USER:***REMOVED***}{'accent'} disabled${DB_USER:***REMOVED***}{loading}>
            {loading ? <Spinner size${DB_USER:***REMOVED***}"sm" /> : 'Проверить'}
          </Button>
        </HStack>
        {error && (
          <Text color${DB_USER:***REMOVED***}{'red.500'} fontSize${DB_USER:***REMOVED***}{'sm'}>
            {error}
          </Text>
        )}
      </form>
      <DomainsTable domains${DB_USER:***REMOVED***}{domains} />
    </Stack>
  );
};

export default CheckDomainPage;
