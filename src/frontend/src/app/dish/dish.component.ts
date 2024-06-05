import {Component, inject} from '@angular/core';
import {SearchComponent} from "../search/search.component";
import {ActivatedRoute} from "@angular/router";
import {map, tap} from "rxjs";
import {AsyncPipe} from "@angular/common";

@Component({
  selector: 'app-dish',
  standalone: true,
  imports: [
    SearchComponent,
    AsyncPipe
  ],
  templateUrl: './dish.component.html',
  styleUrl: './dish.component.scss'
})
export class DishComponent {
  private route: ActivatedRoute = inject(ActivatedRoute);
  currentlySelectedDish$ = this.route.params.pipe(
    map(params => params["dishName"]),
    tap(value => console.log(value))
  );
}
