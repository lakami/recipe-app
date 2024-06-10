export class Account {
  constructor(
    public firstName: string,
    public lastName: string,
    public email: string,
    public imageUrl: string,
    public profileUrl: string,
    public authorities: Authority[],
  ) {
  }
}

export type Authority = 'ROLE_USER' | 'ROLE_ADMIN' | 'ANONYMOUS';
