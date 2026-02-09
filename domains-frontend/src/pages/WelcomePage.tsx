import { Box, Button, HStack, Input, Stack, Text } from '@chakra-ui/react';
import { useCallback, useEffect, useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router';
import { validateDomain } from '../utils/validateDomain';
import { $ok } from '../common/atoms';

const WelcomePage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [input, setInput] ${DB_USER:***REMOVED***} useState<string>('');
  const [error, setError] ${DB_USER:***REMOVED***} useState<string>('');
  const domains ${DB_USER:***REMOVED***} ['.goip.pw', '.godns.pw', '.gofrom.pw'];
  const [domainIndex, setDomainIndex] ${DB_USER:***REMOVED***} useState(0);
  const [isFading, setIsFading] ${DB_USER:***REMOVED***} useState(false);
  const navigate ${DB_USER:***REMOVED***} useNavigate();

  useEffect(() ${DB_USER:***REMOVED***}> {
    const intervalId ${DB_USER:***REMOVED***} setInterval(() ${DB_USER:***REMOVED***}> {
      setIsFading(true);
      const timeoutId ${DB_USER:***REMOVED***} setTimeout(() ${DB_USER:***REMOVED***}> {
        setDomainIndex((prev) ${DB_USER:***REMOVED***}> (prev + 1) % domains.length);
        setIsFading(false);
      }, 300);

      return () ${DB_USER:***REMOVED***}> clearTimeout(timeoutId);
    }, 4000);

    return () ${DB_USER:***REMOVED***}> clearInterval(intervalId);
  }, [domains.length]);

  const handleSubmit ${DB_USER:***REMOVED***} useCallback(
    (e: FormEvent<HTMLFormElement>) ${DB_USER:***REMOVED***}> {
      e.preventDefault();
      const domain ${DB_USER:***REMOVED***} input.trim().toLocaleLowerCase();
      const [result, reason] ${DB_USER:***REMOVED***} validateDomain(domain);
      if (result ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} $ok) {
        navigate(`/check-domain?q${DB_USER:***REMOVED***}${domain}`);
      } else {
        setError(reason);
      }
    },
    [input]
  );

  return (
    <Stack flex${DB_USER:***REMOVED***}{1} justifyContent${DB_USER:***REMOVED***}{'center'} alignItems${DB_USER:***REMOVED***}{'center'}>
      <Stack
        minW${DB_USER:***REMOVED***}{'50em'}
        bg${DB_USER:***REMOVED***}{'accent.muted'}
        p${DB_USER:***REMOVED***}{10}
        borderRadius${DB_USER:***REMOVED***}{'sm'}
        gap${DB_USER:***REMOVED***}{5}
      >
        <Text fontSize${DB_USER:***REMOVED***}{24} fontWeight${DB_USER:***REMOVED***}{'bold'}>
          Проверьте доступность домена
        </Text>
        <form onSubmit${DB_USER:***REMOVED***}{handleSubmit}>
          <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'} gap${DB_USER:***REMOVED***}{5}>
            <HStack gap${DB_USER:***REMOVED***}{0} width${DB_USER:***REMOVED***}{'100%'}>
              <Input
                placeholder${DB_USER:***REMOVED***}"ваш домен"
                style${DB_USER:***REMOVED***}{{ borderTopRightRadius: 0, borderBottomRightRadius: 0 }}
                bg${DB_USER:***REMOVED***}{'bg'}
                border${DB_USER:***REMOVED***}{'none'}
                value${DB_USER:***REMOVED***}{input}
                onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> {
                  setInput(e.target.value);
                  if (error) setError('');
                }}
              />
              <Box
                bg${DB_USER:***REMOVED***}{'accent.solid'}
                color${DB_USER:***REMOVED***}{'accent.contrast'}
                p${DB_USER:***REMOVED***}{2}
                paddingRight${DB_USER:***REMOVED***}{4}
                borderRadius${DB_USER:***REMOVED***}{'md'}
                style${DB_USER:***REMOVED***}{{ borderTopLeftRadius: 0, borderBottomLeftRadius: 0 }}
                height${DB_USER:***REMOVED***}{'2.5em'}
                width${DB_USER:***REMOVED***}{'8.5em'}
                overflow${DB_USER:***REMOVED***}{'hidden'}
              >
                <Box
                  opacity${DB_USER:***REMOVED***}{isFading ? 0 : 1}
                  transition${DB_USER:***REMOVED***}{'opacity 300ms ease'}
                >
                  {domains[domainIndex]}
                </Box>
              </Box>
            </HStack>
            <Button colorPalette${DB_USER:***REMOVED***}{'accent'} type${DB_USER:***REMOVED***}{'submit'}>
              Проверить
            </Button>
          </HStack>
          {error && (
            <Text fontSize${DB_USER:***REMOVED***}{'sm'} color${DB_USER:***REMOVED***}{'red.500'}>
              {error}
            </Text>
          )}
        </form>
      </Stack>
    </Stack>
  );
};

export default WelcomePage;
