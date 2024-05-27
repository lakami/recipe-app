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
    return this.http.post(environment.register, credentials);
  }
}
