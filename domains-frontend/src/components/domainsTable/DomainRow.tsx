import type { ReactNode } from 'react';
import type { DomainQuery } from '../../api/models/DomainQuery';
import { Button, Table, Text } from '@chakra-ui/react';
import { getAccessToken } from '~/utils/authTokens';

type Props ${DB_USER:***REMOVED***} {
  domain: DomainQuery;
  buttonsFunction?: (domain: DomainQuery) ${DB_USER:***REMOVED***}> ReactNode;
};

const DomainRow ${DB_USER:***REMOVED***} (props: Props) ${DB_USER:***REMOVED***}> {
  const { domain, buttonsFunction } ${DB_USER:***REMOVED***} props;
  const isLoggedIn ${DB_USER:***REMOVED***} Boolean(getAccessToken());

  return (
    <Table.Row>
      <Table.Cell>{domain.fqdn}</Table.Cell>
      <Table.Cell>{domain.price}₽ / месяц</Table.Cell>
      <Table.Cell>
        {buttonsFunction ? (
          buttonsFunction(domain)
        ) : domain.free ? (
          isLoggedIn ? (
            <Button colorPalette${DB_USER:***REMOVED***}{'secondary'}>Добавить в корзину</Button>
          ) : (
            <Text color${DB_USER:***REMOVED***}{'green.500'} fontWeight${DB_USER:***REMOVED***}{'bold'}>Свободен</Text>
          )
        ) : (
          <Text>Домен занят</Text>
        )}
      </Table.Cell>
    </Table.Row>
  );
};

export default DomainRow;
