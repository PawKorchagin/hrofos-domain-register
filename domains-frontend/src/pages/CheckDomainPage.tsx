import { Button, HStack, Input, Stack, Table, Text } from '@chakra-ui/react';
import { useCallback, useEffect, useState, type FormEvent } from 'react';
import { useSearchParams } from 'react-router';
import DomainRow from '../components/domainsTable/DomainRow';
import { validateDomain } from '../utils/validateDomain';
import { $ok } from '../common/atoms';
import DomainsTable from '~/components/domainsTable/DomainsTable';

const CheckDomainPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [searchParams, _] ${DB_USER:***REMOVED***} useSearchParams();
  const query ${DB_USER:***REMOVED***} searchParams.get('q');
  const [input, setInput] ${DB_USER:***REMOVED***} useState(query ?? '');
  const [error, setError] ${DB_USER:***REMOVED***} useState('');

  const [domains, setDomains] ${DB_USER:***REMOVED***} useState(
    query
      ? ['.goip.pw', '.godns.pw', '.gofrom.pw'].map((domain) ${DB_USER:***REMOVED***}> ({
          fqdn: `${query}${domain}`,
          price: 50,
          free: Math.random() > 0.5,
        }))
      : []
  );

  const fetchDomains ${DB_USER:***REMOVED***} (domain: string) ${DB_USER:***REMOVED***}> {
    console.log('Fetch Domains');

    // TODO Fetch domains
  };

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
    [input]
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
          <Button fontSize${DB_USER:***REMOVED***}{20} p${DB_USER:***REMOVED***}{6} type${DB_USER:***REMOVED***}{'submit'} colorPalette${DB_USER:***REMOVED***}{'accent'}>
            Проверить
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
