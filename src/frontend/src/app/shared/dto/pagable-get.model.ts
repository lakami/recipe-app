export class PageableGetModel {
    constructor(
        public pageNumber: number,
        public pageSize: number,
        public sort: SortGetModel,
        public offset: number,
        public unpaged: boolean,
        public paged: boolean,
    ) {}
}

export class SortGetModel {
    constructor(
        public empty: boolean,
        public sorted: boolean,
        public unsorted: boolean,
    ) {}
}
