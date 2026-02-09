import { Button, Field, Input, Stack, Text } from '@chakra-ui/react';
import {
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
} from '@chakra-ui/react';
import { useEffect, useState, type FormEvent } from 'react';
import type { Zone } from '~/api/models/domain-order';
import { deleteZone, updateZone } from '~/api/services/domain-order';

type Props ${DB_USER:***REMOVED***} {
  zone: Zone;
  onUpdated?: () ${DB_USER:***REMOVED***}> void | Promise<void>;
};

const ManageZoneDialog ${DB_USER:***REMOVED***} ({ zone, onUpdated }: Props) ${DB_USER:***REMOVED***}> {
  const [price, setPrice] ${DB_USER:***REMOVED***} useState('');
  const [isSaving, setIsSaving] ${DB_USER:***REMOVED***} useState(false);
  const [isDeleting, setIsDeleting] ${DB_USER:***REMOVED***} useState(false);
  const [error, setError] ${DB_USER:***REMOVED***} useState('');

  useEffect(() ${DB_USER:***REMOVED***}> {
    setPrice(zone.price ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} undefined ? '' : String(zone.price));
    setError('');
  }, [zone.id, zone.price]);

  const handleSave ${DB_USER:***REMOVED***} async (event: FormEvent<HTMLFormElement>) ${DB_USER:***REMOVED***}> {
    event.preventDefault();

    if (!zone.id) {
      setError('Зона не найдена.');
      return;
    }

    const nextPrice ${DB_USER:***REMOVED***} Number(price);
    if (Number.isNaN(nextPrice)) {
      setError('Введите корректную цену.');
      return;
    }

    setIsSaving(true);
    setError('');

    try {
      await updateZone(zone.id, { price: nextPrice });
      if (onUpdated) {
        await onUpdated();
      }
    } catch {
      setError('Не удалось обновить зону. Попробуйте снова.');
    } finally {
      setIsSaving(false);
    }
  };

  const handleDelete ${DB_USER:***REMOVED***} async () ${DB_USER:***REMOVED***}> {
    if (!zone.id) {
      setError('Зона не найдена.');
      return;
    }

    setIsDeleting(true);
    setError('');

    try {
      await deleteZone(zone.id);
      if (onUpdated) {
        await onUpdated();
      }
    } catch {
      setError('Не удалось удалить зону. Попробуйте снова.');
    } finally {
      setIsDeleting(false);
    }
  };

  const isSubmitDisabled ${DB_USER:***REMOVED***}
    isSaving || isDeleting || !price.trim() || Number.isNaN(Number(price));
  const formId ${DB_USER:***REMOVED***} `manage-zone-form-${zone.id ?? zone.name ?? 'zone'}`;

  return (
    <DialogRoot>
      <DialogTrigger asChild>
        <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'secondary'}>
          управлять
        </Button>
      </DialogTrigger>
      <DialogBackdrop />
      <DialogPositioner>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Управление зоной</DialogTitle>
          </DialogHeader>
          <DialogBody>
            <form id${DB_USER:***REMOVED***}{formId} onSubmit${DB_USER:***REMOVED***}{handleSave}>
              <Stack gap${DB_USER:***REMOVED***}{4}>
                <Field.Root>
                  <Field.Label>Зона</Field.Label>
                  <Text>{zone.name ?? '-'}</Text>
                </Field.Root>
                <Field.Root required>
                  <Field.Label>Цена</Field.Label>
                  <Input
                    type${DB_USER:***REMOVED***}"number"
                    min${DB_USER:***REMOVED***}{0}
                    step${DB_USER:***REMOVED***}"0.01"
                    value${DB_USER:***REMOVED***}{price}
                    onChange${DB_USER:***REMOVED***}{(event) ${DB_USER:***REMOVED***}> setPrice(event.target.value)}
                  />
                </Field.Root>
                {error && (
                  <Text color${DB_USER:***REMOVED***}{'fg.error'} fontSize${DB_USER:***REMOVED***}{'sm'}>
                    {error}
                  </Text>
                )}
              </Stack>
            </form>
          </DialogBody>
          <DialogFooter>
            <DialogCloseTrigger asChild>
              <Button variant${DB_USER:***REMOVED***}{'ghost'}>Закрыть</Button>
            </DialogCloseTrigger>
            <Button
              colorPalette${DB_USER:***REMOVED***}{'red'}
              onClick${DB_USER:***REMOVED***}{handleDelete}
              loading${DB_USER:***REMOVED***}{isDeleting}
              disabled${DB_USER:***REMOVED***}{isSaving}
            >
              Удалить
            </Button>
            <Button
              colorPalette${DB_USER:***REMOVED***}{'accent'}
              type${DB_USER:***REMOVED***}{'submit'}
              form${DB_USER:***REMOVED***}{formId}
              loading${DB_USER:***REMOVED***}{isSaving}
              disabled${DB_USER:***REMOVED***}{isSubmitDisabled}
            >
              Сохранить
            </Button>
          </DialogFooter>
        </DialogContent>
      </DialogPositioner>
    </DialogRoot>
  );
};

export default ManageZoneDialog;
