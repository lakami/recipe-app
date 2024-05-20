import {inject, Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {mergeMap, Observable} from "rxjs";
import {Login} from "./login.module";
import {Account} from "../core/auth/account.module";
import {AccountService} from "../core/auth/account.service";
import {AuthService} from "../core/auth/auth.service";

@Injectable({providedIn: 'root'})
export class LoginService {
  private accountService = inject(AccountService);
  private authService = inject(AuthService);
  private router = inject(Router);

  login(credentials: Login): Observable<Account | null> {
    return this.authService.login(credentials).pipe(mergeMap(() => this.accountService.identity(true)));
  }

  logoutInClient(): void { //czyszczenie danych frontendu
    this.accountService.authenticate(null);
  }

  logout(): void {
    this.authService.logout().subscribe({
        complete: () => {
            this.accountService.authenticate(null);
            this.router.navigate(['/login']);
        }
    });
  }
}
