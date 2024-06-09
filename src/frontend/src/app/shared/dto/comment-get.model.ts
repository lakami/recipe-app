import {AuthorGetModel} from "./author-get.model";

export class CommentGetModel {
    constructor(
        public id: number,
        public recipeId: number,
        public content: string,
        public createdDate: number,
        public createdBy: AuthorGetModel,
        public lastModifyDate: number,
        public lastModifyBy: AuthorGetModel,
    ) {}
}
