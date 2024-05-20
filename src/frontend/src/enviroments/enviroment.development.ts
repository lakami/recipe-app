const baseUrl: string = '/api/v1';
const base = (path: string) => baseUrl + path;

export const environment = {
  login: base('/login'),
  logout: base('/logout'),
  account: base('/account'),
}
