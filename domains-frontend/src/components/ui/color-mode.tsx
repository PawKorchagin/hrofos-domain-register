'use client';

import type { IconButtonProps, SpanProps } from '@chakra-ui/react';
import { ClientOnly, IconButton, Skeleton, Span } from '@chakra-ui/react';
import { ThemeProvider, useTheme } from 'next-themes';
import type { ThemeProviderProps } from 'next-themes';
import * as React from 'react';
import { LuMoon, LuSun } from 'react-icons/lu';

export interface ColorModeProviderProps extends ThemeProviderProps {}

export function ColorModeProvider(props: ColorModeProviderProps) {
  return (
    <ThemeProvider attribute${DB_USER:***REMOVED***}"class" disableTransitionOnChange {...props} />
  );
}

export type ColorMode ${DB_USER:***REMOVED***} 'light' | 'dark';

export interface UseColorModeReturn {
  colorMode: ColorMode;
  setColorMode: (colorMode: ColorMode) ${DB_USER:***REMOVED***}> void;
  toggleColorMode: () ${DB_USER:***REMOVED***}> void;
}

export function useColorMode(): UseColorModeReturn {
  const { resolvedTheme, setTheme, forcedTheme } ${DB_USER:***REMOVED***} useTheme();
  const colorMode ${DB_USER:***REMOVED***} forcedTheme || resolvedTheme;
  const toggleColorMode ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
    setTheme(resolvedTheme ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'dark' ? 'light' : 'dark');
  };
  return {
    colorMode: colorMode as ColorMode,
    setColorMode: setTheme,
    toggleColorMode,
  };
}

export function useColorModeValue<T>(light: T, dark: T) {
  const { colorMode } ${DB_USER:***REMOVED***} useColorMode();
  return colorMode ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'dark' ? dark : light;
}

export function ColorModeIcon() {
  const { colorMode } ${DB_USER:***REMOVED***} useColorMode();
  return colorMode ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 'dark' ? <LuMoon /> : <LuSun />;
}

interface ColorModeButtonProps extends Omit<IconButtonProps, 'aria-label'> {}

export const ColorModeButton ${DB_USER:***REMOVED***} React.forwardRef<
  HTMLButtonElement,
  ColorModeButtonProps
>(function ColorModeButton(props, ref) {
  const { toggleColorMode } ${DB_USER:***REMOVED***} useColorMode();
  return (
    <ClientOnly fallback${DB_USER:***REMOVED***}{<Skeleton boxSize${DB_USER:***REMOVED***}"9" />}>
      <IconButton
        onClick${DB_USER:***REMOVED***}{toggleColorMode}
        variant${DB_USER:***REMOVED***}"ghost"
        aria-label${DB_USER:***REMOVED***}"Toggle color mode"
        size${DB_USER:***REMOVED***}"sm"
        ref${DB_USER:***REMOVED***}{ref}
        {...props}
        css${DB_USER:***REMOVED***}{{
          _icon: {
            width: '5',
            height: '5',
          },
        }}
      >
        <ColorModeIcon />
      </IconButton>
    </ClientOnly>
  );
});

export const LightMode ${DB_USER:***REMOVED***} React.forwardRef<HTMLSpanElement, SpanProps>(
  function LightMode(props, ref) {
    return (
      <Span
        color${DB_USER:***REMOVED***}"fg"
        display${DB_USER:***REMOVED***}"contents"
        className${DB_USER:***REMOVED***}"chakra-theme light"
        colorPalette${DB_USER:***REMOVED***}"gray"
        colorScheme${DB_USER:***REMOVED***}"light"
        ref${DB_USER:***REMOVED***}{ref}
        {...props}
      />
    );
  }
);

export const DarkMode ${DB_USER:***REMOVED***} React.forwardRef<HTMLSpanElement, SpanProps>(
  function DarkMode(props, ref) {
    return (
      <Span
        color${DB_USER:***REMOVED***}"fg"
        display${DB_USER:***REMOVED***}"contents"
        className${DB_USER:***REMOVED***}"chakra-theme dark"
        colorPalette${DB_USER:***REMOVED***}"gray"
        colorScheme${DB_USER:***REMOVED***}"dark"
        ref${DB_USER:***REMOVED***}{ref}
        {...props}
      />
    );
  }
);
