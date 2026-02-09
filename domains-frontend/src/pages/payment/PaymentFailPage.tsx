import { Button, Heading, HStack, Stack, Text } from '@chakra-ui/react';
import { useNavigate } from 'react-router';

const PaymentFailPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const navigate ${DB_USER:***REMOVED***} useNavigate();

  return (
    <Stack minHeight${DB_USER:***REMOVED***}{'100dvh'} alignItems${DB_USER:***REMOVED***}{'center'} justifyContent${DB_USER:***REMOVED***}{'center'}>
      <Stack
        maxW${DB_USER:***REMOVED***}{'560px'}
        width${DB_USER:***REMOVED***}{'100%'}
        bg${DB_USER:***REMOVED***}{'accent.muted'}
        borderRadius${DB_USER:***REMOVED***}{'lg'}
        p${DB_USER:***REMOVED***}{8}
        gap${DB_USER:***REMOVED***}{4}
      >
        <Heading size${DB_USER:***REMOVED***}{'lg'}>Платеж не прошел</Heading>
        <Text color${DB_USER:***REMOVED***}{'fg.muted'}>
          Оплата не была завершена. Проверьте данные карты или попробуйте снова.
        </Text>
        <HStack gap${DB_USER:***REMOVED***}{3} flexWrap${DB_USER:***REMOVED***}{'wrap'}>
          <Button colorPalette${DB_USER:***REMOVED***}{'secondary'} onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> navigate('/app/cart')}>
            Вернуться в корзину
          </Button>
          <Button variant${DB_USER:***REMOVED***}{'subtle'} onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> navigate('/app/domains')}>
            К моим доменам
          </Button>
        </HStack>
      </Stack>
    </Stack>
  );
};

export default PaymentFailPage;
