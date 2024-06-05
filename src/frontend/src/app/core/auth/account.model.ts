export class Account {
  constructor(
    private firstName: string,
    private lastName: string,
    private email: string,
    private imageUrl: string,
    private profileUrl: string,
    public authorities: Authority[],
  ) {
  }
}

export type Authority = 'ROLE_USER' | 'ROLE_ADMIN' | 'ANONYMOUS';
