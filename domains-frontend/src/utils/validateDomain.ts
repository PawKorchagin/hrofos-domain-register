import { $error, $ok } from '../common/atoms';

export const validateDomain ${DB_USER:***REMOVED***} (
  domain: string
): [typeof $ok, string] | [typeof $error, string] ${DB_USER:***REMOVED***}> {
  const regex ${DB_USER:***REMOVED***} new RegExp(
    '^(?${DB_USER:***REMOVED***}.{1,253}$)(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?)(?:\\.(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?))*$',
    'i'
  );
  if (domain.length ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} 0)
    return [$error, 'Домен должен содержать хотя бы одну букву'];
  if (domain.length > 63)
    return [$error, 'Домен не может быть больше 63 символов'];
  if (!regex.test(domain))
    return [$error, 'Домен содержит недопустимую последовательность символов'];
  return [$ok, domain];
};
