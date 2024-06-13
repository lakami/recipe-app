import {Component, EventEmitter, inject, Input, Output} from '@angular/core';
import {TranslationDirective} from "../../../shared/translation/translation.directive";
import {CommentGetModel} from "../../../shared/dto/comment-get.model";
import {lucideMoreHorizontal, lucidePlus, lucideSave} from "@ng-icons/lucide";
import {HlmIconComponent, provideIcons} from "@spartan-ng/ui-icon-helm";
import {RecipeService} from "../../../shared/services/recipe.service";
import {BrnMenuTriggerDirective} from "@spartan-ng/ui-menu-brain";
import {RouterLink} from "@angular/router";
import {HlmMenuComponent, HlmMenuGroupComponent, HlmMenuItemDirective} from "@spartan-ng/ui-menu-helm";
import {AccountService} from "../../../core/auth/account.service";
import {Clipboard, ClipboardModule} from '@angular/cdk/clipboard';
import {BehaviorSubject} from "rxjs";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TranslationPipe} from "../../../shared/translation/translation.pipe";
import {AsyncPipe} from "@angular/common";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";
import {ToDatePipe} from "../../../shared/pipes/to-date.pipe";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";

@Component({
  selector: 'app-comment',
  standalone: true,
  imports: [
    TranslationDirective,
    BrnMenuTriggerDirective,
    HlmIconComponent,
    RouterLink,
    HlmMenuGroupComponent,
    HlmMenuItemDirective,
    ClipboardModule,
    HlmMenuComponent,
    TranslationPipe,
    ReactiveFormsModule,
    HlmInputDirective,
    AsyncPipe,
    ToDatePipe,
    HlmButtonDirective
  ],
  providers: [provideIcons({lucidePlus, lucideMoreHorizontal, lucideSave})],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.scss'
})
export class CommentComponent {
  @Input({required:true}) comment!: CommentGetModel;
  @Input({required:true}) recipeId!: number;
  @Output() commentChanged = new EventEmitter<void>();
  @Output() commentDeleted = new EventEmitter<void>();

  private recipeService: RecipeService = inject(RecipeService);
  private accountService: AccountService = inject(AccountService);
  private clipboard: Clipboard = inject(Clipboard);
  isEditing: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  form: FormGroup = new FormGroup({
    comment: new FormControl('', {
      nonNullable: true,
      validators: [Validators.minLength(1), Validators.maxLength(1000)]
    })
  });


  deleteComment(): void {
    this.recipeService.deleteComment(this.comment.id, this.recipeId).subscribe(() => {
      this.commentDeleted.emit();
    });
  }

  canUserModifyComment(): boolean {
    return this.accountService.isAuthenticated()
      && (this.accountService.trackCurrentAccount()()!.profileUrl === this.comment.createdBy.profileUrl
        || this.accountService.hasAnyAuthority('ROLE_ADMIN'));
  }

  copy() {
    this.clipboard.copy(this.comment.content);
  }

  toggleEdit() {
    if (this.isEditing.value) {
      this.isEditing.next(false);
    } else {
      this.isEditing.next(true);
      this.form.patchValue({comment: this.comment.content});
    }
  }

  editComment() {
      if (this.form.valid) {
      this.recipeService.updateComment(this.comment.id, this.recipeId, this.form.value.comment).subscribe(() => {
        this.commentChanged.emit();
        this.isEditing.next(false);
      });
    } }
}
