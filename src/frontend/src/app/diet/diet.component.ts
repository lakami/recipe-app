import {Component, inject} from '@angular/core';
import {SearchComponent} from "../search/search.component";
import {ActivatedRoute} from "@angular/router";
import {map, tap} from "rxjs";
import {AsyncPipe} from "@angular/common";

@Component({
  selector: 'app-diet',
  standalone: true,
  imports: [
    SearchComponent,
    AsyncPipe
  ],
  templateUrl: './diet.component.html',
  styleUrl: './diet.component.scss'
})
export class DietComponent {
  private route: ActivatedRoute = inject(ActivatedRoute);
  currentlySelectedDiet$ = this.route.params.pipe(
    map(params => params["dietName"]),
    tap(value => console.log(value))
  );
}
