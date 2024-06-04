import {CanActivateFn, Router} from '@angular/router';
import {inject, isDevMode} from "@angular/core";
import {map} from "rxjs";
import {AccountService} from "./account.service";
import {Account} from "./account.model";
import {StateStorageService} from "./state-storage.service";

export const protectedRoutesGuard: CanActivateFn = (targetRoute, state) => {
  const router = inject(Router);
  const accountService = inject(AccountService);
  const stateStorageService = inject(StateStorageService);
  const returnUrl = state.url;

  return accountService.identity().pipe(
    map((account: Account | null) => {
      if (account) {
        const authorities = targetRoute.data['authorities'];

        if (!authorities || authorities.length === 0 || accountService.hasAnyAuthority(authorities)) {
          return true;
        }

        if (isDevMode()) {
          console.error('User has not any of required authorities: ', authorities);
        }

        router.navigate(['accessdenied']);
        return false;
      }

      stateStorageService.storeUrl(returnUrl); // Store the attempted URL for redirecting after login
      router.navigate(['/login']);
      return false;
    })
  );
};
