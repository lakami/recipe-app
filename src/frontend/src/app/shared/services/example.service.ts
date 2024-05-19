import { Injectable } from '@angular/core';
import {Observable, of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ExampleService {

  constructor() { }

  sum(a: number, b: number): number {
    return a + b;
  }

  askServerForSum(a: number, b: number): Observable<number> {
    return of(a + b);
  }
}
