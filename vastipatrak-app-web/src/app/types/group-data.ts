import { PlanData } from "./plan-data";

export interface Group {

    id: number;
    name: string;
    email: string;
    phoneNumber?: string;
    address?: string;
    subscriptionPlan: PlanData;
    subscriptionStartDate: string;
    subscriptionEndDate: string;
    active: boolean;
    createdUser: string;
    createdAt: string;
    modifiedUser: string;
    modifiedAt: string;

}
