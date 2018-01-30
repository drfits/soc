import {SOC_DATA} from "../util/global_objects.js";

/**
 * Base class for navigation bar and actions.
 * @type {EditLayout}
 */
export class Navbar {

    constructor() {
        console.debug("Initialize Navbar class");

        /* Set event handlers */
        $("nav")
            .on("click", ".page-layouts-editor", () => {
                SOC_DATA.eventBus.trigger("soc.reload.shadows-panel");
            })
            .on("click", ".page-layouts-preview", () => {
                SOC_DATA.eventBus.trigger("soc.delete.shadows-panel");
            });
    }
}
