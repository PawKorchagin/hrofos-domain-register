import { Button, Heading, HStack, Stack, Text } from '@chakra-ui/react';
import { useCallback, useEffect, useState } from 'react';
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

  const loadDomains ${DB_USER:***REMOVED***} useCallback(async () ${DB_USER:***REMOVED***}> {
    try {
      const { data } ${DB_USER:***REMOVED***} await AXIOS_INSTANCE.get<UserDomainDetailed[]>('/userDomains/detailed');
      const mapped: DomainResponse[] ${DB_USER:***REMOVED***} (data ?? []).map((d) ${DB_USER:***REMOVED***}> ({
        id: d.id?.toString(),
        fqdn: d.fqdn,
        zoneName: d.zoneName,
        activatedAt: d.activatedAt,
        expiresAt: d.expiresAt,
      }));
      setDomains(mapped);
    } catch {
      setDomains([]);
    }
  }, []);

  useEffect(() ${DB_USER:***REMOVED***}> {
    loadDomains();
  }, [loadDomains]);

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
      <DomainList domains${DB_USER:***REMOVED***}{domains} onRenewed${DB_USER:***REMOVED***}{loadDomains} />
    </Stack>
  );
};

export default DomainsPage;
