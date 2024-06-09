import {Component, inject, OnInit, signal} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {LoginService} from "./login.service";
import {AccountService} from "../core/auth/account.service";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";
import {HlmCheckboxComponent} from "@spartan-ng/ui-checkbox-helm";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {TranslationDirective} from "../shared/translation/translation.directive";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    HlmInputDirective,
    HlmCheckboxComponent,
    HlmButtonDirective,
    TranslationDirective
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {
  authError = signal(false);
  loginLoading = signal(false);
  loginForm : FormGroup = new FormGroup({
    username: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(255)]
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(255)]
    }),
    rememberMe: new FormControl(false, {nonNullable: true, validators: [Validators.required]})
  });
  private loginService: LoginService = inject(LoginService);
  private accountService: AccountService = inject(AccountService);
  private router: Router = inject(Router);

  ngOnInit() {
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']); //nawigacja do strony głównej
      }
    });
  }
  onSubmit() {
    this.loginLoading.set(true);
    this.loginService.login(this.loginForm.getRawValue()).subscribe({
      next: () => {
        this.loginLoading.set(false);
        this.authError.set(false);
        if (!this.router.getCurrentNavigation()) {
          this.router.navigate(['']);
        }
      },
      error: () => {
        this.loginLoading.set(false);
        this.authError.set(true);
      },
    })
  }
}
