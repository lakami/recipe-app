import {Component, inject, OnInit, signal} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TranslationDirective} from "../shared/translation/translation.directive";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";
import {HlmCheckboxComponent} from "@spartan-ng/ui-checkbox-helm";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {AccountService} from "../core/auth/account.service";
import {ActivateService} from "./activate.service";

@Component({
  selector: 'app-activate',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    HlmInputDirective,
    HlmCheckboxComponent,
    HlmButtonDirective,
    TranslationDirective
  ],
  templateUrl: './activate.component.html',
  styleUrl: './activate.component.scss'
})
export class ActivateComponent implements OnInit {
  authError = signal(false);
  activateLoading = signal(false);
  activateForm : FormGroup = new FormGroup({
    firstName: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(50)]
    }),
    lastName: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(50)]
    })
  });
  private accountService: AccountService = inject(AccountService);
  private router: Router = inject(Router);
  private activateService: ActivateService = inject(ActivateService);
  private activatedRoute: ActivatedRoute = inject(ActivatedRoute);

  ngOnInit(): void {
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']); //nawigacja do strony głównej
      }
    });
  }

  onSubmit() {
    var token = this.activatedRoute.snapshot.queryParams['token'];
    this.activateLoading.set(true);
    this.activateService.activate(
        this.activateForm.getRawValue(),
        token //token z linku strony wysłanej mailem
    ).subscribe({
        next: () => {
            this.activateLoading.set(false);
            this.authError.set(false);
            if (!this.router.getCurrentNavigation()) {
                this.router.navigate(['']);
            }
        }, error: () => {
            this.activateLoading.set(false);
      }
    });
  }

  hasFirstNameError(): boolean {
    let firstNameControl = this.activateForm.get('firstName');
    console.log(firstNameControl);
    return firstNameControl!.invalid && (firstNameControl!.touched || firstNameControl!.dirty);
  }

    hasLastNameError(): boolean {
    let lastNameControl = this.activateForm.get('lastName');
    console.log(lastNameControl);
    return lastNameControl!.invalid && (lastNameControl!.touched || lastNameControl!.dirty);
    }
}
