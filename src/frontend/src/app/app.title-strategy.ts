import {inject, Injectable} from "@angular/core";
import {RouterStateSnapshot, TitleStrategy} from "@angular/router";
import {TranslateService} from "@ngx-translate/core";

@Injectable()
export class AppTitleStrategy extends TitleStrategy {
    public translateService = inject(TranslateService);
    private readonly fallbackTitle = 'main.title';

    override updateTitle(routerState: RouterStateSnapshot) {
        let pageTitle = this.buildTitle(routerState);
        if (!pageTitle) {
            pageTitle = this.fallbackTitle;
        }
        this.translateService.get(pageTitle).subscribe((title) => {
            document.title = title;
        });
    }
}
