import {inject, Injectable} from '@angular/core';
import {Register} from "./register.model";
import {Observable} from "rxjs";
import {environment} from "../../enviroments/enviroment.development";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  private http = inject(HttpClient);
  register(credentials: Register): Observable<{}> {
    const fromData = new FormData();
    fromData.append('email', credentials.email);
    fromData.append('password', credentials.password);
    return this.http.post(environment.register, fromData);
  }
}
