import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink} from "@angular/router";
import {BehaviorSubject, map, tap} from "rxjs";

import {UserProfileGetModel} from "../shared/dto/user-profile-get.model";
import {AsyncPipe} from "@angular/common";
import {RecipeCardItemComponent} from "../recipe-card-item/recipe-card-item.component";
import {TranslationDirective} from "../shared/translation/translation.directive";
import {UserService} from "../shared/services/user.service";

@Component({
  selector: 'app-recipe-user',
  standalone: true,
  imports: [
    AsyncPipe,
    RecipeCardItemComponent,
    TranslationDirective,
    RouterLink
  ],
  templateUrl: './recipe-author.component.html',
  styleUrl: './recipe-author.component.scss'
})
export class RecipeAuthorComponent implements OnInit {
  private route: ActivatedRoute = inject(ActivatedRoute);
  currentlySelectedAuthor$ = this.route.params.pipe(
    map(params => params["profileUrl"]),
    tap(value => console.log(value))
  );
  private user: BehaviorSubject<UserProfileGetModel | null> = new BehaviorSubject<UserProfileGetModel | null>(null);
  user$ = this.user.asObservable();
  userService: UserService = inject(UserService);

  ngOnInit(): void {
    this.currentlySelectedAuthor$.subscribe(currentlySelectedAuthor => {
      console.log(currentlySelectedAuthor);
      this.userService.getUser(currentlySelectedAuthor).subscribe(data => {
        this.user.next(data);
      });
    });
  }

}
