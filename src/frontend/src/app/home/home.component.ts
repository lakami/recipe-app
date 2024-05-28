import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {ExampleService} from "../shared/services/example.service";
import {BehaviorSubject} from "rxjs";
import {AsyncPipe, CommonModule} from "@angular/common";
import {RecipeService} from "../shared/services/recipe.service";
import {RecipeGetModel} from "../shared/dto/recipe-get.model";

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [
    RouterOutlet,
    AsyncPipe,
    CommonModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit, OnDestroy {
  private recipeService: RecipeService = inject(RecipeService);
  private recipes = new BehaviorSubject<RecipeGetModel[]>([]);
  receipes$ = this.recipes.asObservable();

  // result!: number;
  // a: number = 10;
  // b: number = 2;
  // sub!: Subscription
  //
  // result$!: Observable<number>
  //
  // resultBS: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  //
  // resultSignal: WritableSignal<number> = signal(0)
  //
  // resultAsObservable$: Observable<number> = toObservable(this.resultSignal)
  //
  // numbers: number[] = Array.from({length: 100}, (_, i) => i + 1);

  constructor(private exampleService: ExampleService) {
  }

  ngOnInit() {
    // this.result = this.exampleService.sum(10, 2)
    // this.sub = this.exampleService.askServerForSum(1, 2)
    //   .subscribe(value => {
    //     this.result = value;
    //     this.resultBS.next(value)
    //     this.resultSignal.set(value)
    //   })
    //
    // this.result$ = this.exampleService.askServerForSum(this.a, this.b)

    this.recipeService.getRecipes().subscribe({
      next: (page) => {
        this.recipes.next(page.content);
      },
      error: (error) => {
        console.error(error);
      },
      complete: () => {
        console.log('complete');
      }
    })


  }

  // makeABigger() {
  //   this.a++;
  //   this.result$ = this.exampleService.askServerForSum(this.a, this.b)
  //
  //   this.sub.unsubscribe();
  //   this.sub = this.exampleService.askServerForSum(this.a, this.b)
  //     .subscribe(value => {
  //       this.result = value;
  //       this.resultBS.next(value)
  //       this.resultSignal.set(value)
  //     })
  // }

  ngOnDestroy() {
    // this.sub.unsubscribe()
  }

}
