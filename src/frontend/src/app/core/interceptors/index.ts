import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {AuthExpiredInterceptor} from "./auth-expired.interceptor";

export const httpInterceptorProviders = [
    { provide: HTTP_INTERCEPTORS,
      useClass: AuthExpiredInterceptor,
      multi: true
    }
  ];
