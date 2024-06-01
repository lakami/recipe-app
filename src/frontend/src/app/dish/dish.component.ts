import {Component} from '@angular/core';
import {SearchComponent} from "../search/search.component";

@Component({
  selector: 'app-dish',
  standalone: true,
  imports: [
    SearchComponent
  ],
  templateUrl: './dish.component.html',
  styleUrl: './dish.component.scss'
})
export class DishComponent {

}
