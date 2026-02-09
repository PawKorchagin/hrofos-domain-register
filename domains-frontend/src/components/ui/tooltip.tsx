import { Tooltip as ChakraTooltip, Portal } from '@chakra-ui/react';
import * as React from 'react';

export interface TooltipProps extends ChakraTooltip.RootProps {
  showArrow?: boolean;
  portalled?: boolean;
  portalRef?: React.RefObject<HTMLElement | null>;
  content: React.ReactNode;
  contentProps?: ChakraTooltip.ContentProps;
  disabled?: boolean;
}

export const Tooltip ${DB_USER:***REMOVED***} React.forwardRef<HTMLDivElement, TooltipProps>(
  function Tooltip(props, ref) {
    const {
      showArrow,
      children,
      disabled,
      portalled ${DB_USER:***REMOVED***} true,
      content,
      contentProps,
      portalRef,
      ...rest
    } ${DB_USER:***REMOVED***} props;

    if (disabled) return children;

    return (
      <ChakraTooltip.Root {...rest}>
        <ChakraTooltip.Trigger asChild>{children}</ChakraTooltip.Trigger>
        <Portal disabled${DB_USER:***REMOVED***}{!portalled} container${DB_USER:***REMOVED***}{portalRef}>
          <ChakraTooltip.Positioner>
            <ChakraTooltip.Content ref${DB_USER:***REMOVED***}{ref} {...contentProps}>
              {showArrow && (
                <ChakraTooltip.Arrow>
                  <ChakraTooltip.ArrowTip />
                </ChakraTooltip.Arrow>
              )}
              {content}
            </ChakraTooltip.Content>
          </ChakraTooltip.Positioner>
        </Portal>
      </ChakraTooltip.Root>
    );
  }
);
