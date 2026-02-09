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
import { useState, type FormEvent } from 'react';
import { createZone } from '~/api/services/domain-order';

type Props ${DB_USER:***REMOVED***} {
  onCreated?: () ${DB_USER:***REMOVED***}> void | Promise<void>;
};

const CreateZoneDialog ${DB_USER:***REMOVED***} ({ onCreated }: Props) ${DB_USER:***REMOVED***}> {
  const [isCreating, setIsCreating] ${DB_USER:***REMOVED***} useState(false);
  const [createError, setCreateError] ${DB_USER:***REMOVED***} useState('');
  const [zoneName, setZoneName] ${DB_USER:***REMOVED***} useState('');
  const [zonePrice, setZonePrice] ${DB_USER:***REMOVED***} useState('');

  const handleCreateZone ${DB_USER:***REMOVED***} async (event: FormEvent<HTMLFormElement>) ${DB_USER:***REMOVED***}> {
    event.preventDefault();
    const name ${DB_USER:***REMOVED***} zoneName.trim();
    const price ${DB_USER:***REMOVED***} Number(zonePrice);

    if (!name || Number.isNaN(price)) {
      setCreateError('Введие корректное имя и(или) цену.');
      return;
    }

    setIsCreating(true);
    setCreateError('');

    try {
      await createZone({ name, price });
      setZoneName('');
      setZonePrice('');
      if (onCreated) {
        await onCreated();
      }
    } catch {
      setCreateError('Не удалось создать зону. Попробуйте снова');
    } finally {
      setIsCreating(false);
    }
  };

  const isSubmitDisabled ${DB_USER:***REMOVED***}
    isCreating ||
    !zoneName.trim() ||
    !zonePrice.trim() ||
    Number.isNaN(Number(zonePrice));

  return (
    <DialogRoot>
      <DialogTrigger asChild>
        <Button colorPalette${DB_USER:***REMOVED***}{'secondary'} size${DB_USER:***REMOVED***}{'sm'}>
          Создать зону
        </Button>
      </DialogTrigger>
      <DialogBackdrop />
      <DialogPositioner>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Создать зону</DialogTitle>
          </DialogHeader>
          <DialogBody>
            <form id${DB_USER:***REMOVED***}"create-zone-form" onSubmit${DB_USER:***REMOVED***}{handleCreateZone}>
              <Stack gap${DB_USER:***REMOVED***}{4}>
                <Field.Root required>
                  <Field.Label>Название зоны</Field.Label>
                  <Input
                    placeholder${DB_USER:***REMOVED***}"example.goip.com"
                    value${DB_USER:***REMOVED***}{zoneName}
                    onChange${DB_USER:***REMOVED***}{(event) ${DB_USER:***REMOVED***}> setZoneName(event.target.value)}
                  />
                </Field.Root>
                <Field.Root required>
                  <Field.Label>Цена</Field.Label>
                  <Input
                    type${DB_USER:***REMOVED***}"number"
                    min${DB_USER:***REMOVED***}{30}
                    step${DB_USER:***REMOVED***}"0.01"
                    value${DB_USER:***REMOVED***}{zonePrice}
                    onChange${DB_USER:***REMOVED***}{(event) ${DB_USER:***REMOVED***}> setZonePrice(event.target.value)}
                  />
                </Field.Root>
                {createError && (
                  <Text color${DB_USER:***REMOVED***}{'fg.error'} fontSize${DB_USER:***REMOVED***}{'sm'}>
                    {createError}
                  </Text>
                )}
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
              loading${DB_USER:***REMOVED***}{isCreating}
              disabled${DB_USER:***REMOVED***}{isSubmitDisabled}
            >
              Создать
            </Button>
          </DialogFooter>
        </DialogContent>
      </DialogPositioner>
    </DialogRoot>
  );
};

export default CreateZoneDialog;
