import {
  Accordion,
  Badge,
  Box,
  Button,
  createListCollection,
  Field,
  Grid,
  GridItem,
  Heading,
  HStack,
  Input,
  Portal,
  Select,
  Spinner,
  Stack,
  Text,
} from '@chakra-ui/react';
import { useCallback, useEffect, useMemo, useState } from 'react';
import { AXIOS_INSTANCE } from '~/api/apiClientDomains';

/* ───── types ───── */

type DnsRecordType ${DB_USER:***REMOVED***} 'A' | 'AAAA' | 'NS' | 'MX' | 'TXT' | 'CNAME' | 'SOA';

interface DnsRecord {
  id: number;
  name: string;
  type: DnsRecordType;
  ttl: number;
  data: unknown;
}

interface UserDomain {
  id?: number;
  fqdn?: string;
}

/* ───── helpers ───── */

const DNS_TYPES: DnsRecordType[] ${DB_USER:***REMOVED***} ['A', 'AAAA', 'CNAME', 'TXT', 'MX', 'NS', 'SOA'];

const dnsTypesCollection ${DB_USER:***REMOVED***} createListCollection({
  items: DNS_TYPES.map((t) ${DB_USER:***REMOVED***}> ({ label: t, value: t })),
});

/** Human-readable representation of a record's data */
const formatData ${DB_USER:***REMOVED***} (type: DnsRecordType, data: unknown): string ${DB_USER:***REMOVED***}> {
  if (data ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null || data ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} undefined) return '';
  if (typeof data ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'string') return data;
  if (type ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'MX' && typeof data ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'object') {
    const mx ${DB_USER:***REMOVED***} data as { preference?: number; exchange?: string };
    return `${mx.preference ?? 0} ${mx.exchange ?? ''}`;
  }
  if (type ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'SOA' && typeof data ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'object') {
    const soa ${DB_USER:***REMOVED***} data as {
      mname?: string; rname?: string; serial?: number;
      refresh?: number; retry?: number; expire?: number; minimum?: number;
    };
    return `${soa.mname} ${soa.rname} ${soa.serial} ${soa.refresh} ${soa.retry} ${soa.expire} ${soa.minimum}`;
  }
  return JSON.stringify(data);
};

/* ───── component ───── */

const DNSPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  /* domain list */
  const [domains, setDomains] ${DB_USER:***REMOVED***} useState<UserDomain[]>([]);
  const [selectedFqdn, setSelectedFqdn] ${DB_USER:***REMOVED***} useState<string | null>(null);

  /* records */
  const [records, setRecords] ${DB_USER:***REMOVED***} useState<DnsRecord[]>([]);
  const [loadingRecords, setLoadingRecords] ${DB_USER:***REMOVED***} useState(false);

  /* add form */
  const [newType, setNewType] ${DB_USER:***REMOVED***} useState<DnsRecordType>('A');
  const [newName, setNewName] ${DB_USER:***REMOVED***} useState('');
  const [newTtl, setNewTtl] ${DB_USER:***REMOVED***} useState('300');
  // simple fields (A, AAAA, NS, TXT, CNAME)
  const [newData, setNewData] ${DB_USER:***REMOVED***} useState('');
  // MX fields
  const [mxPreference, setMxPreference] ${DB_USER:***REMOVED***} useState('10');
  const [mxExchange, setMxExchange] ${DB_USER:***REMOVED***} useState('');
  // SOA fields
  const [soaMname, setSoaMname] ${DB_USER:***REMOVED***} useState('');
  const [soaRname, setSoaRname] ${DB_USER:***REMOVED***} useState('');
  const [soaSerial, setSoaSerial] ${DB_USER:***REMOVED***} useState('1');
  const [soaRefresh, setSoaRefresh] ${DB_USER:***REMOVED***} useState('3600');
  const [soaRetry, setSoaRetry] ${DB_USER:***REMOVED***} useState('600');
  const [soaExpire, setSoaExpire] ${DB_USER:***REMOVED***} useState('1209600');
  const [soaMinimum, setSoaMinimum] ${DB_USER:***REMOVED***} useState('300');

  const [adding, setAdding] ${DB_USER:***REMOVED***} useState(false);
  const [deletingId, setDeletingId] ${DB_USER:***REMOVED***} useState<number | null>(null);
  const [error, setError] ${DB_USER:***REMOVED***} useState('');

  const domainCollection ${DB_USER:***REMOVED***} useMemo(
    () ${DB_USER:***REMOVED***}> createListCollection({ items: domains.map((d) ${DB_USER:***REMOVED***}> ({ label: d.fqdn ?? '', value: d.fqdn ?? '' })) }),
    [domains]
  );

  /* load user domains */
  useEffect(() ${DB_USER:***REMOVED***}> {
    (async () ${DB_USER:***REMOVED***}> {
      try {
        const { data } ${DB_USER:***REMOVED***} await AXIOS_INSTANCE.get<UserDomain[]>('/userDomains/detailed');
        setDomains(data ?? []);
      } catch { /* ignore */ }
    })();
  }, []);

  /* load records when domain changes */
  const loadRecords ${DB_USER:***REMOVED***} useCallback(async (fqdn: string) ${DB_USER:***REMOVED***}> {
    setLoadingRecords(true);
    setError('');
    try {
      const { data } ${DB_USER:***REMOVED***} await AXIOS_INSTANCE.get<DnsRecord[]>(
        `/l3Domains/${encodeURIComponent(fqdn)}/dnsRecords`
      );
      setRecords(data ?? []);
    } catch {
      setRecords([]);
    } finally {
      setLoadingRecords(false);
    }
  }, []);

  useEffect(() ${DB_USER:***REMOVED***}> {
    if (selectedFqdn) loadRecords(selectedFqdn);
    else setRecords([]);
  }, [selectedFqdn, loadRecords]);

  /* build request body for new record */
  const buildRecordBody ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
    const base ${DB_USER:***REMOVED***} { name: newName || selectedFqdn, type: newType, ttl: Number(newTtl) || 300 };

    switch (newType) {
      case 'A':
      case 'AAAA':
      case 'NS':
      case 'TXT':
      case 'CNAME':
        return { ...base, data: newData };
      case 'MX':
        return { ...base, data: { preference: Number(mxPreference) || 0, exchange: mxExchange } };
      case 'SOA':
        return {
          ...base,
          data: {
            mname: soaMname, rname: soaRname,
            serial: Number(soaSerial) || 1,
            refresh: Number(soaRefresh) || 3600,
            retry: Number(soaRetry) || 600,
            expire: Number(soaExpire) || 1209600,
            minimum: Number(soaMinimum) || 300,
          },
        };
      default:
        return { ...base, data: newData };
    }
  };

  const handleAdd ${DB_USER:***REMOVED***} async () ${DB_USER:***REMOVED***}> {
    if (!selectedFqdn) return;
    setAdding(true);
    setError('');
    try {
      await AXIOS_INSTANCE.post(`/l3Domains/${encodeURIComponent(selectedFqdn)}`, buildRecordBody());
      await loadRecords(selectedFqdn);
      setNewData('');
      setMxExchange('');
    } catch (e: unknown) {
      setError('Не удалось создать запись');
    } finally {
      setAdding(false);
    }
  };

  const handleDelete ${DB_USER:***REMOVED***} async (id: number) ${DB_USER:***REMOVED***}> {
    if (!selectedFqdn) return;
    setDeletingId(id);
    try {
      await AXIOS_INSTANCE.delete(`/dnsRecords/${id}`);
      await loadRecords(selectedFqdn);
    } catch {
      setError('Не удалось удалить запись');
    } finally {
      setDeletingId(null);
    }
  };

  /* ───── render ───── */

  const renderDataFields ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
    switch (newType) {
      case 'A':
        return (
          <Field.Root>
            <Field.Label>IPv4 адрес</Field.Label>
            <Input placeholder${DB_USER:***REMOVED***}"1.2.3.4" bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{newData} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setNewData(e.target.value)} />
          </Field.Root>
        );
      case 'AAAA':
        return (
          <Field.Root>
            <Field.Label>IPv6 адрес</Field.Label>
            <Input placeholder${DB_USER:***REMOVED***}"2001:db8::1" bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{newData} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setNewData(e.target.value)} />
          </Field.Root>
        );
      case 'CNAME':
        return (
          <Field.Root>
            <Field.Label>Каноническое имя (FQDN)</Field.Label>
            <Input placeholder${DB_USER:***REMOVED***}"alias.example.com" bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{newData} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setNewData(e.target.value)} />
          </Field.Root>
        );
      case 'TXT':
        return (
          <Field.Root>
            <Field.Label>Текст</Field.Label>
            <Input placeholder${DB_USER:***REMOVED***}"v${DB_USER:***REMOVED***}spf1 ..." bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{newData} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setNewData(e.target.value)} />
          </Field.Root>
        );
      case 'NS':
        return (
          <Field.Root>
            <Field.Label>Name Server (FQDN)</Field.Label>
            <Input placeholder${DB_USER:***REMOVED***}"ns1.example.com" bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{newData} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setNewData(e.target.value)} />
          </Field.Root>
        );
      case 'MX':
        return (
          <HStack gap${DB_USER:***REMOVED***}{4}>
            <Field.Root>
              <Field.Label>Приоритет</Field.Label>
              <Input type${DB_USER:***REMOVED***}"number" placeholder${DB_USER:***REMOVED***}"10" bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{mxPreference} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setMxPreference(e.target.value)} />
            </Field.Root>
            <Field.Root>
              <Field.Label>Mail Server (FQDN)</Field.Label>
              <Input placeholder${DB_USER:***REMOVED***}"mx1.example.com" bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{mxExchange} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setMxExchange(e.target.value)} />
            </Field.Root>
          </HStack>
        );
      case 'SOA':
        return (
          <Stack gap${DB_USER:***REMOVED***}{3}>
            <HStack gap${DB_USER:***REMOVED***}{4}>
              <Field.Root>
                <Field.Label>Primary NS (mname)</Field.Label>
                <Input placeholder${DB_USER:***REMOVED***}"ns1.example.com" bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{soaMname} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setSoaMname(e.target.value)} />
              </Field.Root>
              <Field.Root>
                <Field.Label>Email (rname)</Field.Label>
                <Input placeholder${DB_USER:***REMOVED***}"hostmaster.example.com" bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{soaRname} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setSoaRname(e.target.value)} />
              </Field.Root>
            </HStack>
            <HStack gap${DB_USER:***REMOVED***}{4}>
              <Field.Root>
                <Field.Label>Serial</Field.Label>
                <Input type${DB_USER:***REMOVED***}"number" bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{soaSerial} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setSoaSerial(e.target.value)} />
              </Field.Root>
              <Field.Root>
                <Field.Label>Refresh</Field.Label>
                <Input type${DB_USER:***REMOVED***}"number" bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{soaRefresh} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setSoaRefresh(e.target.value)} />
              </Field.Root>
              <Field.Root>
                <Field.Label>Retry</Field.Label>
                <Input type${DB_USER:***REMOVED***}"number" bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{soaRetry} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setSoaRetry(e.target.value)} />
              </Field.Root>
              <Field.Root>
                <Field.Label>Expire</Field.Label>
                <Input type${DB_USER:***REMOVED***}"number" bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{soaExpire} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setSoaExpire(e.target.value)} />
              </Field.Root>
              <Field.Root>
                <Field.Label>Minimum</Field.Label>
                <Input type${DB_USER:***REMOVED***}"number" bg${DB_USER:***REMOVED***}{'bg'} value${DB_USER:***REMOVED***}{soaMinimum} onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setSoaMinimum(e.target.value)} />
              </Field.Root>
            </HStack>
          </Stack>
        );
      default:
        return null;
    }
  };

  return (
    <Stack gap${DB_USER:***REMOVED***}{4}>
      <Heading>DNS</Heading>

      {/* domain selector */}
      <Select.Root
        collection${DB_USER:***REMOVED***}{domainCollection}
        onValueChange${DB_USER:***REMOVED***}{(details) ${DB_USER:***REMOVED***}> setSelectedFqdn(details.value[0] ?? null)}
      >
        <Select.HiddenSelect />
        <Select.Label>Выберите домен</Select.Label>
        <Select.Control>
          <Select.Trigger>
            <Select.ValueText placeholder${DB_USER:***REMOVED***}"Выберите домен" />
          </Select.Trigger>
          <Select.IndicatorGroup>
            <Select.Indicator />
          </Select.IndicatorGroup>
        </Select.Control>
        <Portal>
          <Select.Positioner>
            <Select.Content>
              {domainCollection.items.map((item) ${DB_USER:***REMOVED***}> (
                <Select.Item item${DB_USER:***REMOVED***}{item} key${DB_USER:***REMOVED***}{item.value}>
                  {item.label}
                  <Select.ItemIndicator />
                </Select.Item>
              ))}
            </Select.Content>
          </Select.Positioner>
        </Portal>
      </Select.Root>

      {!selectedFqdn && <Text color${DB_USER:***REMOVED***}{'fg.muted'}>Выберите домен для управления DNS-записями</Text>}

      {selectedFqdn && (
        <Accordion.Root multiple defaultValue${DB_USER:***REMOVED***}{['add', 'list']}>
          {/* ── Add record ── */}
          <Accordion.Item value${DB_USER:***REMOVED***}{'add'}>
            <Accordion.ItemTrigger>
              <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Добавить запись</Text>
              <Accordion.ItemIndicator />
            </Accordion.ItemTrigger>
            <Accordion.ItemContent>
              <Accordion.ItemBody>
                <Stack bg${DB_USER:***REMOVED***}{'accent.muted'} p${DB_USER:***REMOVED***}{5} borderRadius${DB_USER:***REMOVED***}{'md'} gap${DB_USER:***REMOVED***}{4}>
                  <HStack gap${DB_USER:***REMOVED***}{4}>
                    <Field.Root>
                      <Field.Label>Имя записи</Field.Label>
                      <Input
                        placeholder${DB_USER:***REMOVED***}{selectedFqdn}
                        bg${DB_USER:***REMOVED***}{'bg'}
                        value${DB_USER:***REMOVED***}{newName}
                        onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setNewName(e.target.value)}
                      />
                    </Field.Root>
                    <Field.Root>
                      <Field.Label>Тип</Field.Label>
                      <Select.Root
                        collection${DB_USER:***REMOVED***}{dnsTypesCollection}
                        value${DB_USER:***REMOVED***}{[newType]}
                        onValueChange${DB_USER:***REMOVED***}{(d) ${DB_USER:***REMOVED***}> setNewType((d.value[0] as DnsRecordType) ?? 'A')}
                      >
                        <Select.HiddenSelect />
                        <Select.Control>
                          <Select.Trigger bg${DB_USER:***REMOVED***}{'bg'}>
                            <Select.ValueText />
                          </Select.Trigger>
                          <Select.IndicatorGroup>
                            <Select.Indicator />
                          </Select.IndicatorGroup>
                        </Select.Control>
                        <Portal>
                          <Select.Positioner>
                            <Select.Content>
                              {dnsTypesCollection.items.map((t) ${DB_USER:***REMOVED***}> (
                                <Select.Item item${DB_USER:***REMOVED***}{t} key${DB_USER:***REMOVED***}{t.value}>
                                  {t.label}
                                  <Select.ItemIndicator />
                                </Select.Item>
                              ))}
                            </Select.Content>
                          </Select.Positioner>
                        </Portal>
                      </Select.Root>
                    </Field.Root>
                    <Field.Root>
                      <Field.Label>TTL</Field.Label>
                      <Input
                        type${DB_USER:***REMOVED***}"number"
                        bg${DB_USER:***REMOVED***}{'bg'}
                        value${DB_USER:***REMOVED***}{newTtl}
                        onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setNewTtl(e.target.value)}
                        width${DB_USER:***REMOVED***}{'100px'}
                      />
                    </Field.Root>
                  </HStack>

                  {renderDataFields()}

                  {error && <Text color${DB_USER:***REMOVED***}{'fg.error'} fontSize${DB_USER:***REMOVED***}{'sm'}>{error}</Text>}

                  <Box>
                    <Button
                      colorPalette${DB_USER:***REMOVED***}{'secondary'}
                      size${DB_USER:***REMOVED***}{'sm'}
                      onClick${DB_USER:***REMOVED***}{handleAdd}
                      loading${DB_USER:***REMOVED***}{adding}
                    >
                      Добавить
                    </Button>
                  </Box>
                </Stack>
              </Accordion.ItemBody>
            </Accordion.ItemContent>
          </Accordion.Item>

          {/* ── Record list ── */}
          <Accordion.Item value${DB_USER:***REMOVED***}{'list'}>
            <Accordion.ItemTrigger>
              <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Записи ({records.length})</Text>
              <Accordion.ItemIndicator />
            </Accordion.ItemTrigger>
            <Accordion.ItemContent>
              <Accordion.ItemBody>
                {loadingRecords ? (
                  <HStack justifyContent${DB_USER:***REMOVED***}{'center'} py${DB_USER:***REMOVED***}{5}><Spinner /></HStack>
                ) : records.length ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 0 ? (
                  <Text color${DB_USER:***REMOVED***}{'fg.muted'}>Нет DNS-записей</Text>
                ) : (
                  <Stack bg${DB_USER:***REMOVED***}{'accent.muted'} p${DB_USER:***REMOVED***}{5} borderRadius${DB_USER:***REMOVED***}{'md'} gap${DB_USER:***REMOVED***}{2}>
                    <Grid templateColumns${DB_USER:***REMOVED***}{'80px 1fr auto auto'} gap${DB_USER:***REMOVED***}{2} alignItems${DB_USER:***REMOVED***}{'center'}>
                      <GridItem><Text fontWeight${DB_USER:***REMOVED***}{'bold'} fontSize${DB_USER:***REMOVED***}{'sm'}>Тип</Text></GridItem>
                      <GridItem><Text fontWeight${DB_USER:***REMOVED***}{'bold'} fontSize${DB_USER:***REMOVED***}{'sm'}>Значение</Text></GridItem>
                      <GridItem><Text fontWeight${DB_USER:***REMOVED***}{'bold'} fontSize${DB_USER:***REMOVED***}{'sm'}>TTL</Text></GridItem>
                      <GridItem />
                    </Grid>
                    {records.map((rec) ${DB_USER:***REMOVED***}> (
                      <Grid
                        key${DB_USER:***REMOVED***}{rec.id}
                        templateColumns${DB_USER:***REMOVED***}{'80px 1fr auto auto'}
                        gap${DB_USER:***REMOVED***}{2}
                        alignItems${DB_USER:***REMOVED***}{'center'}
                        bg${DB_USER:***REMOVED***}{'bg'}
                        px${DB_USER:***REMOVED***}{3}
                        py${DB_USER:***REMOVED***}{2}
                        borderRadius${DB_USER:***REMOVED***}{'sm'}
                      >
                        <GridItem>
                          <Badge colorPalette${DB_USER:***REMOVED***}{'secondary'}>{rec.type}</Badge>
                        </GridItem>
                        <GridItem>
                          <Text fontSize${DB_USER:***REMOVED***}{'sm'} wordBreak${DB_USER:***REMOVED***}{'break-all'}>
                            {formatData(rec.type, rec.data)}
                          </Text>
                        </GridItem>
                        <GridItem>
                          <Text fontSize${DB_USER:***REMOVED***}{'sm'} color${DB_USER:***REMOVED***}{'fg.muted'}>{rec.ttl}s</Text>
                        </GridItem>
                        <GridItem>
                          <Button
                            size${DB_USER:***REMOVED***}{'xs'}
                            colorPalette${DB_USER:***REMOVED***}{'red'}
                            onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> handleDelete(rec.id)}
                            loading${DB_USER:***REMOVED***}{deletingId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} rec.id}
                            disabled${DB_USER:***REMOVED***}{deletingId !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null && deletingId !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} rec.id}
                          >
                            удалить
                          </Button>
                        </GridItem>
                      </Grid>
                    ))}
                  </Stack>
                )}
              </Accordion.ItemBody>
            </Accordion.ItemContent>
          </Accordion.Item>
        </Accordion.Root>
      )}
    </Stack>
  );
};

export default DNSPage;
