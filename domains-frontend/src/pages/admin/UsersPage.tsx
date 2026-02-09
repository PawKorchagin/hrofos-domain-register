import {
  Button,
  Grid,
  GridItem,
  Heading,
  HStack,
  Stack,
  Text,
} from '@chakra-ui/react';

const UsersPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  return (
    <Stack>
      <Heading>Пользователи</Heading>
      <Stack bg${DB_USER:***REMOVED***}{'accent.muted'} p${DB_USER:***REMOVED***}{5} borderRadius${DB_USER:***REMOVED***}{'md'}>
        <Grid
          templateColumns${DB_USER:***REMOVED***}{'1fr auto 1fr'}
          alignItems${DB_USER:***REMOVED***}{'center'}
          rowGap${DB_USER:***REMOVED***}{2}
          columnGap${DB_USER:***REMOVED***}{5}
        >
          <GridItem borderBottomColor${DB_USER:***REMOVED***}{'border'}>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Email</Text>
          </GridItem>
          <GridItem borderBottomColor${DB_USER:***REMOVED***}{'border'}>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>кол-во доменов</Text>
          </GridItem>
          <GridItem borderBottomColor${DB_USER:***REMOVED***}{'border'} />

          <GridItem>some@email.com</GridItem>
          <GridItem>5</GridItem>
          <GridItem>
            <HStack justifyContent${DB_USER:***REMOVED***}{'flex-end'}>
              <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'secondary'}>
                редактировать
              </Button>
              <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'red'}>
                заблокировать
              </Button>
            </HStack>
          </GridItem>

          <GridItem>another@email.com</GridItem>
          <GridItem>7</GridItem>
          <GridItem>
            <HStack justifyContent${DB_USER:***REMOVED***}{'flex-end'}>
              <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'secondary'}>
                редактировать
              </Button>
              <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'red'}>
                заблокировать
              </Button>
            </HStack>
          </GridItem>
        </Grid>
      </Stack>
    </Stack>
  );
};

export default UsersPage;
