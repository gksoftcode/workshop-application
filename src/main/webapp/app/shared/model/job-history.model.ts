import { IJob } from '@/shared/model/job.model';
import { IDepartment } from '@/shared/model/department.model';
import { IEmployee } from '@/shared/model/employee.model';

export interface IJobHistory {
  id?: number;
  startDate?: Date | null;
  endDate?: Date | null;
  job?: IJob | null;
  department?: IDepartment | null;
  employee?: IEmployee | null;
}

export class JobHistory implements IJobHistory {
  constructor(
    public id?: number,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public job?: IJob | null,
    public department?: IDepartment | null,
    public employee?: IEmployee | null
  ) {}
}
