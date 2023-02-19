import { Group } from './group-data';

export interface RoleData {
  id?: number;
  roleName: string;
  active: boolean;
  primaryRole: boolean;
  group: Group;
}
