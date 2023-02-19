import { IncomeRange } from "../constants/incomeRange";
import { Group } from "./group-data";
import { Monk } from "./monk-data";
import { Person } from "./person-data";

export interface Family {

    id: number;
    familyName: string;
    primaryPhoneNumber: string;
    primaryEmail: string;
    address: string;

    buildingName?: string;
    nativePlace?: string;
    samaj?: string;

    primaryBusiness?: string;
    incomeRange?: IncomeRange;

    sadharmik : boolean;
    wantToHelpSadharmik : boolean;
    gruhJinalay: boolean;
    garamPani: boolean;
    chowihar: boolean;
    receiveUpdates: boolean;
    active: boolean;

    persons : Person[];
    monks : Monk[];
    group : Group;
    
}