import { Grid, GridItem, Heading, Stack, Text } from '@chakra-ui/react';
import DateText from '~/components/DateText';

const SystemEventsPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  return (
    <Stack>
      <Heading>События</Heading>
      <Grid
        templateColumns${DB_USER:***REMOVED***}{'auto 1fr auto'}
        rowGap${DB_USER:***REMOVED***}{2}
        columnGap${DB_USER:***REMOVED***}{5}
        bg${DB_USER:***REMOVED***}{'accent.muted'}
        p${DB_USER:***REMOVED***}{5}
        borderRadius${DB_USER:***REMOVED***}{'md'}
        alignItems${DB_USER:***REMOVED***}{'center'}
      >
        <GridItem>
          <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>пользователь</Text>
        </GridItem>
        <GridItem>
          <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>действие</Text>
        </GridItem>
        <GridItem>
          <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>дата</Text>
        </GridItem>

        <GridItem>ivan@zinch.me</GridItem>
        <GridItem>покупка домена some.godns.pw</GridItem>
        <GridItem>
          <DateText>{new Date()}</DateText>
        </GridItem>
      </Grid>
    </Stack>
  );
};

export default SystemEventsPage;
