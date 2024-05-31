import {AuthorGetModel} from "./author-get.model";

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
        public categories: CategoryGetModel[],
        public dishes: DishGetModel[],
        public author: AuthorGetModel,
        public createDate: number,
    ) {}
}

export class IngredientGetModel {
    constructor(
        public name: string,
    ) {}
}

export class StepGetModel {
    constructor(
        public number: number,
        public description: string,
    ) {}
}

export class TagGetModel {
    constructor(
        public id: number,
        public name: string,
    ) {}
}

export class CategoryGetModel {
    constructor(
        public id: number,
        public name: string,
    ) {}
}

export class DishGetModel {
    constructor(
        public id: number,
        public name: string,
    ) {}
}
