import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../enviroments/enviroment.development";
import {Activate} from "./activate.model";

@Injectable({
    providedIn: 'root'
})
export class ActivateService {
    private http = inject(HttpClient);
    activate(credentials: Activate, token: string) {
        return this.http.post(environment.activate+token, credentials);
    }
}
