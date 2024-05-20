import {inject, isDevMode, NgModule} from "@angular/core";
import {MissingTranslationHandler, TranslateLoader, TranslateModule, TranslateService} from "@ngx-translate/core";
import {HttpClient} from "@angular/common/http";
import {missingTranslationHandler, translatePartialLoader} from "./translation.config";
import {StateStorageService} from "../../core/auth/state-storage.service";

const defaultLang = 'pl'

@NgModule({
  declarations: [],
  imports: [
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: translatePartialLoader,
        deps: [HttpClient]
      },
      missingTranslationHandler: {
        provide: MissingTranslationHandler,
        useFactory: missingTranslationHandler
      }
    })
  ]
})
export class TranslationModule {
  private translationService = inject(TranslateService);
  private stateStorageService = inject(StateStorageService);

  constructor() {
    this.translationService.setDefaultLang(defaultLang);
    const storedLocale = this.stateStorageService.getLocale();
    if (isDevMode()) {
      console.log('Stored locale', storedLocale);
    }
    const langKey = storedLocale ?? defaultLang;
    this.translationService.use(langKey);
  }
}
