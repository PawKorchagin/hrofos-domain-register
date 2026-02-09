import {
  Accordion,
  Box,
  Button,
  createListCollection,
  Field,
  Grid,
  GridItem,
  Heading,
  HStack,
  Input,
  InputGroup,
  Portal,
  Select,
  Stack,
  Text,
} from '@chakra-ui/react';

const DNSPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const frameworks ${DB_USER:***REMOVED***} createListCollection({
    items: [
      { label: 'hello.zom.com', value: 'hello.zom.com' },
      { label: 'omg.com.com', value: 'omg.com.com' },
      { label: 'why.me.co', value: 'why.me.co' },
      { label: 'not.my.domain.com', value: 'not.my.domain.com' },
    ],
  });

  const DNSTypes ${DB_USER:***REMOVED***} createListCollection({
    items: [
      { label: 'A', value: 'a' },
      { label: 'AAAA', value: 'aaaa' },
      { label: 'SOA', value: 'soa' },
      { label: 'CNAME', value: 'cname' },
      { label: 'TXT', value: 'txt' },
      { label: 'MX', value: 'mx' },
      { label: 'SRV', value: 'srv' },
      { label: 'CAA', value: 'caa' },
    ],
  });

  return (
    <Stack>
      <Heading>DNS</Heading>
      <Select.Root collection${DB_USER:***REMOVED***}{frameworks}>
        <Select.HiddenSelect />
        <Select.Label>Выберите домен</Select.Label>
        <Select.Control>
          <Select.Trigger>
            <Select.ValueText placeholder${DB_USER:***REMOVED***}"Выберите домен" />
          </Select.Trigger>
          <Select.IndicatorGroup>
            <Select.Indicator />
          </Select.IndicatorGroup>
        </Select.Control>
        <Portal>
          <Select.Positioner>
            <Select.Content>
              {frameworks.items.map((framework) ${DB_USER:***REMOVED***}> (
                <Select.Item item${DB_USER:***REMOVED***}{framework} key${DB_USER:***REMOVED***}{framework.value}>
                  {framework.label}
                  <Select.ItemIndicator />
                </Select.Item>
              ))}
            </Select.Content>
          </Select.Positioner>
        </Portal>
      </Select.Root>
      <HStack justifyContent${DB_USER:***REMOVED***}{'flex-end'}>
        <Button size${DB_USER:***REMOVED***}{'sm'} colorPalette${DB_USER:***REMOVED***}{'secondary'}>
          изменить DNS-сервера
        </Button>
      </HStack>

      <Accordion.Root multiple>
        <Accordion.Item value${DB_USER:***REMOVED***}{'1'}>
          <Accordion.ItemTrigger>
            <Text>Добавить запись</Text>
            <Accordion.ItemIndicator />
          </Accordion.ItemTrigger>
          <Accordion.ItemContent>
            <Accordion.ItemBody>
              <Stack bg${DB_USER:***REMOVED***}{'accent.muted'} p${DB_USER:***REMOVED***}{5} borderRadius${DB_USER:***REMOVED***}{'md'}>
                <HStack gap${DB_USER:***REMOVED***}{5}>
                  <Field.Root>
                    <Field.Label>Название</Field.Label>
                    <InputGroup endAddon${DB_USER:***REMOVED***}".goip.pw">
                      <Input placeholder${DB_USER:***REMOVED***}"введите название записи" bg${DB_USER:***REMOVED***}{'bg'} />
                    </InputGroup>
                  </Field.Root>
                  <Field.Root>
                    <Field.Label>Тип</Field.Label>
                    <Select.Root collection${DB_USER:***REMOVED***}{DNSTypes}>
                      <Select.HiddenSelect />
                      <Select.Control>
                        <Select.Trigger bg${DB_USER:***REMOVED***}{'bg'}>
                          <Select.ValueText placeholder${DB_USER:***REMOVED***}"Выберите домен" />
                        </Select.Trigger>
                        <Select.IndicatorGroup>
                          <Select.Indicator />
                        </Select.IndicatorGroup>
                      </Select.Control>
                      <Portal>
                        <Select.Positioner>
                          <Select.Content>
                            {DNSTypes.items.map((type) ${DB_USER:***REMOVED***}> (
                              <Select.Item item${DB_USER:***REMOVED***}{type} key${DB_USER:***REMOVED***}{type.value}>
                                {type.label}
                                <Select.ItemIndicator />
                              </Select.Item>
                            ))}
                          </Select.Content>
                        </Select.Positioner>
                      </Portal>
                    </Select.Root>
                  </Field.Root>
                </HStack>
                <Field.Root>
                  <Field.Label>Значение</Field.Label>
                  <Input placeholder${DB_USER:***REMOVED***}"введите значение записи" bg${DB_USER:***REMOVED***}{'bg'} />
                </Field.Root>
                <Box>
                  <Button colorPalette${DB_USER:***REMOVED***}{'secondary'} size${DB_USER:***REMOVED***}{'sm'}>
                    добавить
                  </Button>
                </Box>
              </Stack>
            </Accordion.ItemBody>
          </Accordion.ItemContent>
        </Accordion.Item>

        <Accordion.Item value${DB_USER:***REMOVED***}{'2'}>
          <Accordion.ItemTrigger>
            <Text>Записи</Text>
            <Accordion.ItemIndicator />
          </Accordion.ItemTrigger>
          <Accordion.ItemContent>
            <Accordion.ItemBody>
              <Stack bg${DB_USER:***REMOVED***}{'accent.muted'} p${DB_USER:***REMOVED***}{5} borderRadius${DB_USER:***REMOVED***}{'md'}>
                <Accordion.Root multiple>
                  <Accordion.Item value${DB_USER:***REMOVED***}{'domain1'}>
                    <Accordion.ItemTrigger>
                      <Text>@</Text>
                      <Accordion.ItemIndicator />
                    </Accordion.ItemTrigger>
                    <Accordion.ItemContent>
                      <Accordion.ItemBody>
                        <Stack bg${DB_USER:***REMOVED***}{'bg'} p${DB_USER:***REMOVED***}{3} borderRadius${DB_USER:***REMOVED***}{'md'}>
                          <Grid gap${DB_USER:***REMOVED***}{2} templateColumns${DB_USER:***REMOVED***}{'auto 1fr auto'}>
                            <GridItem>A</GridItem>
                            <GridItem>5.153.135.4</GridItem>
                            <GridItem>
                              <Button size${DB_USER:***REMOVED***}{'xs'} colorPalette${DB_USER:***REMOVED***}{'red'}>
                                удалить
                              </Button>
                            </GridItem>

                            <GridItem>TXT</GridItem>
                            <GridItem>gasvfcda</GridItem>
                            <GridItem>
                              <Button size${DB_USER:***REMOVED***}{'xs'} colorPalette${DB_USER:***REMOVED***}{'red'}>
                                удалить
                              </Button>
                            </GridItem>
                          </Grid>
                        </Stack>
                      </Accordion.ItemBody>
                    </Accordion.ItemContent>
                  </Accordion.Item>

                  <Accordion.Item value${DB_USER:***REMOVED***}{'domain2'}>
                    <Accordion.ItemTrigger>
                      <Text>www</Text>
                      <Accordion.ItemIndicator />
                    </Accordion.ItemTrigger>
                    <Accordion.ItemContent>
                      <Accordion.ItemBody>
                        <Stack bg${DB_USER:***REMOVED***}{'bg'} p${DB_USER:***REMOVED***}{3} borderRadius${DB_USER:***REMOVED***}{'md'}>
                          <Grid gap${DB_USER:***REMOVED***}{2} templateColumns${DB_USER:***REMOVED***}{'auto 1fr auto'}>
                            <GridItem>A</GridItem>
                            <GridItem>5.153.135.4</GridItem>
                            <GridItem>
                              <Button size${DB_USER:***REMOVED***}{'xs'} colorPalette${DB_USER:***REMOVED***}{'red'}>
                                удалить
                              </Button>
                            </GridItem>

                            <GridItem>TXT</GridItem>
                            <GridItem>gasvfcda</GridItem>
                            <GridItem>
                              <Button size${DB_USER:***REMOVED***}{'xs'} colorPalette${DB_USER:***REMOVED***}{'red'}>
                                удалить
                              </Button>
                            </GridItem>
                          </Grid>
                        </Stack>
                      </Accordion.ItemBody>
                    </Accordion.ItemContent>
                  </Accordion.Item>
                </Accordion.Root>
              </Stack>
            </Accordion.ItemBody>
          </Accordion.ItemContent>
        </Accordion.Item>
      </Accordion.Root>
    </Stack>
  );
};

export default DNSPage;
