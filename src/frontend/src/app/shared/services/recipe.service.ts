import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CategoryGetModel, RecipeGetModel} from "../dto/recipe-get.model";
import {PageGetModel} from "../dto/page-get.model";
import {environment} from "../../../enviroments/enviroment.development";
import {DishGetModel} from "../dto/dish-get.model";


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

  getRecipesByDish(dish: string[]): Observable<PageGetModel<RecipeGetModel>> {
    return this.http.get<PageGetModel<RecipeGetModel>>(
      `${environment.recipe}?dishes=${dish.join(',').replaceAll(' ', '+')}`
    )
  }

  getDishes(): Observable<DishGetModel[]> {
    return this.http.get<DishGetModel[]>(`${environment.dish}`)
  }

  getRecipesByDiet(diet: string[]): Observable<PageGetModel<RecipeGetModel>> {
    return this.http.get<PageGetModel<RecipeGetModel>>(
      `${environment.recipe}?category=${diet.join(',').replaceAll(' ', '+')}`
    )
  }

  getDiets(): Observable<CategoryGetModel[]> {
    return this.http.get<CategoryGetModel[]>(`${environment.diet}`)
  }

  // getDishById(dishId: any) {
  //
  // }

  // getRecipesByTag(tag: string): Observable<PageGetModel<RecipeGetModel>> {
  //   return this.http.get<PageGetModel<RecipeGetModel>>(`${environment.recipe}/tag/${tag}`)
  // }
  //
  // getTags(): Observable<string[]> {
  //   return this.http.get<string[]>(`${environment.recipe}/tag`)
  // }
  //

}
