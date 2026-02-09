import {
  Box,
  Button,
  Heading,
  HStack,
  Input,
  Stack,
  Text,
} from '@chakra-ui/react';

const AdminDashboardPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  return (
    <Stack gap${DB_USER:***REMOVED***}{5}>
      <Heading>Админ-панель</Heading>
      <HStack gap${DB_USER:***REMOVED***}{5}>
        <Stack>
          <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Кол-во пользователей</Text>
          <Stack
            width${DB_USER:***REMOVED***}{'12em'}
            bg${DB_USER:***REMOVED***}{'accent.muted'}
            borderRadius${DB_USER:***REMOVED***}{'md'}
            py${DB_USER:***REMOVED***}{5}
            alignItems${DB_USER:***REMOVED***}{'center'}
          >
            <Text>1135</Text>
          </Stack>
        </Stack>

        <Stack>
          <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Кол-во доменов</Text>
          <Stack
            width${DB_USER:***REMOVED***}{'12em'}
            bg${DB_USER:***REMOVED***}{'accent.muted'}
            borderRadius${DB_USER:***REMOVED***}{'md'}
            py${DB_USER:***REMOVED***}{5}
            alignItems${DB_USER:***REMOVED***}{'center'}
          >
            <Text>115</Text>
          </Stack>
        </Stack>

        <Stack>
          <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Оборот за неделю</Text>
          <Stack
            width${DB_USER:***REMOVED***}{'12em'}
            bg${DB_USER:***REMOVED***}{'accent.muted'}
            borderRadius${DB_USER:***REMOVED***}{'md'}
            py${DB_USER:***REMOVED***}{5}
            alignItems${DB_USER:***REMOVED***}{'center'}
          >
            <Text>1555 рублей</Text>
          </Stack>
        </Stack>

        <Stack>
          <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Оборот за месяц</Text>
          <Stack
            width${DB_USER:***REMOVED***}{'12em'}
            bg${DB_USER:***REMOVED***}{'accent.muted'}
            borderRadius${DB_USER:***REMOVED***}{'md'}
            py${DB_USER:***REMOVED***}{5}
            alignItems${DB_USER:***REMOVED***}{'center'}
          >
            <Text>15550 рублей</Text>
          </Stack>
        </Stack>
      </HStack>

      <Stack alignItems${DB_USER:***REMOVED***}{'flex-start'}>
        <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Отчёт</Text>
        <HStack width${DB_USER:***REMOVED***}{'30em'}>
          <Text>С</Text>
          <Input type${DB_USER:***REMOVED***}{'date'} />
          <Text>по</Text>
          <Input type${DB_USER:***REMOVED***}{'date'} />
        </HStack>
        <Button colorPalette${DB_USER:***REMOVED***}{'accent'}>Составить отчёт</Button>
      </Stack>
    </Stack>
  );
};

export default AdminDashboardPage;
