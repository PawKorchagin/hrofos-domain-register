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

const UsersDomainsPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  return (
    <Stack>
      <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
        <Heading>Домены</Heading>
        <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'secondary'}>
          добавить
        </Button>
      </HStack>

      <Grid
        templateColumns${DB_USER:***REMOVED***}{'1fr 1fr auto 3fr'}
        rowGap${DB_USER:***REMOVED***}{2}
        columnGap${DB_USER:***REMOVED***}{5}
        bg${DB_USER:***REMOVED***}{'accent.muted'}
        p${DB_USER:***REMOVED***}{5}
        borderRadius${DB_USER:***REMOVED***}{'md'}
        alignItems${DB_USER:***REMOVED***}{'center'}
      >
        <GridItem>
          <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>домен</Text>
        </GridItem>
        <GridItem>
          <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>пользователь</Text>
        </GridItem>
        <GridItem>
          <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>истекает</Text>
        </GridItem>
        <GridItem />

        <GridItem>hello.example.com</GridItem>
        <GridItem>hello@zinch.com</GridItem>
        <GridItem>
          <DateText>{new Date(2026, 1, 3)}</DateText>
        </GridItem>
        <GridItem>
          <HStack justifyContent${DB_USER:***REMOVED***}{'flex-end'}>
            <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'secondary'}>
              редактировать
            </Button>
            <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'red'}>
              удалить
            </Button>
          </HStack>
        </GridItem>

        <GridItem>omg.example.com</GridItem>
        <GridItem>omg@zinch.com</GridItem>
        <GridItem>
          <DateText>{new Date(2026, 3, 3)}</DateText>
        </GridItem>
        <GridItem>
          <HStack justifyContent${DB_USER:***REMOVED***}{'flex-end'}>
            <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'secondary'}>
              редактировать
            </Button>
            <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'red'}>
              удалить
            </Button>
          </HStack>
        </GridItem>
      </Grid>
    </Stack>
  );
};

export default UsersDomainsPage;
