'use client';

import { ChakraProvider } from '@chakra-ui/react';
import { ColorModeProvider, type ColorModeProviderProps } from './color-mode';
import { system } from '../../theme';

export function Provider(props: ColorModeProviderProps) {
  return (
    <ChakraProvider value${DB_USER:***REMOVED***}{system}>
      <ColorModeProvider forcedTheme${DB_USER:***REMOVED***}"light" enableSystem${DB_USER:***REMOVED***}{false} {...props} />
    </ChakraProvider>
  );
}
