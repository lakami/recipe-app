import {Routes} from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {errorRoute} from "./error/error.routes";
import {LoginComponent} from "./login/login.component";

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: 'home.title'
  },
  {
    path: 'login',
    component: LoginComponent,
    title: 'login.title'
  },
  ...errorRoute
];
