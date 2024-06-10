import {Component, inject, Input, OnInit} from '@angular/core';
import {BehaviorSubject, ReplaySubject} from "rxjs";
import {RecipeService} from "../../shared/services/recipe.service";
import {CommentGetModel} from "../../shared/dto/comment-get.model";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AccountService} from "../../core/auth/account.service";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";
import {TranslationPipe} from "../../shared/translation/translation.pipe";
import {AsyncPipe} from "@angular/common";
import {TranslationDirective} from "../../shared/translation/translation.directive";
import {lucideMoreHorizontal, lucidePlus} from "@ng-icons/lucide";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {HlmIconComponent, provideIcons} from "@spartan-ng/ui-icon-helm";
import {CommentComponent} from "./comment/comment.component";

@Component({
  selector: 'app-comment-section',
  standalone: true,
  imports: [
    HlmInputDirective,
    ReactiveFormsModule,
    HlmInputDirective,
    TranslationPipe,
    AsyncPipe,
    TranslationDirective,
    HlmButtonDirective,
    [HlmIconComponent],
    HlmButtonDirective,
    CommentComponent
  ],
  providers: [provideIcons({lucidePlus, lucideMoreHorizontal})],
  templateUrl: './comment-section.component.html',
  styleUrl: './comment-section.component.scss'
})
export class CommentSectionComponent implements OnInit {
  @Input() set recipeId(value: number) {
    this.currntRecipeId.next(value);
  }

  currntRecipeId: ReplaySubject<number> = new ReplaySubject<number>(1)
  recipeIdx?: number;
  private recipeService: RecipeService = inject(RecipeService);
  comments: BehaviorSubject<CommentGetModel[]> = new BehaviorSubject<CommentGetModel[]>([]);
  accountService: AccountService = inject(AccountService);

  commentForm: FormGroup = new FormGroup({
    comment: new FormControl('', {
      nonNullable: true,
      validators: [Validators.minLength(1), Validators.maxLength(1000)]
    })
  });

  ngOnInit(): void {
    this.currntRecipeId.subscribe(recipeId => {
      console.log(recipeId);
      this.recipeIdx = recipeId;
      this.refreshCommentList(recipeId);
    });
  }

  sendComment() {
    if (this.commentForm.valid) {
      this.recipeService.addComment(this.recipeIdx!, this.commentForm.value.comment).subscribe(() => {
        this.refreshCommentList(this.recipeIdx!);
      });
    }
  }

  refreshCommentList(recipeId: number) {
    this.recipeService.getComments(recipeId).subscribe(data => {
      this.comments.next(data);
    });
  }

}
