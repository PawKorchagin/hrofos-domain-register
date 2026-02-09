'use client';

import {
  Toaster as ChakraToaster,
  Portal,
  Spinner,
  Stack,
  Toast,
  createToaster,
} from '@chakra-ui/react';

export const toaster ${DB_USER:***REMOVED***} createToaster({
  placement: 'bottom-end',
  pauseOnPageIdle: true,
});

export const Toaster ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  return (
    <Portal>
      <ChakraToaster toaster${DB_USER:***REMOVED***}{toaster} insetInline${DB_USER:***REMOVED***}{{ mdDown: '4' }}>
        {(toast) ${DB_USER:***REMOVED***}> (
          <Toast.Root width${DB_USER:***REMOVED***}{{ md: 'sm' }}>
            {toast.type ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'loading' ? (
              <Spinner size${DB_USER:***REMOVED***}"sm" color${DB_USER:***REMOVED***}"blue.solid" />
            ) : (
              <Toast.Indicator />
            )}
            <Stack gap${DB_USER:***REMOVED***}"1" flex${DB_USER:***REMOVED***}"1" maxWidth${DB_USER:***REMOVED***}"100%">
              {toast.title && <Toast.Title>{toast.title}</Toast.Title>}
              {toast.description && (
                <Toast.Description>{toast.description}</Toast.Description>
              )}
            </Stack>
            {toast.action && (
              <Toast.ActionTrigger>{toast.action.label}</Toast.ActionTrigger>
            )}
            {toast.closable && <Toast.CloseTrigger />}
          </Toast.Root>
        )}
      </ChakraToaster>
    </Portal>
  );
};
