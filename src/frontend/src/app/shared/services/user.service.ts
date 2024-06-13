import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserProfileGetModel} from "../dto/user-profile-get.model";
import {environment} from "../../../enviroments/enviroment.development";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http: HttpClient = inject(HttpClient)

  getUser(profileUrl: String): Observable<UserProfileGetModel> {
    return this.http.get<UserProfileGetModel>(`${environment.user}/${profileUrl}`)
  }
}
