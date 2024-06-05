import {Directive, ElementRef, inject, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from "@angular/core";
import {Subject, takeUntil} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {translationNotFoundMessage} from "./translation.config";

@Directive({
    standalone: true,
    selector: '[translate]'
})

export class TranslationDirective implements OnChanges, OnInit, OnDestroy {
    @Input() translate!: string;
    @Input() translateArgs?: { [key: string]: unknown };

    //cleanup on destroy
    private readonly directiveDestroy = new Subject();

    private element =inject(ElementRef);
    private translateService = inject(TranslateService);

    ngOnInit(): void {
        this.translateService.onLangChange
            .pipe(takeUntil(this.directiveDestroy)).subscribe(() => {
            this.getTranslation();
        });
        this.translateService.onTranslationChange
            .pipe(takeUntil(this.directiveDestroy)).subscribe(() => {
            this.getTranslation();
        });
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.getTranslation();
    }

    ngOnDestroy(): void {
        //cleanup
        this.directiveDestroy.next(null);
        this.directiveDestroy.complete();
    }

    //Metoda przeładowuje przetłumaczony tekst używając serwisu tłumaczeniowego
    private getTranslation(): void {
        this.translateService
            .get(this.translate, this.translateArgs)
            .pipe(takeUntil(this.directiveDestroy))
            .subscribe({
                next: value => {
                    this.element.nativeElement.innerHTML = value;
                },
            error: () => `${translationNotFoundMessage}[${this.translate}]`,
        });
    }
}
