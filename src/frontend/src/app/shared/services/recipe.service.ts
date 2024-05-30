import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {RecipeGetModel} from "../dto/recipe-get.model";
import {PageGetModel} from "../dto/page-get.model";
import {environment} from "../../../enviroments/enviroment.development";


@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  private http: HttpClient = inject(HttpClient)

  getRecipes(): Observable<PageGetModel<RecipeGetModel>> {
    return this.http.get<PageGetModel<RecipeGetModel>>(environment.recipe)
  }

  getRecipeById(recipeId: number): Observable<RecipeGetModel> {
    return this.http.get<RecipeGetModel>(`${environment.recipe}/${recipeId}`)
  }
}
