import { Button, Field, Input, Stack, Text } from '@chakra-ui/react';
import {
  DialogActionTrigger,
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

const ChangePassword ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [currentPassword, setCurrentPassword] ${DB_USER:***REMOVED***} useState('');
  const [newPassword, setNewPassword] ${DB_USER:***REMOVED***} useState('');
  const [confirmPassword, setConfirmPassword] ${DB_USER:***REMOVED***} useState('');

  const isMismatch ${DB_USER:***REMOVED***}
    newPassword.length > 0 &&
    confirmPassword.length > 0 &&
    newPassword !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} confirmPassword;

  const isSubmitDisabled ${DB_USER:***REMOVED***}
    !currentPassword || !newPassword || !confirmPassword || isMismatch;

  const handleSubmit ${DB_USER:***REMOVED***} (e: FormEvent<HTMLFormElement>) ${DB_USER:***REMOVED***}> {
    e.preventDefault();
  };

  return (
    <DialogRoot>
      <DialogTrigger asChild>
        <Button colorPalette${DB_USER:***REMOVED***}{'secondary'} size${DB_USER:***REMOVED***}{'sm'}>
          Сменить пароль
        </Button>
      </DialogTrigger>
      <DialogBackdrop />
      <DialogPositioner>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Смена пароля</DialogTitle>
          </DialogHeader>
          <DialogBody>
            <form onSubmit${DB_USER:***REMOVED***}{handleSubmit}>
              <Stack gap${DB_USER:***REMOVED***}{4}>
                <Field.Root required>
                  <Field.Label>Текущий пароль</Field.Label>
                  <Input
                    type${DB_USER:***REMOVED***}"password"
                    value${DB_USER:***REMOVED***}{currentPassword}
                    onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setCurrentPassword(e.target.value)}
                  />
                </Field.Root>
                <Field.Root required>
                  <Field.Label>Новый пароль</Field.Label>
                  <Input
                    type${DB_USER:***REMOVED***}"password"
                    value${DB_USER:***REMOVED***}{newPassword}
                    onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setNewPassword(e.target.value)}
                  />
                </Field.Root>
                <Field.Root required invalid${DB_USER:***REMOVED***}{isMismatch}>
                  <Field.Label>Повторите новый пароль</Field.Label>
                  <Input
                    type${DB_USER:***REMOVED***}"password"
                    value${DB_USER:***REMOVED***}{confirmPassword}
                    onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setConfirmPassword(e.target.value)}
                  />
                </Field.Root>
                {isMismatch && (
                  <Text color${DB_USER:***REMOVED***}{'fg.error'} fontSize${DB_USER:***REMOVED***}{'sm'}>
                    Пароли не совпадают
                  </Text>
                )}
              </Stack>
            </form>
          </DialogBody>
          <DialogFooter>
            <DialogCloseTrigger asChild>
              <Button variant${DB_USER:***REMOVED***}{'ghost'}>Отмена</Button>
            </DialogCloseTrigger>
            <DialogActionTrigger asChild>
              <Button
                colorPalette${DB_USER:***REMOVED***}{'accent'}
                type${DB_USER:***REMOVED***}{'submit'}
                form${DB_USER:***REMOVED***}"change-password-form"
                disabled${DB_USER:***REMOVED***}{isSubmitDisabled}
              >
                Сохранить
              </Button>
            </DialogActionTrigger>
          </DialogFooter>
        </DialogContent>
      </DialogPositioner>
    </DialogRoot>
  );
};

export default ChangePassword;
