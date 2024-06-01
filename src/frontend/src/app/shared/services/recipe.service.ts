import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {RecipeGetModel} from "../dto/recipe-get.model";
import {PageGetModel} from "../dto/page-get.model";
import {environment} from "../../../enviroments/enviroment.development";
import {DishGetModel} from "../dto/dish-get.model";
import {TagGetModel} from "../dto/tag-get.model";
import {DietGetModel} from "../dto/diet-get.model";


@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  private http: HttpClient = inject(HttpClient)

  // getRecipes(): Observable<PageGetModel<RecipeGetModel>> {
  //   return this.http.get<PageGetModel<RecipeGetModel>>(environment.recipe)
  // }

  getRecipeById(recipeId: number): Observable<RecipeGetModel> {
    return this.http.get<RecipeGetModel>(`${environment.recipe}/${recipeId}`)
  }

  // getRecipesByDish(dish: string[]): Observable<PageGetModel<RecipeGetModel>> {
  //   return this.http.get<PageGetModel<RecipeGetModel>>(
  //     `${environment.recipe}?dishes=${dish.join(',').replaceAll(' ', '+')}`
  //   )
  // }

  getDishes(): Observable<DishGetModel[]> {
    return this.http.get<DishGetModel[]>(`${environment.dish}`)
  }

  // getRecipesByDiet(diet: string[]): Observable<PageGetModel<RecipeGetModel>> {
  //   return this.http.get<PageGetModel<RecipeGetModel>>(
  //     `${environment.recipe}?category=${diet.join(',').replaceAll(' ', '+')}`
  //   )
  // }

  getDiets(): Observable<DietGetModel[]> {
    return this.http.get<DietGetModel[]>(`${environment.diet}`)
  }

  // getDishById(dishId: any) {
  //
  // }

  // getRecipesByTag(tag: string): Observable<PageGetModel<RecipeGetModel>> {
  //   return this.http.get<PageGetModel<RecipeGetModel>>(`${environment.recipe}/tag/${tag}`)
  // }

  getTags(): Observable<TagGetModel[]> {
    return this.http.get<TagGetModel[]>(`${environment.tag}`)
  }

  getRecipes(dishes: string = "",
             diets: string = "",
             tags: string = "",
             search?: string,
             page?: number,
             size?: number,
             sort: string = "createdDate,desc"): Observable<PageGetModel<RecipeGetModel>> {
    console.log(dishes, diets, tags, search, page, size, sort);
    var params = new HttpParams();
    dishes.length > 0 ? params = params.set('dishes', dishes.replaceAll(" ", "+")) : null;
    diets.length > 0 ? params = params.set('categories', diets.replaceAll(" ", "+")) : null; // TODO: categories -> diets, when backend is fixed
    tags.length > 0 ? params = params.set('tags', tags.replaceAll(" ", "+")) : null;
    search ? params = params.set('search', search.replaceAll(" ", "+")) : null;
    page ? params = params.set('page', page.toString()) : null;
    size ? params = params.set('size', size.toString()) : null;
    params = params.set('sort', sort);
    return this.http.get<PageGetModel<RecipeGetModel>>(environment.recipe, {
      params: params
    })
  }

}
