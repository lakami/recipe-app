export class Account {
  constructor(
    private username: string,
    private email: string,
    public authorities: Authority[],
  ) {
  }
}

export type Authority = 'ROLE_USER' | 'ROLE_ADMIN' | 'ANONYMOUS';
