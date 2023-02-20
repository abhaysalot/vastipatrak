import { RoleData } from "./role-data";


export interface LoginData {

    id?: number;
    loginId: string;
    password: string;
    userFirstName: string;
    userLastName: string;
    userEmail: string;
    firstLoginDate: string;
    previousLoginDate: string;
    lastLoginDate: string;
    devOpsUser: boolean;
    active: boolean;
    failedAttempts: number;
    lastFailedLoginDate: string;
    roles: RoleData[];

}
