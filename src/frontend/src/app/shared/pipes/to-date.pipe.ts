import {inject, Pipe, PipeTransform} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {format} from "date-fns";
import {enGB, pl} from "date-fns/locale";

@Pipe({
  name: 'toDate',
  standalone: true
})
export class ToDatePipe implements PipeTransform {
  private _translateService = inject(TranslateService);

  transform(epochMs: number): string {
    const locale = this._translateService.currentLang === 'pl' ? pl : enGB;
    let formatStr = 'HH:mm dd/MM/y'
    return format(new Date(epochMs),
      formatStr,
      {locale: locale});
  }

}
