import {Routes} from "@angular/router";
import {ErrorComponent} from "./error.component";

export const errorRoute: Routes = [
  {
    path: 'error',
    component: ErrorComponent,
    title: 'error.title',
  },
  {
    path: 'accessdenied',
    component: ErrorComponent,
    data: {
      errorMessage: 'error.http.403',
    },
    title: 'error.title',
  },
  {
    path: '404',
    component: ErrorComponent,
    data: {
      errorMessage: 'error.http.404',
    },
    title: 'error.title',
  },
  {
    path: '**', // nieznane ścieżki przekierowują na stronę /404
    redirectTo: '/404',
  },
];
