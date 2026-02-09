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
import type { TwoFactorSetupResponse } from '~/api/models/auth';
import { enable2FA, generateSetup } from '~/api/services/auth';

type Props ${DB_USER:***REMOVED***} {
  onEnabled?: () ${DB_USER:***REMOVED***}> void | Promise<void>;
};

const EnableTwoFactorDialog ${DB_USER:***REMOVED***} ({ onEnabled }: Props) ${DB_USER:***REMOVED***}> {
  const [setup, setSetup] ${DB_USER:***REMOVED***} useState<TwoFactorSetupResponse | null>(null);
  const [code, setCode] ${DB_USER:***REMOVED***} useState('');
  const [isLoadingSetup, setIsLoadingSetup] ${DB_USER:***REMOVED***} useState(false);
  const [isEnabling, setIsEnabling] ${DB_USER:***REMOVED***} useState(false);
  const [error, setError] ${DB_USER:***REMOVED***} useState('');
  const [success, setSuccess] ${DB_USER:***REMOVED***} useState('');

  const handleOpen ${DB_USER:***REMOVED***} async () ${DB_USER:***REMOVED***}> {
    setError('');
    setSuccess('');
    if (setup || isLoadingSetup) return;

    setIsLoadingSetup(true);
    try {
      const response ${DB_USER:***REMOVED***} await generateSetup();
      setSetup(response?.data ?? null);
    } catch {
      setError('Не удалось получить настройки 2FA. Попробуйте еще раз.');
    } finally {
      setIsLoadingSetup(false);
    }
  };

  const handleEnable ${DB_USER:***REMOVED***} async (event: FormEvent<HTMLFormElement>) ${DB_USER:***REMOVED***}> {
    event.preventDefault();
    setError('');
    setSuccess('');

    const secret ${DB_USER:***REMOVED***} setup?.secret ?? setup?.manualEntryKey;
    if (!secret) {
      setError('Настройки 2FA не найдены. Попробуйте еще раз.');
      return;
    }

    if (!code.trim()) {
      setError('Введите корректный код.');
      return;
    }

    setIsEnabling(true);
    try {
      await enable2FA({ secret, code: code.trim() });
      setSuccess('Двухфакторная аутентификация включена.');
      setCode('');
      if (onEnabled) {
        await onEnabled();
      }
    } catch {
      setError('Не удалось включить 2FA. Попробуйте еще раз.');
    } finally {
      setIsEnabling(false);
    }
  };

  const isSubmitDisabled ${DB_USER:***REMOVED***} isEnabling || !code.trim() || isLoadingSetup;

  return (
    <DialogRoot>
      <DialogTrigger asChild>
        <Button colorPalette${DB_USER:***REMOVED***}{'secondary'} size${DB_USER:***REMOVED***}{'sm'} onClick${DB_USER:***REMOVED***}{handleOpen}>
          Подключить
        </Button>
      </DialogTrigger>
      <DialogBackdrop />
      <DialogPositioner>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Включить 2FA</DialogTitle>
          </DialogHeader>
          <DialogBody>
            <form id${DB_USER:***REMOVED***}"enable-2fa-form" onSubmit${DB_USER:***REMOVED***}{handleEnable}>
              <Stack gap${DB_USER:***REMOVED***}{4}>
                {isLoadingSetup ? (
                  <Text>Загрузка настроек...</Text>
                ) : (
                  <>
                    {setup?.qrCodeUrl && (
                      <img src${DB_USER:***REMOVED***}{setup.qrCodeUrl} alt${DB_USER:***REMOVED***}"2FA QR code" />
                    )}
                    <Field.Root>
                      <Field.Label>Ключ настройки</Field.Label>
                      <Input
                        readOnly
                        value${DB_USER:***REMOVED***}{setup?.manualEntryKey ?? setup?.secret ?? ''}
                      />
                    </Field.Root>
                    <Field.Root required>
                      <Field.Label>Код</Field.Label>
                      <Input
                        inputMode${DB_USER:***REMOVED***}"numeric"
                        value${DB_USER:***REMOVED***}{code}
                        onChange${DB_USER:***REMOVED***}{(event) ${DB_USER:***REMOVED***}> setCode(event.target.value)}
                      />
                    </Field.Root>
                  </>
                )}
                {error && (
                  <Text color${DB_USER:***REMOVED***}{'fg.error'} fontSize${DB_USER:***REMOVED***}{'sm'}>
                    {error}
                  </Text>
                )}
                {success && (
                  <Text color${DB_USER:***REMOVED***}{'fg.success'} fontSize${DB_USER:***REMOVED***}{'sm'}>
                    {success}
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
              colorPalette${DB_USER:***REMOVED***}{'accent'}
              type${DB_USER:***REMOVED***}{'submit'}
              form${DB_USER:***REMOVED***}"enable-2fa-form"
              loading${DB_USER:***REMOVED***}{isEnabling}
              disabled${DB_USER:***REMOVED***}{isSubmitDisabled}
            >
              Включить
            </Button>
          </DialogFooter>
        </DialogContent>
      </DialogPositioner>
    </DialogRoot>
  );
};

export default EnableTwoFactorDialog;
