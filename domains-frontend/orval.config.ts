import { defineConfig } from 'orval';

export default defineConfig({
  auth: {
    // input: 'src/api/specs/auth-openapi.json',
    input: 'http://localhost:8080/api/auth/v3/api-docs',
    output: {
      client: 'axios-functions',
      target: 'src/api/services/auth.ts',
      schemas: 'src/api/models/auth',
      override: {
        mutator: {
          path: './src/api/apiClient.ts',
          name: 'customInstance',
        },
      },
    },
  },
  domainOrder: {
    // input: 'src/api/specs/domain-order-openapi.json',
    input: 'http://localhost:8080/api/domains/v3/api-docs',
    output: {
      client: 'axios-functions',
      target: 'src/api/services/domain-order.ts',
      schemas: 'src/api/models/domain-order',
      override: {
        mutator: {
          path: './src/api/apiClientDomains.ts',
          name: 'customInstance',
        },
      },
    },
  },
  exdns: {
    input: 'src/api/specs/exdns.json',
    output: {
      client: 'axios-functions',
      target: 'src/api/services/exdns.ts',
      schemas: 'src/api/models/exdns',
      override: {
        mutator: {
          path: './src/api/apiClientExdns.ts',
          name: 'customInstance',
        },
      },
    },
  },
});
