import {RecipeGetModel} from "./recipe-get.model";

export class UserProfileGetModel {
    constructor(
        public firstName: string,
        public lastName: string,
        public profileUrl: string,
        public imageUrl: string,
        public authoredRecipes: RecipeGetModel[],
    ) {}
}
