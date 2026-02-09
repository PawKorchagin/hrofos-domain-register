import type { ReactNode } from 'react';
import type { DomainQuery } from '../../api/models/DomainQuery';
import { Button, Table, Text } from '@chakra-ui/react';

type Props ${DB_USER:***REMOVED***} {
  domain: DomainQuery;
  buttonsFunction?: (domain: DomainQuery) ${DB_USER:***REMOVED***}> ReactNode;
};

const DomainRow ${DB_USER:***REMOVED***} (props: Props) ${DB_USER:***REMOVED***}> {
  const { domain, buttonsFunction } ${DB_USER:***REMOVED***} props;
  return (
    <Table.Row>
      <Table.Cell>{domain.fqdn}</Table.Cell>
      <Table.Cell>{domain.price}₽ / месяц</Table.Cell>
      <Table.Cell>
        {buttonsFunction ? (
          buttonsFunction(domain)
        ) : domain.free ? (
          <Button colorPalette${DB_USER:***REMOVED***}{'secondary'}>Добавить в корзину</Button>
        ) : (
          <Text>Домен занят</Text>
        )}
      </Table.Cell>
    </Table.Row>
  );
};

export default DomainRow;
