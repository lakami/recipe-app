import {Injectable} from "@angular/core";
import {map, Observable} from "rxjs";
import {BreakpointObserver} from "@angular/cdk/layout";

@Injectable({
    providedIn: 'root'
})
export class LayoutService {
    xs$: Observable<boolean> = this.observeLayout(Layout.xs)
    sm$: Observable<boolean> = this.observeLayout(Layout.sm)
    md$: Observable<boolean> = this.observeLayout(Layout.md)
    lg$: Observable<boolean> = this.observeLayout(Layout.lg)
    xl$: Observable<boolean> = this.observeLayout(Layout.xl)
    xxl$: Observable<boolean> = this.observeLayout(Layout.xxl)
    layouts$: Observable<LayoutState> = this.breakpointObserver
        .observe(Object.values(Layout))
        .pipe(
            map(state => state.breakpoints),
            map(breakpoints => {
                const layoutState: LayoutState = {
                    xs: breakpoints[Layout.xs],
                    sm: breakpoints[Layout.sm],
                    md: breakpoints[Layout.md],
                    lg: breakpoints[Layout.lg],
                    xl: breakpoints[Layout.xl],
                    xxl: breakpoints[Layout.xxl]
                }
                return layoutState;
            })
        )

    constructor(private breakpointObserver: BreakpointObserver) {
    }

    public observeLayout(layout: Layout): Observable<boolean> {
        return this.breakpointObserver.observe(layout).pipe(map(res => res.matches))
    }
}

export enum Layout {
    xs = '(min-width: 0px)',
    sm = '(min-width: 640px)',
    md = '(min-width: 768px)',
    lg = '(min-width: 1024px)',
    xl = '(min-width: 1280px)',
    xxl = '(min-width: 1536px)'
}

export interface LayoutState {
    xs: boolean,
    sm: boolean,
    md: boolean,
    lg: boolean,
    xl: boolean,
    xxl: boolean
}
