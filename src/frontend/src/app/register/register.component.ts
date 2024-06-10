import {Component, ElementRef, inject, OnInit, signal, ViewChild} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {AccountService} from "../core/auth/account.service";
import {RegisterService} from "./register.service";
import {TranslationDirective} from "../shared/translation/translation.directive";
import {AsyncPipe} from "@angular/common";
import {TranslationPipe} from "../shared/translation/translation.pipe";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    HlmInputDirective,
    HlmButtonDirective,
    TranslationDirective,
    AsyncPipe,
    TranslationPipe
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent implements OnInit {
  @ViewChild('username', {static:false})
  username!: ElementRef;
  registerLoading = signal(false);
    registerForm : FormGroup = new FormGroup({
      email: new FormControl('', {
          nonNullable: true,
          validators: [Validators.required, Validators.email, Validators.minLength(5), Validators.maxLength(128)]
      }),
      password: new FormControl('', {
          nonNullable: true,
          validators: [Validators.required, Validators.minLength(8), Validators.maxLength(64)]
      })
  });
  private accountService: AccountService = inject(AccountService);
  private router: Router = inject(Router);
  private registerService: RegisterService = inject(RegisterService);

  ngOnInit() {
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']); //nawigacja do strony głównej
      }
    });
  }
  onSubmit() {
    this.registerLoading.set(true);
    this.registerService.register(this.registerForm.getRawValue()).subscribe({
        next: () => {
            this.registerLoading.set(false);
            if (!this.router.getCurrentNavigation()) {
                this.router.navigate(['']);
            }
        }, error: () => {
            this.registerLoading.set(false);
      }
    });
  }

  hasEmailError(): boolean {
      let emailControl = this.registerForm.get('email');
      console.log(emailControl?.errors);
      return emailControl!.invalid && (emailControl!.touched || emailControl!.dirty);
  }

  hasPasswordError(): boolean {
      let passwordControl = this.registerForm.get('password');
        console.log(passwordControl?.errors);
        return passwordControl!.invalid && (passwordControl!.touched || passwordControl!.dirty);
  }
}
