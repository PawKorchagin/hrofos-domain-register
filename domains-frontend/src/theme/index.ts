import { createSystem, defaultConfig, defineConfig } from '@chakra-ui/react';

const config ${DB_USER:***REMOVED***} defineConfig({
  theme: {
    tokens: {
      colors: {
        accent: {
          50: { value: '#fdf9e6' },
          100: { value: '#fbf1c8' },
          200: { value: '#f7e79c' },
          300: { value: '#f3dc6f' },
          400: { value: '#f0d45f' },
          500: { value: '#eed059' },
          600: { value: '#d8b84f' },
          700: { value: '#b3923f' },
          800: { value: '#8b6f30' },
          900: { value: '#6a5425' },
        },
        secondary: {
          50: { value: '#eef1ff' },
          100: { value: '#d6defe' },
          200: { value: '#b2c1fc' },
          300: { value: '#8da4f8' },
          400: { value: '#6f8cf3' },
          500: { value: '#5976ee' },
          600: { value: '#4e63d7' },
          700: { value: '#3f4eb6' },
          800: { value: '#323c8c' },
          900: { value: '#262d69' },
        },
      },
      radii: {
        sm: { value: '20px' },
        md: { value: '20px' },
        lg: { value: '20px' },
        xl: { value: '20px' },
      },
      fonts: {
        heading: { value: "'Jest', 'Jost Variable', sans-serif" },
        body: { value: "'Golos Text', sans-serif" },
      },
    },
    semanticTokens: {
      colors: {
        accent: {
          solid: { value: '{colors.accent.500}' },
          contrast: { value: '{colors.accent.900}' },
          fg: { value: '{colors.accent.700}' },
          muted: { value: '{colors.accent.100}' },
          subtle: { value: '{colors.accent.200}' },
          emphasized: { value: '{colors.accent.300}' },
          focusRing: { value: '{colors.accent.500}' },
        },
        secondary: {
          solid: { value: '{colors.secondary.500}' },
          contrast: { value: 'white' },
          fg: { value: '{colors.secondary.700}' },
          muted: { value: '{colors.secondary.100}' },
          subtle: { value: '{colors.secondary.200}' },
          emphasized: { value: '{colors.secondary.300}' },
          focusRing: { value: '{colors.secondary.500}' },
        },
      },
    },
  },
});

export const system ${DB_USER:***REMOVED***} createSystem(defaultConfig, config);
