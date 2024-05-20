import {ApplicationConfig, importProvidersFrom, inject} from '@angular/core';
import {
    NavigationError,
    provideRouter,
    Router,
    RouterFeatures,
    TitleStrategy,
    withComponentInputBinding,
    withNavigationErrorHandler
} from '@angular/router';

import {routes} from './app.routes';
import {provideHttpClient} from "@angular/common/http";
import {TranslationModule} from "./shared/translation/translation.module";
import {provideAnimationsAsync} from "@angular/platform-browser/animations/async";
import {Title} from "@angular/platform-browser";
import {httpInterceptorProviders} from "./core/interceptors";
import {AppTitleStrategy} from "./app.title-strategy";

const routeFeatures: Array<RouterFeatures> = [
  withComponentInputBinding(), //umożliwia łączenia danych wejściowych komponentu z parametrami zapytań
  withNavigationErrorHandler((e: NavigationError) => {
  const router = inject(Router);
  if (e.error.status === 403) { //forbidden
    router.navigate(['/accessdenied']);
  } else if (e.error.status === 404) { //not found
    router.navigate(['/404']);
  } else if (e.error.status === 401) { //unauthorized
    router.navigate(['/login']);
  } else {
    router.navigate(['/error']);
  }
  }),
];

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes, ...routeFeatures),
    provideHttpClient(),
    importProvidersFrom(TranslationModule),
    provideAnimationsAsync(),
    Title,
    {provide: 'LOCALE_ID', useValue: 'pl'},
    httpInterceptorProviders,
    {provide:TitleStrategy, useClass: AppTitleStrategy}
  ]
};
