import { Button, Heading, HStack, Stack, Text } from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import type { DomainResponse } from '~/api/models/domain-order';
import { AXIOS_INSTANCE } from '~/api/apiClientDomains';
import DomainList from '../../components/dashboard/DomainList';

interface UserDomainDetailed {
  id?: number;
  fqdn?: string;
  zoneName?: string;
  activatedAt?: string;
  expiresAt?: string;
}

const DomainsPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [domains, setDomains] ${DB_USER:***REMOVED***} useState<DomainResponse[]>([]);

  useEffect(() ${DB_USER:***REMOVED***}> {
    let isMounted ${DB_USER:***REMOVED***} true;

    const loadDomains ${DB_USER:***REMOVED***} async () ${DB_USER:***REMOVED***}> {
      try {
        const { data } ${DB_USER:***REMOVED***} await AXIOS_INSTANCE.get<UserDomainDetailed[]>('/userDomains/detailed');
        if (isMounted) {
          const mapped: DomainResponse[] ${DB_USER:***REMOVED***} (data ?? []).map((d) ${DB_USER:***REMOVED***}> ({
            id: d.id?.toString(),
            fqdn: d.fqdn,
            zoneName: d.zoneName,
            activatedAt: d.activatedAt,
            expiresAt: d.expiresAt,
          }));
          setDomains(mapped);
        }
      } catch {
        if (isMounted) {
          setDomains([]);
        }
      }
    };

    loadDomains();

    return () ${DB_USER:***REMOVED***}> {
      isMounted ${DB_USER:***REMOVED***} false;
    };
  }, []);

  return (
    <Stack gap${DB_USER:***REMOVED***}{5}>
      <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
        <Heading>Мои домены</Heading>
        <HStack>
          <Text>{domains.length} доменов</Text>
          <Button colorPalette${DB_USER:***REMOVED***}{'secondary'} size${DB_USER:***REMOVED***}{'sm'}>
            купить новый
          </Button>
        </HStack>
      </HStack>
      <DomainList domains${DB_USER:***REMOVED***}{domains} />
    </Stack>
  );
};

export default DomainsPage;
