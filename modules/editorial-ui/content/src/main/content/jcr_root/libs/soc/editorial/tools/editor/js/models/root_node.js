import {TreeNode} from "./tree_node.js";

export class RootNode extends TreeNode {

    get $element() {
        return this._$element;
    }

    constructor($element) {
        super($element, arguments);
        this._$element = $element;
    }
}
