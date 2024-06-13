import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {RecipeGetModel} from "../dto/recipe-get.model";
import {PageGetModel} from "../dto/page-get.model";
import {environment} from "../../../enviroments/enviroment.development";
import {DishGetModel} from "../dto/dish-get.model";
import {TagGetModel} from "../dto/tag-get.model";
import {DietGetModel} from "../dto/diet-get.model";
import {CommentGetModel} from "../dto/comment-get.model";


@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  private http: HttpClient = inject(HttpClient)

  getRecipeById(recipeId: number): Observable<RecipeGetModel> {
    return this.http.get<RecipeGetModel>(`${environment.recipe}/${recipeId}`)
  }

  getDishes(): Observable<DishGetModel[]> {
    return this.http.get<DishGetModel[]>(`${environment.dish}`)
  }

  getDiets(): Observable<DietGetModel[]> {
    return this.http.get<DietGetModel[]>(`${environment.diet}`)
  }

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
    diets.length > 0 ? params = params.set('diets', diets.replaceAll(" ", "+")) : null;
    tags.length > 0 ? params = params.set('tags', tags.replaceAll(" ", "+")) : null;
    search ? params = params.set('search', search.replaceAll(" ", "+")) : null;
    page ? params = params.set('page', page.toString()) : null;
    size ? params = params.set('size', size.toString()) : null;
    params = params.set('sort', sort);
    return this.http.get<PageGetModel<RecipeGetModel>>(environment.recipe, {
      params: params
    })
  }

  addRecipe(name: string,
            description: string,
            preparationTime: number,
            servings: number,
            dishes: string[],
            diets: string[],
            tags: string[],
            steps: string[],
            ingredients: string[],
            image: File): Observable<number> {
    const recipe = new FormData();
    recipe.append('image', image);
    recipe.append('recipe', new Blob([JSON.stringify({
        name: name,
        description: description,
        preparationTime: preparationTime,
        servings: servings,
        dishes: dishes,
        diets: diets,
        tags: tags,
        steps: steps,
        ingredients: ingredients
        })], {
      type: "application/json"
    }));

    return this.http.post<number>(environment.recipe, recipe)
  }

  updateRecipe(id: number,
               name: string,
               description: string,
               preparationTime: number,
               servings: number,
               dishes: string[],
               diets: string[],
               tags: string[],
               steps: string[],
               ingredients: string[]): Observable<any> {
    return this.http.put<any>(environment.recipe + `/${id}`,{
        name: name,
        description: description,
        preparationTime: preparationTime,
        servings: servings,
        dishes: dishes,
        diets: diets,
        tags: tags,
        steps: steps,
        ingredients: ingredients
      })
  }

  addDish(name: any): Observable<DishGetModel> {
    return this.http.post<DishGetModel>(environment.dish, name)
  }

  addDiet(name: any): Observable<DietGetModel> {
    return this.http.post<DietGetModel>(environment.diet, name)
  }

  addTag(name: any): Observable<TagGetModel> {
    return this.http.post<TagGetModel>(environment.tag, name)
  }

  addFavourite(recipeId: number): Observable<any> {
    return this.http.post<any>(`${environment.favourites}/${recipeId}`, null)
  }

  deleteFavourite(recipeId: number): Observable<any> {
    return this.http.delete<any>(`${environment.favourites}/${recipeId}`)
  }

  getFavourites(): Observable<RecipeGetModel[]> {
    return this.http.get<RecipeGetModel[]>(`${environment.favourites}`)
  }

  isFavourite(recipeId: number): Observable<boolean> {
    return this.http.get<boolean>(`${environment.favourites}/${recipeId}`)
  }

  addPromoted(recipeId: number): Observable<any> {
    return this.http.post<any>(`${environment.promoted}/${recipeId}`, null)
  }

  deletePromoted(recipeId: number): Observable<any> {
    return this.http.delete<any>(`${environment.promoted}/${recipeId}`)
  }

  getAllPromoted(): Observable<RecipeGetModel[]> {
    return this.http.get<RecipeGetModel[]>(`${environment.promoted}`)
  }

  isPromoted(recipeId: number): Observable<boolean> {
    return this.http.get<boolean>(`${environment.promoted}/${recipeId}`)
  }

  getComments(recipeId: number): Observable<CommentGetModel[]> {
    return this.http.get<CommentGetModel[]>(`${environment.comment}/${recipeId}`)
  }

  deleteComment(commentId: number, recipeId: number): Observable<any> {
    return this.http.delete<any>(`${environment.comment}/${recipeId}/${commentId}`)
  }

  addComment(recipeId: number, content: string): Observable<CommentGetModel> {
    return this.http.post<CommentGetModel>(`${environment.comment}/${recipeId}`, {content: content})
  }

  updateComment(commentId: number, recipeId: number, content: string): Observable<CommentGetModel> {
    return this.http.put<CommentGetModel>(
      `${environment.comment}/${recipeId}/${commentId}`,
      {content: content}
    )
  }

  updateImage(recipeId: number, image: File): Observable<any> {
    const formImage = new FormData();
    formImage.append('image', image);
    return this.http.put<any>(`${environment.images}/${recipeId}`, formImage)
  }

}
