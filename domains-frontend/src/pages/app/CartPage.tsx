import {
  Box,
  Button,
  Field,
  Heading,
  HStack,
  Input,
  Stack,
  Text,
} from '@chakra-ui/react';
import { useCallback, useEffect, useMemo, useState, type FormEvent } from 'react';
import { ORDER_AXIOS_INSTANCE } from '~/api/apiClientOrders';
import { AXIOS_INSTANCE } from '~/api/apiClientDomains';

interface CartResponse {
  totalMonthlyPrice: number;
  totalYearlyPrice: number;
  l3Domains: string[];
}

interface L2Zone {
  id: number;
  name: string;
  monthlyPrice?: number;
}

interface DomainSearchResult {
  fqdn: string;
  zone: string;
  free: boolean;
  monthlyPrice: number;
}

const CartPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [query, setQuery] ${DB_USER:***REMOVED***} useState('');
  const [cartDomains, setCartDomains] ${DB_USER:***REMOVED***} useState<string[]>([]);
  const [totalMonthly, setTotalMonthly] ${DB_USER:***REMOVED***} useState(0);
  const [totalYearly, setTotalYearly] ${DB_USER:***REMOVED***} useState(0);
  const [searchResults, setSearchResults] ${DB_USER:***REMOVED***} useState<DomainSearchResult[]>([]);
  const [isCartLoading, setIsCartLoading] ${DB_USER:***REMOVED***} useState(false);
  const [isSearchLoading, setIsSearchLoading] ${DB_USER:***REMOVED***} useState(false);
  const [error, setError] ${DB_USER:***REMOVED***} useState('');

  const cartCount ${DB_USER:***REMOVED***} useMemo(() ${DB_USER:***REMOVED***}> cartDomains.length, [cartDomains]);

  const loadCart ${DB_USER:***REMOVED***} useCallback(async () ${DB_USER:***REMOVED***}> {
    setIsCartLoading(true);
    setError('');
    try {
      const { data } ${DB_USER:***REMOVED***} await ORDER_AXIOS_INSTANCE.get<CartResponse>('/cart/me');
      setCartDomains(data.l3Domains ?? []);
      setTotalMonthly(data.totalMonthlyPrice ?? 0);
      setTotalYearly(data.totalYearlyPrice ?? 0);
    } catch {
      setError('Не удалось загрузить корзину.');
    } finally {
      setIsCartLoading(false);
    }
  }, []);

  useEffect(() ${DB_USER:***REMOVED***}> {
    loadCart();
  }, [loadCart]);

  const handleSubmit ${DB_USER:***REMOVED***} useCallback(
    async (e: FormEvent<HTMLFormElement>) ${DB_USER:***REMOVED***}> {
      e.preventDefault();
      const search ${DB_USER:***REMOVED***} query.trim();
      if (!search) return;

      setIsSearchLoading(true);
      setError('');

      try {
        const [freeRes, zonesRes] ${DB_USER:***REMOVED***} await Promise.all([
          AXIOS_INSTANCE.get<string[]>(`/l3Domains/${encodeURIComponent(search)}/free`),
          AXIOS_INSTANCE.get<L2Zone[]>('/l2Domains'),
        ]);

        const freeDomains ${DB_USER:***REMOVED***} new Set(freeRes.data ?? []);
        const zones ${DB_USER:***REMOVED***} zonesRes.data ?? [];

        const results: DomainSearchResult[] ${DB_USER:***REMOVED***} zones.map((zone) ${DB_USER:***REMOVED***}> {
          const fqdn ${DB_USER:***REMOVED***} `${search}.${zone.name}`;
          return {
            fqdn,
            zone: zone.name,
            free: freeDomains.has(fqdn),
            monthlyPrice: zone.monthlyPrice ?? 200,
          };
        });

        setSearchResults(results);
      } catch {
        setError('Не удалось выполнить поиск доменов.');
      } finally {
        setIsSearchLoading(false);
      }
    },
    [query]
  );

  const handleAddToCart ${DB_USER:***REMOVED***} useCallback(
    async (fqdn: string) ${DB_USER:***REMOVED***}> {
      setIsCartLoading(true);
      setError('');
      try {
        await ORDER_AXIOS_INSTANCE.post(`/cart/${encodeURIComponent(fqdn)}`);
        await loadCart();
      } catch {
        setError('Не удалось добавить домен в корзину.');
      } finally {
        setIsCartLoading(false);
      }
    },
    [loadCart]
  );

  const handleCheckout ${DB_USER:***REMOVED***} useCallback(
    async (period: 'MONTH' | 'YEAR') ${DB_USER:***REMOVED***}> {
      if (cartDomains.length ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 0) return;
      setIsCartLoading(true);
      setError('');
      try {
        await ORDER_AXIOS_INSTANCE.post('/cart/checkout', { period });
        setCartDomains([]);
        setTotalMonthly(0);
        setTotalYearly(0);
      } catch {
        setError('Не удалось оформить покупку.');
      } finally {
        setIsCartLoading(false);
      }
    },
    [cartDomains]
  );

  return (
    <Stack gap${DB_USER:***REMOVED***}{4}>
      <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
        <Heading>Корзина</Heading>
        <HStack>
          <Text>{cartCount} доменов</Text>
        </HStack>
      </HStack>

      {/* Cart contents */}
      {cartDomains.length > 0 && (
        <Stack bg${DB_USER:***REMOVED***}{'accent.muted'} p${DB_USER:***REMOVED***}{5} borderRadius${DB_USER:***REMOVED***}{'md'} gap${DB_USER:***REMOVED***}{3}>
          {cartDomains.map((domain) ${DB_USER:***REMOVED***}> (
            <HStack key${DB_USER:***REMOVED***}{domain} justifyContent${DB_USER:***REMOVED***}{'space-between'}>
              <Text>{domain}</Text>
            </HStack>
          ))}
          <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'} pt${DB_USER:***REMOVED***}{3} borderTopWidth${DB_USER:***REMOVED***}{1}>
            <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>
              Итого: {totalMonthly}₽/мес · {totalYearly}₽/год
            </Text>
            <HStack>
              <Button
                size${DB_USER:***REMOVED***}{'sm'}
                colorPalette${DB_USER:***REMOVED***}{'secondary'}
                onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> handleCheckout('MONTH')}
                loading${DB_USER:***REMOVED***}{isCartLoading}
              >
                Купить на месяц
              </Button>
              <Button
                size${DB_USER:***REMOVED***}{'sm'}
                colorPalette${DB_USER:***REMOVED***}{'secondary'}
                onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> handleCheckout('YEAR')}
                loading${DB_USER:***REMOVED***}{isCartLoading}
              >
                Купить на год
              </Button>
            </HStack>
          </HStack>
        </Stack>
      )}

      {/* Search */}
      <form onSubmit${DB_USER:***REMOVED***}{handleSubmit}>
        <HStack alignItems${DB_USER:***REMOVED***}{'flex-end'}>
          <Field.Root>
            <Field.Label>Поиск домена</Field.Label>
            <Input
              placeholder${DB_USER:***REMOVED***}"Введите имя домена"
              value${DB_USER:***REMOVED***}{query}
              onChange${DB_USER:***REMOVED***}{(e) ${DB_USER:***REMOVED***}> setQuery(e.target.value)}
            />
          </Field.Root>
          <Button colorPalette${DB_USER:***REMOVED***}{'accent'} type${DB_USER:***REMOVED***}{'submit'} loading${DB_USER:***REMOVED***}{isSearchLoading}>
            Искать
          </Button>
        </HStack>
      </form>

      {error && (
        <Text color${DB_USER:***REMOVED***}{'fg.error'} fontSize${DB_USER:***REMOVED***}{'sm'}>
          {error}
        </Text>
      )}

      {/* Search results */}
      {searchResults.length > 0 && (
        <Stack bg${DB_USER:***REMOVED***}{'accent.muted'} p${DB_USER:***REMOVED***}{5} borderRadius${DB_USER:***REMOVED***}{'md'} gap${DB_USER:***REMOVED***}{2}>
          <Text fontWeight${DB_USER:***REMOVED***}{'bold'}>Результаты поиска</Text>
          {searchResults.map((result) ${DB_USER:***REMOVED***}> (
            <HStack key${DB_USER:***REMOVED***}{result.fqdn} justifyContent${DB_USER:***REMOVED***}{'space-between'}>
              <Text>{result.fqdn}</Text>
              <HStack>
                <Text>{result.monthlyPrice}₽ / месяц</Text>
                {result.free ? (
                  <Button
                    size${DB_USER:***REMOVED***}{'sm'}
                    colorPalette${DB_USER:***REMOVED***}{'secondary'}
                    onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> handleAddToCart(result.fqdn)}
                    loading${DB_USER:***REMOVED***}{isCartLoading}
                  >
                    В корзину
                  </Button>
                ) : (
                  <Text color${DB_USER:***REMOVED***}{'fg.muted'}>Занят</Text>
                )}
              </HStack>
            </HStack>
          ))}
        </Stack>
      )}
    </Stack>
  );
};

export default CartPage;
