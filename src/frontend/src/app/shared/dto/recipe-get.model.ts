import {AuthorGetModel} from "./author-get.model";
import {DietGetModel} from "./diet-get.model";
import {TagGetModel} from "./tag-get.model";
import {StepGetModel} from "./step-get.model";
import {IngredientGetModel} from "./ingredient-get.model";
import {DishGetModel} from "./dish-get.model";

export class RecipeGetModel {
    constructor(
        public id: number,
        public name: string,
        public description: string,
        public imageUrl: string,
        public servings: number,
        public preparationTime: number,
        public ingredients: IngredientGetModel[],
        public steps: StepGetModel[],
        public tags: TagGetModel[],
        public categories: DietGetModel[], // TODO: fix when backend is changed to diets
        public dishes: DishGetModel[],
        public author: AuthorGetModel,
        public createdDate: number,
    ) {}
}
