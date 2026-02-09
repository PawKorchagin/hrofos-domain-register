import { Table } from '@chakra-ui/react';
import React, { type ReactNode } from 'react';
import type { DomainQuery } from '~/api/models/DomainQuery';
import DomainRow from './DomainRow';

type Props ${DB_USER:***REMOVED***} {
  domains: DomainQuery[];
  buttonsFunction?: (domain: DomainQuery) ${DB_USER:***REMOVED***}> ReactNode;
};

const DomainsTable ${DB_USER:***REMOVED***} (props: Props) ${DB_USER:***REMOVED***}> {
  return (
    <Table.Root striped tableLayout${DB_USER:***REMOVED***}"fixed" width${DB_USER:***REMOVED***}"100%">
      <Table.Caption />
      <Table.Header>
        <Table.Row>
          <Table.ColumnHeader fontWeight${DB_USER:***REMOVED***}{'bold'} width${DB_USER:***REMOVED***}"60%">
            Домен
          </Table.ColumnHeader>
          <Table.ColumnHeader fontWeight${DB_USER:***REMOVED***}{'bold'} width${DB_USER:***REMOVED***}"20%">
            Цена
          </Table.ColumnHeader>
          <Table.ColumnHeader width${DB_USER:***REMOVED***}"20%" />
        </Table.Row>
      </Table.Header>
      <Table.Body>
        {props.domains.map((domain) ${DB_USER:***REMOVED***}> (
          <DomainRow
            key${DB_USER:***REMOVED***}{domain.fqdn}
            domain${DB_USER:***REMOVED***}{domain}
            buttonsFunction${DB_USER:***REMOVED***}{props.buttonsFunction}
          />
        ))}
      </Table.Body>
    </Table.Root>
  );
};

export default DomainsTable;
