import {Component} from '@angular/core';
import {SearchComponent} from "../search/search.component";

@Component({
  selector: 'app-diet',
  standalone: true,
    imports: [
        SearchComponent
    ],
  templateUrl: './diet.component.html',
  styleUrl: './diet.component.scss'
})
export class DietComponent {

}
