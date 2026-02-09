import {
  Box,
  Button,
  Grid,
  GridItem,
  Heading,
  HStack,
  Spinner,
  Stack,
  Text,
  VStack,
} from '@chakra-ui/react';
import { observer } from 'mobx-react-lite';
import { useEffect, useState } from 'react';
import { get2FAStatus } from '~/api/services/auth';
import EnableTwoFactorDialog from '~/components/app/profile/EnableTwoFactorDialog';
import { useStores } from '~/store';

const ProfilePage ${DB_USER:***REMOVED***} observer(() ${DB_USER:***REMOVED***}> {
  const { userStore } ${DB_USER:***REMOVED***} useStores();
  const { user } ${DB_USER:***REMOVED***} userStore;

  const [tfaStatus, setTfaStatus] ${DB_USER:***REMOVED***} useState<boolean | undefined>(undefined);

  useEffect(() ${DB_USER:***REMOVED***}> {
    get2FAStatus().then((status) ${DB_USER:***REMOVED***}> setTfaStatus(Boolean(status.data)));
  }, []);

  return (
    <Stack>
      <Heading>Профиль</Heading>
      <Grid templateColumns${DB_USER:***REMOVED***}{'1fr 1fr'} maxW${DB_USER:***REMOVED***}{'70%'} gap${DB_USER:***REMOVED***}{5}>
        <GridItem>
          <Text mb${DB_USER:***REMOVED***}{4}>Данные</Text>
          <Stack bg${DB_USER:***REMOVED***}{'accent.muted'} p${DB_USER:***REMOVED***}{5} borderRadius${DB_USER:***REMOVED***}{'md'} gap${DB_USER:***REMOVED***}{5}>
            <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
              <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Email</Text>
              <Text>{user?.email ?? '??'}</Text>
            </HStack>
            {/* <Box>
              <ChangePassword />
            </Box> */}
          </Stack>
        </GridItem>
        <GridItem>
          <Text mb${DB_USER:***REMOVED***}{4}>2FA</Text>
          <Stack bg${DB_USER:***REMOVED***}{'accent.muted'} p${DB_USER:***REMOVED***}{5} borderRadius${DB_USER:***REMOVED***}{'md'} gap${DB_USER:***REMOVED***}{5}>
            {tfaStatus ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} undefined && (
              <VStack height${DB_USER:***REMOVED***}{'100%'} py${DB_USER:***REMOVED***}{5}>
                <Spinner />
              </VStack>
            )}
            {tfaStatus ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} true && (
              <>
                <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
                  <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Подключена 2FA по TOTP</Text>
                </HStack>
                <Box>
                  <Button colorPalette${DB_USER:***REMOVED***}{'secondary'} size${DB_USER:***REMOVED***}{'sm'}>
                    отключить
                  </Button>
                </Box>
              </>
            )}
            {tfaStatus ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} false && (
              <>
                <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>2FA не подключена</Text>
                <EnableTwoFactorDialog onEnabled${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> setTfaStatus(true)} />
              </>
            )}
          </Stack>
        </GridItem>
        <GridItem>
          <HStack>
            {/* <Button colorPalette${DB_USER:***REMOVED***}{'secondary'}>Выйти</Button> */}
            {/* <Button colorPalette${DB_USER:***REMOVED***}{'red'}>Удалить аккаунт</Button> */}
          </HStack>
        </GridItem>
      </Grid>
    </Stack>
  );
});

export default ProfilePage;
