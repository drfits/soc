import {RootNode} from "./root_node.js";

let last_id = 0;

/**
 * Global object which store commonly used data
 * @type {TreeNode}
 */
export class TreeNode {

    /**
     * DOM element id within wrapper window
     * @returns {string}
     */
    get id() {
        return this._id;
    }

    /**
     * Set node id
     * @param {string} value
     * @private
     */
    set id(value) {
        this._id = value;
    }

    /**
     * Node depth within tree structure
     * @returns {Number}
     */
    get depth() {
        return this._depth;
    }

    /**
     *
     * @param {Number} value
     */
    set depth(value) {
        this._depth = value;
    }

    /**
     * Get parent node
     * @returns {TreeNode}
     */
    get parent() {
        return this._parent;
    }

    /**
     * Set parent node
     * @param {TreeNode}value
     */
    set parent(value) {
        this._parent = value;
    }

    /**
     * Size of shadow on shadow panel
     * @returns {*}
     */
    get shadowPosition() {
        return this._shadowPosition;
    }

    /**
     * Set size of shadow on shadow panel
     * @param value
     */
    set shadowPosition(value) {
        this._shadowPosition = value;
    }

    /**
     * A set of child components just for navigation within components tree
     * @returns {TreeNode[]}
     */
    get children() {
        return this._children;
    }

    /**
     * Set children for specified node
     * @param {TreeNode[]} value
     * @private
     */
    set children(value) {
        this._children = value;
    }

    /**
     * Get child element by specified ID value
     * @param id for search
     * @returns {TreeNode}
     */
    getChildById(id) {
        let childNodes = [].concat(this.children);
        while (childNodes.length) {
            const node = childNodes.pop();
            if (node.id === id) {
                return node;
            } else {
                childNodes = childNodes.concat(node.children);
            }
        }
        return undefined;
    };

    /**
     * Retrieve parent area component if exists, {undefined} otherwise
     * @returns {TreeNode|undefined}
     */
    parentArea() {
        let areaNode;
        let parentArea = this.parent;
        while (parentArea) {
            if (parentArea instanceof RootNode) {
                break;
            }
            if (!parentArea.isComponent) {
                areaNode = parentArea;
                break;
            }
            parentArea = parentArea.parent;
        }
        return areaNode;
    }

    /**
     * Add child element
     * @param child
     */
    add(child) {
        this.children.push(child);
    };


    /**
     * Get component start data
     * @returns {StartData} component JSON data representation
     *
     * JSON data object which contains component data like dialog path, resource path and so on
     * @typedef {Object} StartData
     * @property {String} title The current component title for user experience
     * @property {String} path The current component absolute JCR path
     * @property {String} dlg The current component absolute JCR path to dialog
     * @property {String} res_type The current component "sling:resourceType" property
     * @property {String} type is a property which indicate which dialog type should be using for specified component (meanwhile area or component)
     * @returns {StartData}
     */
    get startData() {
        return this._startData;
    }

    /**
     * Set JSON data object which contains component data like dialog path, resource path and so on
     * @param {StartData}value
     * @private
     */
    set startData(value) {
        this._startData = value;
    }

    get endData() {
        return this._endData;
    }

    set endData(value) {
        this._endData = value;
    }

    /**
     * DOM Comment which indicate start of component
     * @returns {HTMLElement}
     */
    get startEl() {
        return this._startEl;
    }

    /**
     * Set start element
     * @param {HTMLElement} value
     * @private
     */
    set startEl(value) {
        this._startEl = value;
    }

    get endEl() {
        return this._endEl;
    }

    /**
     * Set end element for this component marker
     * @param value
     */
    set endEl(value) {
        this._endEl = value;
    }

    /**
     * Add all children elements to current object
     * @param children for add
     */
    addAll(children) {
        this.children.concat(children);
    };

    constructor(startEl, startData) {
        this.id = "soc_" + (++last_id);
        this.startEl = startEl;
        this.startData = startData;
        this.children = [];
    }

    /**
     * Check is this shadow belongs to component
     * @return {boolean}
     */
    get isComponent() {
        return this.startData.type === "component";
    }
}