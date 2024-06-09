import {Component, inject, Input, OnInit} from '@angular/core';
import {ReplaySubject} from "rxjs";
import {RecipeService} from "../../shared/services/recipe.service";
import {CommentGetModel} from "../../shared/dto/comment-get.model";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {AccountService} from "../../core/auth/account.service";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";

@Component({
  selector: 'app-comment-section',
  standalone: true,
  imports: [
    HlmInputDirective,
    ReactiveFormsModule,
    HlmInputDirective
  ],
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
  comments: ReplaySubject<CommentGetModel[]> = new ReplaySubject<CommentGetModel[]>(1);
  accountService: AccountService = inject(AccountService);

  commentForm: FormGroup = new FormGroup({
    //todo: add validators
    comment: new FormControl('')
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