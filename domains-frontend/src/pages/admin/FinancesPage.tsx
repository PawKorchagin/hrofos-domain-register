import {
  Button,
  Grid,
  GridItem,
  Heading,
  HStack,
  Stack,
  Text,
} from '@chakra-ui/react';
import React from 'react';
import DateText from '~/components/DateText';

const FinancesPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  return (
    <Stack gap${DB_USER:***REMOVED***}{5}>
      <Heading>Финансы</Heading>
      <Stack>
        <Text>Оборот за месяц: 18514 рублей</Text>
        <Text>Оборот за неделю: 1321 рублей</Text>
      </Stack>

      <Stack>
        <Text>Счета</Text>
        <Grid
          templateColumns${DB_USER:***REMOVED***}{'auto auto auto auto auto 1fr'}
          rowGap${DB_USER:***REMOVED***}{2}
          columnGap${DB_USER:***REMOVED***}{5}
          bg${DB_USER:***REMOVED***}{'accent.muted'}
          p${DB_USER:***REMOVED***}{5}
          borderRadius${DB_USER:***REMOVED***}{'md'}
          alignItems${DB_USER:***REMOVED***}{'center'}
        >
          <GridItem>
            <Text fontWeight${DB_USER:***REMOVED***}"bold">пользователь</Text>
          </GridItem>
          <GridItem>
            <Text fontWeight${DB_USER:***REMOVED***}"bold">операция</Text>
          </GridItem>
          <GridItem>
            <Text fontWeight${DB_USER:***REMOVED***}"bold">статус</Text>
          </GridItem>
          <GridItem>
            <Text fontWeight${DB_USER:***REMOVED***}"bold">дата</Text>
          </GridItem>
          <GridItem>
            <Text fontWeight${DB_USER:***REMOVED***}"bold">сумма</Text>
          </GridItem>
          <GridItem />

          <GridItem>ivain@zinch.me</GridItem>
          <GridItem>поступление(карта)</GridItem>
          <GridItem>совершена</GridItem>
          <GridItem>
            <DateText>{new Date()}</DateText>
          </GridItem>
          <GridItem>1500 рублей</GridItem>
          <GridItem>
            <HStack justifyContent${DB_USER:***REMOVED***}{'flex-end'}>
              <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'red'}>
                отменить операцию
              </Button>
            </HStack>
          </GridItem>

          <GridItem>ivain@zinch.me</GridItem>
          <GridItem>поступление(карта)</GridItem>
          <GridItem>ожидание оплаты</GridItem>
          <GridItem>
            <DateText>{new Date()}</DateText>
          </GridItem>
          <GridItem>1500 рублей</GridItem>
          <GridItem>
            <HStack justifyContent${DB_USER:***REMOVED***}{'flex-end'}>
              <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'secondary'}>
                пропустить оплату
              </Button>
            </HStack>
          </GridItem>
        </Grid>
      </Stack>
    </Stack>
  );
};

export default FinancesPage;
