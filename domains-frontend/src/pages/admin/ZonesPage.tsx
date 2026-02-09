import {
  Button,
  DialogBackdrop,
  DialogBody,
  DialogCloseTrigger,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogPositioner,
  DialogRoot,
  DialogTitle,
  DialogTrigger,
  Field,
  Grid,
  GridItem,
  Heading,
  HStack,
  Input,
  Spinner,
  Stack,
  Text,
} from '@chakra-ui/react';
import { Fragment, useCallback, useEffect, useState, type FormEvent } from 'react';
import { AXIOS_INSTANCE } from '~/api/apiClientDomains';

interface L2Domain {
  id?: number;
  name: string;
}

const ZonesPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [zones, setZones] ${DB_USER:***REMOVED***} useState<L2Domain[]>([]);
  const [loading, setLoading] ${DB_USER:***REMOVED***} useState(true);
  const [deletingName, setDeletingName] ${DB_USER:***REMOVED***} useState<string | null>(null);

  /* create dialog state */
  const [newName, setNewName] ${DB_USER:***REMOVED***} useState('');
  const [creating, setCreating] ${DB_USER:***REMOVED***} useState(false);
  const [createError, setCreateError] ${DB_USER:***REMOVED***} useState('');

  const loadZones ${DB_USER:***REMOVED***} useCallback(async () ${DB_USER:***REMOVED***}> {
    setLoading(true);
    try {
      const { data } ${DB_USER:***REMOVED***} await AXIOS_INSTANCE.get<L2Domain[]>('/l2Domains');
      setZones(data ?? []);
    } catch {
      setZones([]);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() ${DB_USER:***REMOVED***}> {
    loadZones();
  }, [loadZones]);

  const handleCreate ${DB_USER:***REMOVED***} async (e: FormEvent<HTMLFormElement>) ${DB_USER:***REMOVED***}> {
    e.preventDefault();
    const name ${DB_USER:***REMOVED***} newName.trim();
    if (!name) return;

    setCreating(true);
    setCreateError('');
    try {
      await AXIOS_INSTANCE.post('/l2Domains', { name });
      setNewName('');
      await loadZones();
    } catch {
      setCreateError('Не удалось создать зону');
    } finally {
      setCreating(false);
    }
  };

  const handleDelete ${DB_USER:***REMOVED***} async (name: string) ${DB_USER:***REMOVED***}> {
    setDeletingName(name);
    try {
      await AXIOS_INSTANCE.delete(`/l2Domains/${encodeURIComponent(name)}`);
      await loadZones();
    } catch {
      // ignore
    } finally {
      setDeletingName(null);
    }
  };

  return (
    <Stack gap${DB_USER:***REMOVED***}{5}>
      <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
        <Heading>Зоны 2-го уровня</Heading>
        <HStack>
          <Text>{zones.length} зон</Text>

          {/* Create dialog */}
          <DialogRoot>
            <DialogTrigger asChild>
              <Button colorPalette${DB_USER:***REMOVED***}{'secondary'} size${DB_USER:***REMOVED***}{'sm'}>Создать зону</Button>
            </DialogTrigger>
            <DialogBackdrop />
            <DialogPositioner>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle>Создать зону</DialogTitle>
                </DialogHeader>
                <DialogBody>
                  <form id${DB_USER:***REMOVED***}"create-zone-form" onSubmit${DB_USER:***REMOVED***}{handleCreate}>
                    <Stack gap${DB_USER:***REMOVED***}{4}>
                      <Field.Root required>
                        <Field.Label>Название зоны</Field.Label>
                        <Input
                          placeholder${DB_USER:***REMOVED***}"example.com"
                          value${DB_USER:***REMOVED***}{newName}
                          onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setNewName(e.target.value)}
                        />
                      </Field.Root>
                      {createError && <Text color${DB_USER:***REMOVED***}{'fg.error'} fontSize${DB_USER:***REMOVED***}{'sm'}>{createError}</Text>}
                    </Stack>
                  </form>
                </DialogBody>
                <DialogFooter>
                  <DialogCloseTrigger asChild>
                    <Button variant${DB_USER:***REMOVED***}{'ghost'}>Отменить</Button>
                  </DialogCloseTrigger>
                  <Button
                    colorPalette${DB_USER:***REMOVED***}{'accent'}
                    type${DB_USER:***REMOVED***}{'submit'}
                    form${DB_USER:***REMOVED***}"create-zone-form"
                    loading${DB_USER:***REMOVED***}{creating}
                    disabled${DB_USER:***REMOVED***}{creating || !newName.trim()}
                  >
                    Создать
                  </Button>
                </DialogFooter>
              </DialogContent>
            </DialogPositioner>
          </DialogRoot>
        </HStack>
      </HStack>

      {loading ? (
        <HStack><Spinner size${DB_USER:***REMOVED***}{'sm'} /><Text>Загрузка...</Text></HStack>
      ) : zones.length ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 0 ? (
        <Text color${DB_USER:***REMOVED***}{'fg.muted'}>Нет зон</Text>
      ) : (
        <Grid
          templateColumns${DB_USER:***REMOVED***}{'1fr auto'}
          rowGap${DB_USER:***REMOVED***}{2}
          bg${DB_USER:***REMOVED***}{'accent.muted'}
          alignItems${DB_USER:***REMOVED***}{'center'}
          px${DB_USER:***REMOVED***}{5}
          py${DB_USER:***REMOVED***}{2.5}
          borderRadius${DB_USER:***REMOVED***}{'sm'}
        >
          <GridItem><Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Зона</Text></GridItem>
          <GridItem />

          {zones.map((zone) ${DB_USER:***REMOVED***}> (
            <Fragment key${DB_USER:***REMOVED***}{zone.name}>
              <GridItem><Text>{zone.name}</Text></GridItem>
              <GridItem>
                <HStack justifyContent${DB_USER:***REMOVED***}{'flex-end'}>
                  <Button
                    size${DB_USER:***REMOVED***}{'xs'}
                    colorPalette${DB_USER:***REMOVED***}{'red'}
                    onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> handleDelete(zone.name)}
                    loading${DB_USER:***REMOVED***}{deletingName ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} zone.name}
                    disabled${DB_USER:***REMOVED***}{deletingName !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null && deletingName !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} zone.name}
                  >
                    удалить
                  </Button>
                </HStack>
              </GridItem>
            </Fragment>
          ))}
        </Grid>
      )}
    </Stack>
  );
};

export default ZonesPage;
