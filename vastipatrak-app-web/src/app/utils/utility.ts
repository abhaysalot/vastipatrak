import { JwtTokenData } from "../types/jwt-token-data";

export default class Utility {

    static isNotNullOrUndefined(value: any): boolean {
        return value === null || value === undefined;
    }
}
