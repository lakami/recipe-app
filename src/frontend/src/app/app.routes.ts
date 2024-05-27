import {Routes} from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {errorRoute} from "./error/error.routes";
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {ActivateComponent} from "./activate/activate.component";

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
  {
    path: 'register',
    component: RegisterComponent,
    title: 'register.title'
  },
  {
    path: 'activate',
    component: ActivateComponent,
    title: 'activate.title'
  },
  ...errorRoute
];
