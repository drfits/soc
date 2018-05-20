import {SOC_DATA} from "../../util/global_objects.js";

export class Layout {

    get SOC_DATA() {
        return this._SOC_DATA;
    }

    set SOC_DATA(value) {
        this._SOC_DATA = value;
    }

    constructor() {
        this.SOC_DATA = SOC_DATA;
    }
}