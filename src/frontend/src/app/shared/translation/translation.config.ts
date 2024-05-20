import {HttpClient} from '@angular/common/http';
import {MissingTranslationHandler, MissingTranslationHandlerParams, TranslateLoader} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';

export const translationNotFoundMessage = 'translation-not-found';

export class MissingTranslationHandlerImpl implements MissingTranslationHandler {
  handle(params: MissingTranslationHandlerParams): string {
    const key = params.key;
    return `${translationNotFoundMessage}[${key}]`;
  }
}

export function translatePartialLoader(http: HttpClient): TranslateLoader {
  return new TranslateHttpLoader(http);
}

export function missingTranslationHandler(): MissingTranslationHandlerImpl {
  return new MissingTranslationHandlerImpl();
}
