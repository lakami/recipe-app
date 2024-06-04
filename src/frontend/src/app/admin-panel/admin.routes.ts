import {Routes} from "@angular/router";
import {AdminPanelComponent} from "./admin-panel.component";
import {protectedRoutesGuard} from "../core/auth/protectedRoutesGuard";
import {EditCategoriesComponent} from "./edit-categories/edit-categories.component";

const adminRoutes: Routes = [
    {
      path: '',
      component: AdminPanelComponent,
      title: 'admin-panel.title',
      canActivate: [protectedRoutesGuard],
      data: {
        authorities: ['ROLE_ADMIN'],
      },
      children: [
        {
          path: 'edit-categories',
          component: EditCategoriesComponent,
          title: 'admin-panel.edit-categories.title'
        }
      ]
    }
];
export default adminRoutes;
