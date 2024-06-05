import {PageableGetModel, SortGetModel} from "./pagable-get.model";

export class PageGetModel<ContentType> {
    constructor(
        public last: boolean,
        public totalPages: number,
        public totalElements: number,
        public size: number,
        public number: number,
        public sort: SortGetModel,
        public numberOfElements: number,
        public first: boolean,
        public empty: boolean,
        public content: ContentType[],
        public pageable: PageableGetModel,
    ) {}
}
