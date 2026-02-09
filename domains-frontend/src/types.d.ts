export {};

declare global {
  interface Window {
    _hrofors?: {
      access?: string;
      refresh?: string;
    };
  }
}
