import {Component, inject, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {HeaderComponent} from "./header/header.component";
import {AccountService} from "./core/auth/account.service";

@Component({
  selector: 'app-root',
  standalone: true,
    imports: [RouterOutlet, HeaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  // todo: remove this and corresponding test
  sum(a: number, b: number): number {
    return a + b;
  }

  private accountService: AccountService = inject(AccountService);

  ngOnInit(): void {
    this.accountService.identity().subscribe();
  }
}
