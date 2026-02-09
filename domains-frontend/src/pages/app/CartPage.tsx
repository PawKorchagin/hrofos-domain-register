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
import type { DomainQuery } from '~/api/models/DomainQuery';
import { RecordType, type Zone as ExdnsZone } from '~/api/models/exdns';
import {
  AddCartItemRequestAction,
  AddCartItemRequestTerm,
} from '~/api/models/domain-order';
import {
  addItem,
  clearCart,
  createDomain,
  getCart,
  getZoneByName,
  removeItem,
  searchDomains,
} from '~/api/services/domain-order';
import { postZonesDomain } from '~/api/services/exdns';
import DomainsTable from '~/components/domainsTable/DomainsTable';

type CartDomain ${DB_USER:***REMOVED***} DomainQuery & { itemId?: string };

type DomainSearchItem ${DB_USER:***REMOVED***} {
  fqdn?: string;
  price?: number;
  free?: boolean;
};

type CartItem ${DB_USER:***REMOVED***} {
  id?: string;
  fqdn?: string;
  price?: number;
};

const mapCartItems ${DB_USER:***REMOVED***} (items: CartItem[]): CartDomain[] ${DB_USER:***REMOVED***}>
  items.map((item) ${DB_USER:***REMOVED***}> ({
    itemId: item.id,
    fqdn: item.fqdn ?? '',
    price: item.price ?? 0,
    free: false,
  }));

const mapSearchItems ${DB_USER:***REMOVED***} (items: DomainSearchItem[]): DomainQuery[] ${DB_USER:***REMOVED***}>
  items.map((item) ${DB_USER:***REMOVED***}> ({
    fqdn: item.fqdn ?? '',
    price: item.price ?? 0,
    free: Boolean(item.free),
  }));

const resolveZoneId ${DB_USER:***REMOVED***} async (fqdn: string) ${DB_USER:***REMOVED***}> {
  const parts ${DB_USER:***REMOVED***} fqdn.split('.').filter(Boolean);
  if (parts.length <${DB_USER:***REMOVED***} 2) return '';

  const zoneName ${DB_USER:***REMOVED***} `${parts[parts.length - 2]}.${parts[parts.length - 1]}`;
  const zoneResponse ${DB_USER:***REMOVED***} await getZoneByName(zoneName);
  return zoneResponse?.data?.id ?? '';
};

const buildZonePayload ${DB_USER:***REMOVED***} (fqdn: string): ExdnsZone ${DB_USER:***REMOVED***}> ({
  name: fqdn,
  version: 1,
  records: [
    {
      name: '@',
      type: RecordType.A,
      ttl: 300,
      data: '127.0.0.1',
    },
  ],
});

const CartPage ${DB_USER:***REMOVED***} () ${DB_USER:***REMOVED***}> {
  const [query, setQuery] ${DB_USER:***REMOVED***} useState<string>('');
  const [cartDomains, setCartDomains] ${DB_USER:***REMOVED***} useState<CartDomain[]>([]);
  const [domains, setDomains] ${DB_USER:***REMOVED***} useState<DomainQuery[]>([]);
  const [isCartLoading, setIsCartLoading] ${DB_USER:***REMOVED***} useState(false);
  const [isSearchLoading, setIsSearchLoading] ${DB_USER:***REMOVED***} useState(false);
  const [error, setError] ${DB_USER:***REMOVED***} useState('');

  const cartCount ${DB_USER:***REMOVED***} useMemo(() ${DB_USER:***REMOVED***}> cartDomains.length, [cartDomains]);

  const loadCart ${DB_USER:***REMOVED***} useCallback(async () ${DB_USER:***REMOVED***}> {
    setIsCartLoading(true);
    setError('');

    try {
      const response ${DB_USER:***REMOVED***} await getCart();
      const items ${DB_USER:***REMOVED***} response?.data?.items ?? [];
      setCartDomains(mapCartItems(items));
    } catch {
      setError('Не удалось загрузить корзину. Попробуйте еще раз.');
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
        const response ${DB_USER:***REMOVED***} await searchDomains({ query: search });
        const items ${DB_USER:***REMOVED***} response?.data ?? [];
        setDomains(mapSearchItems(items));
      } catch {
        setError('Не удалось выполнить поиск доменов. Попробуйте еще раз.');
      } finally {
        setIsSearchLoading(false);
      }
    },
    [query]
  );

  const handlePurchase ${DB_USER:***REMOVED***} useCallback(async () ${DB_USER:***REMOVED***}> {
    if (cartDomains.length ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 0) return;

    setIsCartLoading(true);
    setError('');

    try {
      for (const domain of cartDomains) {
        const fqdn ${DB_USER:***REMOVED***} domain.fqdn.trim();
        if (!fqdn) continue;

        const zoneId ${DB_USER:***REMOVED***} await resolveZoneId(fqdn);
        if (!zoneId) {
          throw new Error('zone');
        }

        const expiresAt ${DB_USER:***REMOVED***} new Date();
        expiresAt.setFullYear(expiresAt.getFullYear() + 1);

        await createDomain({
          fqdn,
          zoneId,
          expiresAt: expiresAt.toISOString(),
        });
        await postZonesDomain(fqdn, buildZonePayload(fqdn));
      }

      await clearCart();
      setCartDomains([]);
    } catch {
      setError('Не удалось оформить покупку. Попробуйте еще раз.');
    } finally {
      setIsCartLoading(false);
    }
  }, [cartDomains]);

  const handleAddToCart ${DB_USER:***REMOVED***} useCallback(
    async (domain: DomainQuery) ${DB_USER:***REMOVED***}> {
      if (!domain.free) return;

      setIsCartLoading(true);
      setError('');

      try {
        await addItem({
          action: AddCartItemRequestAction.register,
          term: AddCartItemRequestTerm.yearly,
          fqdn: domain.fqdn,
          price: domain.price,
        });
        await loadCart();
      } catch {
        setError('Не удалось добавить домен в корзину. Попробуйте еще раз.');
      } finally {
        setIsCartLoading(false);
      }
    },
    [loadCart]
  );

  const handleRemoveFromCart ${DB_USER:***REMOVED***} useCallback(async (domain: CartDomain) ${DB_USER:***REMOVED***}> {
    if (!domain.itemId) return;

    setIsCartLoading(true);
    setError('');

    try {
      await removeItem(domain.itemId);
      setCartDomains((prev) ${DB_USER:***REMOVED***}> prev.filter((item) ${DB_USER:***REMOVED***}> item.itemId !${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} domain.itemId));
    } catch {
      setError('Не удалось удалить домен из корзины. Попробуйте еще раз.');
    } finally {
      setIsCartLoading(false);
    }
  }, []);

  return (
    <Stack gap${DB_USER:***REMOVED***}{4}>
      <HStack justifyContent${DB_USER:***REMOVED***}{'space-between'}>
        <Heading>Корзина</Heading>
        <HStack>
          <Text>{cartCount} доменов</Text>
          <Button
            size${DB_USER:***REMOVED***}{'sm'}
            colorPalette${DB_USER:***REMOVED***}{'secondary'}
            onClick${DB_USER:***REMOVED***}{handlePurchase}
            loading${DB_USER:***REMOVED***}{isCartLoading}
          >
            Приобрести
          </Button>
        </HStack>
      </HStack>
      <form onSubmit${DB_USER:***REMOVED***}{handleSubmit}>
        <HStack alignItems${DB_USER:***REMOVED***}{'flex-end'}>
          <Field.Root>
            <Field.Label>Домен</Field.Label>
            <Input
              placeholder${DB_USER:***REMOVED***}"Введите домен"
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
      <DomainsTable
        domains${DB_USER:***REMOVED***}{cartDomains}
        buttonsFunction${DB_USER:***REMOVED***}{(domain) ${DB_USER:***REMOVED***}> (
          <Button
            size${DB_USER:***REMOVED***}{'sm'}
            colorPalette${DB_USER:***REMOVED***}{'secondary'}
            onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> handleRemoveFromCart(domain as CartDomain)}
            loading${DB_USER:***REMOVED***}{isCartLoading}
          >
            Удалить
          </Button>
        )}
      />
      <Box my${DB_USER:***REMOVED***}{2} />
      <Text>Доступные домены</Text>
      <DomainsTable
        domains${DB_USER:***REMOVED***}{domains}
        buttonsFunction${DB_USER:***REMOVED***}{(domain) ${DB_USER:***REMOVED***}>
          domain.free ? (
            <Button
              size${DB_USER:***REMOVED***}{'sm'}
              colorPalette${DB_USER:***REMOVED***}{'secondary'}
              onClick${DB_USER:***REMOVED***}{() ${DB_USER:***REMOVED***}> handleAddToCart(domain)}
              loading${DB_USER:***REMOVED***}{isCartLoading}
            >
              В корзину
            </Button>
          ) : (
            <Text>Недоступен</Text>
          )
        }
      />
    </Stack>
  );
};

export default CartPage;
