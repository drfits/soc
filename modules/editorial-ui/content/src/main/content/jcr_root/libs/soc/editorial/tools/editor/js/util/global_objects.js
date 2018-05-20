/**
 * Global constants for application
 */
/**
 * Global object which store commonly used data
 * @type {SOC_DATA}
 *
 * @typedef {Object} SOC_DATA
 * @property {TreeNode} selectedEl The element on shadow panel which was currently selected
 * @property {RootNode} rootNode The root node of shadows tree
 * @property {String} areaResourceType The resource type of area components
 * @property {jQuery} eventBus for editor page
 */
export class SOC_DATA {
    static get selectedEl() {
        return this._selectedEl;
    }

    static set selectedEl(value) {
        this._selectedEl = value;
    }

    static get rootNode() {
        return this._rootNode;
    }

    static set rootNode(value) {
        this._rootNode = value;
    }

    static get eventBus() {
        if (!this._eventBus) {
            this._eventBus = $(window);
        }
        return this._eventBus;
    }
}