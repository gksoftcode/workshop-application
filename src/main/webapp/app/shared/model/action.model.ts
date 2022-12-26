export interface IAction {
  id?: number;
  name?: string | null;
}

export class Action implements IAction {
  constructor(public id?: number, public name?: string | null) {}
}
