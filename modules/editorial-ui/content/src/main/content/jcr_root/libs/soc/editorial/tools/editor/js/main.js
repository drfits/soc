import {EditLayout} from './models/layouts/edit_layout.js';
import {Navbar} from "./models/navbar.js";

class Editor {
    get layouts() {
        return this._layouts;
    }

    set layouts(value) {
        this._layouts = value;
    }

    constructor() {
        this.layouts = {};
        this.layouts['edit'] = new EditLayout();
        new Navbar();
    }
}

$(window).on("load", () => {
    new Editor();
});