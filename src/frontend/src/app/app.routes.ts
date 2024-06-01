import {Routes} from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {errorRoute} from "./error/error.routes";
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {ActivateComponent} from "./activate/activate.component";
import {ActivateSuccessComponent} from "./activate-success/activate-success.component";
import {RegisterSuccessComponent} from "./register-success/register-success.component";
import {RecipeDetailsComponent} from "./recipe-details/recipe-details.component";
import {DishComponent} from "./dish/dish.component";
import {DietComponent} from "./diet/diet.component";

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
  {
    path: 'activate-success',
    component: ActivateSuccessComponent,
    title: 'activate-success.title'
  },
  {
    path: 'register-success',
    component: RegisterSuccessComponent,
    title: 'register-success.title'
  },
  {
    path: 'recipe/:recipeId',
    component: RecipeDetailsComponent,
  },
  // {
  //   path: 'search',
  //   component: SearchComponent,
  // },
  {
    path: 'dish/:dishName',
    component: DishComponent,
  },
  {
    path: 'diet/:dietName',
    component: DietComponent,
  },
  ...errorRoute
];
