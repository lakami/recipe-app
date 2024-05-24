import {Component, ElementRef, inject, OnInit, signal, ViewChild} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {LoginService} from "./login.service";
import {AccountService} from "../core/auth/account.service";
import {ThemeService} from "../shared/services/theme.service";
import {map} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";
import {HlmCheckboxComponent} from "@spartan-ng/ui-checkbox-helm";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, HlmInputDirective, HlmInputDirective, HlmCheckboxComponent, HlmButtonDirective],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {
  @ViewChild('username', {static:false})
  username!: ElementRef;
  authError = signal(false);
  loginLoading = signal(false);
  loginForm : FormGroup = new FormGroup({
    username: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.min(5), Validators.max(255)]
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.min(5), Validators.max(255)]
    }),
    rememberMe: new FormControl(false, {nonNullable: true, validators: [Validators.required]})
  });
  private loginService: LoginService = inject(LoginService);
  private accountService: AccountService = inject(AccountService);
  private router: Router = inject(Router);
  private themeService: ThemeService = inject(ThemeService);
  darkTheme$ = this.themeService.theme$
    .pipe(map(theme => theme === 'dark'))
  private translateService: TranslateService = inject(TranslateService);

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

  toggleTheme() {
    this.themeService.toggleDarkMode();
  }

  toggleLanguage() {
    const current = this.translateService.currentLang;
    console.log(current);
    if (current === 'pl') {
      this.translateService.use('en');
    } else {
      this.translateService.use('pl');
    }
  }
}
