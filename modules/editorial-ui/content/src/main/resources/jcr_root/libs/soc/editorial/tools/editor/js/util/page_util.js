import {SOC_DATA} from "./global_objects.js";

/**
 * Util class which intend to communicate Editor page and provide to it some required
 * functionality
 */
export class PageUtil {

    /**
     * Create shadows tree nodes to provide visual positions for editable components
     * @param node {TreeNode} from which tree will be generated
     * @return {Array} of components shadows. Each shadow allow us to see where element
     * was placed
     */
    shadowsTreeNodes(node) {
        const Z_INDEX_OFFSET = 1;
        let out = [];
        if (node.depth) {
            const isArea = node.startData.res_type === SOC_DATA.areaResourceType;
            out.push($("<div></div>")
                .attr({
                    "id": node.id,
                    "class": "shadow-component" + (isArea && " shadow-component-area" || "")
                })
                .css({
                    "z-index": Z_INDEX_OFFSET + node.depth
                }));
        }
        let i, child, children = node.children, len = children.length;
        for (i = 0; i < len; i++) {
            child = children[i];
            out = out.concat(this.shadowsTreeNodes(child));
        }
        return out;
    }
}