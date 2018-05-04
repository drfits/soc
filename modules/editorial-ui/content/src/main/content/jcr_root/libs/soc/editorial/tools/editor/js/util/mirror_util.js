import {RootNode} from "../models/root_node.js";
import {TreeNode} from "../models/tree_node.js";

/**
 * Util class which intend to communicate with Mirror iframe
 */
export class MirrorUtil {

    /**
     * Method collect page components which are placed within comments.
     * Start and end comments must be at the same tree level otherwise we cannot obtain correct data
     * <!--{soc_start:true, id:"resource/type/of/component"}--> - this is begin of component comment
     * <!--{soc_start:false, id:"resource/type/of/component"}--> - this is end of component comment
     * Where:
     * soc_start - is a start marker with value of component resource path
     * soc_end - is a end marker with value of component resource path
     * dlg - relative path to component dialog form
     * res_type - is a resource type
     * @param selector on body element where to find SOC markup
     * @returns {RootNode}
     */
    getPageComponents(selector) {
        const $selector = $(selector);
        if (!$selector.length) {
            return undefined;
        }
        let rootNode = new RootNode($selector);
        rootNode = findComponentsFn(rootNode.$element, rootNode, []);

        /**
         * List of components node
         * @param rootEl{Object} is an html tag where to search child nodes
         * @param rootNode {TreeNode} root node for store child items
         * @param stack {[TreeNode]} to hold tags
         * @returns {TreeNode}
         */
        function findComponentsFn(rootEl, rootNode, stack) {
            rootNode && stack.push(rootNode);
            $(rootEl)
                .contents()
                .each((i, el) => {
                    if (el.nodeType === Node.COMMENT_NODE) {
                        const markerData = getMarkerData(el);
                        if (markerData) {
                            console.debug("Check node for: %s", markerData["path"]);
                            if (markerData["res_type"]) {
                                console.debug("Open node for: %s", markerData["path"]);
                                const startNode = new TreeNode(el, markerData);
                                const stackLength = stack.push(startNode);
                                stack[stackLength - 2].add(startNode);
                            } else {
                                console.debug("Close node for: %s", markerData["path"]);
                                const node = stack.pop();
                                console.debug("Close node id: %s", node.id);
                                node.endEl = el;
                                node.endData = markerData;
                            }
                        }
                    } else {
                        findComponentsFn(el, undefined, stack);
                    }
                });
            return rootNode;
        }

        /**
         * Get component marker JSON data
         * @param el with data
         * @returns {*} JSON data if this is a SOC component marker, {undefined} otherwise
         */
        function getMarkerData(el) {
            try {
                if (el.data.substring(0, 3) === "soc") {
                    return JSON.parse(el.data.substring(3)) || {};
                }
            } catch (e) {
                console.debug("Not a component comment");
            }
            return undefined;
        }

        /**
         * Set depth value on every node
         * @param node {TreeNode} to process
         * @param depth {Number} of this node within tree structure
         */
        function setNodeDepthAndParents(node, depth) {
            node.depth = depth;
            node.children.forEach((childNode) => {
                childNode.parent = node;
                setNodeDepthAndParents(childNode, depth + 1);
            })
        }

        setNodeDepthAndParents(rootNode, 0);
        // Return the jQuery comments collection.
        return rootNode;
    }

    /**
     * Find square which we could place under this component
     * @param children{TreeNode[]} - for which we should find squares
     * @return {Array} of [ [top, left, width, height, component_id], [top, left, width, height, component_id] ... ]
     */
    getShadows(children) {
        const squares = [];
        let listOfChilds = [].concat(children);
        while (listOfChilds.length) {
            const component = listOfChilds.pop();
            listOfChilds = listOfChilds.concat(component.children);

            /**
             * Matrix for store future wrapper box [left, top, width + left, height + top]
             * @type {Array}
             */
            const elMatrix = [];

            window.console.debug("Inner elements for: " + component.id);
            // Create list of squares positions [top, left, width, heigh]
            const componentElements = $(component.startEl).parent().contents().toArray().reverse();
            let belongsToComponent = false;
            while (componentElements.length) {
                const siblingEl = componentElements.pop();
                if (component.startEl === siblingEl) {
                    belongsToComponent = true;
                }
                if (component.endEl === siblingEl) {
                    break;
                }
                if (belongsToComponent && siblingEl.nodeType === Node.ELEMENT_NODE) {
                    const $el = $(siblingEl);
                    const offset = $el.offset();
                    const matrix = [offset.top, offset.left, offset.left + $el.innerWidth(), offset.top + $el.innerHeight()];
                    elMatrix.push(matrix);
                    console.debug("Shadow size: " + matrix);
                }
            }
            // Compute shadow coordinates
            let componentWrapperMatrix = [
                _.min(elMatrix, (el) => {
                    return el[0];
                })[0],
                _.min(elMatrix, (el) => {
                    return el[1];
                })[1],
                _.max(elMatrix, (el) => {
                    return el[2];
                })[2],
                _.max(elMatrix, (el) => {
                    return el[3];
                })[3]
            ];
            componentWrapperMatrix[2] = componentWrapperMatrix[2] - componentWrapperMatrix[1];
            componentWrapperMatrix[3] = componentWrapperMatrix[3] - componentWrapperMatrix[0];
            componentWrapperMatrix.push(component.id);
            console.debug("Calculated shadow: %s", componentWrapperMatrix);
            squares.push(componentWrapperMatrix);
        }
        return squares;
    }

    /**
     * Update component which associated with specified shadow
     * @param shadow{TreeNode} for update
     */
    updateComponent(shadow) {
        if (shadow.startData.path) {
            const resPath = shadow.startData.path + ".html";
            $.ajax({
                type: "GET",
                cache: false,
                url: resPath,
                success: (html) => {
                    let remove = false;
                    const $startEl = $(shadow.startEl);
                    $startEl.parent()
                        .contents()
                        .each((i, el) => {
                            if (shadow.startEl === el) {
                                remove = true;
                            } else if (shadow.endEl === el) {
                                $(shadow.endEl).remove();
                                remove = false;
                            } else {
                                remove && $(el).remove();
                            }
                        });
                    $startEl.replaceWith(html);
                }
            })
        }
    }

    /**
     * Delete component from DOM tree
     * @param shadow{TreeNode} for delete associated component
     */
    deleteComponent(shadow) {
        if (shadow.startData.path) {
            const resPath = shadow.startData.path;
            $.ajax({
                type: "POST",
                cache: false,
                url: resPath,
                data: {
                    ":operation": "delete"
                },
                success: () => {
                    let remove = false;
                    const $startEl = $(shadow.startEl);
                    $startEl.parent()
                        .contents()
                        .each((i, el) => {
                            if (shadow.startEl === el) {
                                $(shadow.startEl).remove();
                                remove = true;
                            } else if (shadow.endEl === el) {
                                $(shadow.endEl).remove();
                                remove = false;
                            } else {
                                remove && $(el).remove();
                            }
                        });
                }
            });
        }
    }
}