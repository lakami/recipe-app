import {Component, OnDestroy, OnInit, signal, WritableSignal} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {ExampleService} from "../shared/services/example.service";
import {BehaviorSubject, Observable, Subscription} from "rxjs";
import {AsyncPipe} from "@angular/common";
import {toObservable} from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [
    RouterOutlet,
    AsyncPipe
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit, OnDestroy {
  result!: number;
  a: number = 10;
  b: number = 2;
  sub!: Subscription

  result$!: Observable<number>

  resultBS: BehaviorSubject<number> = new BehaviorSubject<number>(0);

  resultSignal: WritableSignal<number> = signal(0)

  resultAsObservable$: Observable<number> = toObservable(this.resultSignal)



  constructor(private exampleService: ExampleService) {
  }

  ngOnInit() {
    // this.result = this.exampleService.sum(10, 2)
    this.sub = this.exampleService.askServerForSum(1, 2)
      .subscribe(value => {
        this.result = value;
        this.resultBS.next(value)
        this.resultSignal.set(value)
      })

    this.result$ = this.exampleService.askServerForSum(this.a, this.b)
  }

  makeABigger() {
    this.a++;
    this.result$ = this.exampleService.askServerForSum(this.a, this.b)

    this.sub.unsubscribe();
    this.sub = this.exampleService.askServerForSum(this.a, this.b)
      .subscribe(value => {
        this.result = value;
        this.resultBS.next(value)
        this.resultSignal.set(value)
      })
  }

  ngOnDestroy() {
    this.sub.unsubscribe()
  }

}
