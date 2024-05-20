import {inject, Injectable} from "@angular/core";
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {StateStorageService} from "../auth/state-storage.service";
import {Router} from "@angular/router";
import {Observable, tap} from "rxjs";
import {environment} from "../../../enviroments/enviroment.development";
import {LoginService} from "../../login/login.service";

@Injectable()
export class AuthExpiredInterceptor implements HttpInterceptor {
  private loginService = inject(LoginService);
  private stateStorageService = inject(StateStorageService);
  private router = inject(Router);

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap({
        error: (err: HttpErrorResponse) => {
          if (err.status === 401 && err.url && !err.url.includes('api/account')) {
            if (err.url.includes(environment.logout)) {
              this.loginService.logout();
              return;
            }
            this.stateStorageService.storeUrl(this.router.routerState.snapshot.url);
            this.loginService.logout();
            this.router.navigate(['/login']);
          }
        },
      }),
    );
  }
}
