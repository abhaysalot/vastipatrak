import { Family } from "./family-data";
import { Group } from "./group-data";
import { Monk } from "./monk-data";
import { Person } from "./person-data";


export interface DashboardData {

    allFamilies?: Family[];
    allPersons?: Person[];
    totalKidsPercentage?: number;
    totalYouthPercentage?: number;
    totalSeniorPercentage?: number;
    allMonks?:  Monk[];
    allNativePlaces?: string[];
    allSamaj?: string[];
    totalGruhJinalay?: number;
    averageIncome?: number;
    wantToHelpSadharmik?: Family[];
    educationNumbers?: Map<string, Person[]>;
    occupationNumbers?: Map<string, Person[]>;
    group : Group;

}


