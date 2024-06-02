import {inject, Pipe, PipeTransform} from '@angular/core';
import {Observable} from "rxjs";
import {TranslateService} from "@ngx-translate/core";

@Pipe({
  name: 'translation',
  standalone: true
})
export class TranslationPipe implements PipeTransform {
  private translateService = inject(TranslateService);
  transform(key: string, ...args: unknown[]): Observable<string | any> {
    return this.translateService.get(key, args); //zwracam asynchroniczny strumień
  }

}
