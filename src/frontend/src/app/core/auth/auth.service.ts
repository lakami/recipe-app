import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Login} from "../../login/login.model";
import {map, Observable} from "rxjs";
import {environment} from "../../../enviroments/enviroment.development";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private http = inject(HttpClient);

    login(credentials: Login): Observable<{}> {
        const fromData = new FormData();
        fromData.append('username', credentials.username);
        fromData.append('password', credentials.password);
        fromData.append('rememberMe', credentials.rememberMe ? 'true' : 'false');
        return this.http.post(environment.login, fromData);
    }

    logout(): Observable<void> {
        return this.http.post<void>(environment.logout, {}).pipe(
            map(() => {
                this.http.get(environment.logout).subscribe();
            })
        );
    }
}
