import {ShortRecipeGetModel} from "./short-recipe-get.model";

export class UserProfileGetModel {
    constructor(
        public firstName: string,
        public lastName: string,
        public profileUrl: string,
        public imageUrl: string,
        public authorRecipes: ShortRecipeGetModel[],
    ) {}
}
