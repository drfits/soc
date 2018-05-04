import {EditComponentDialog} from "./edit_dialog.js";
import {SOC_DATA} from "../../util/global_objects.js";

/**
 * Menu component which create menu for specified component
 */
export class ShadowComponentMenu {
    /**
     * Place menu toolbar on specified shadow element
     * @param el
     */
    showToolbar(el) {
        const $selectedEl = $(el);
        $selectedEl.siblings(".selected").removeClass("selected");
        const $toolbar = $("#soc-component-toolbar");
        $toolbar.show();
        $selectedEl.addClass("selected");
        $toolbar.position({
            my: "left bottom",
            at: "left top",
            of: $selectedEl,
            collision: "fit"
        });
        $toolbar.css("left", $selectedEl.offset().left);
        const shadowNode = SOC_DATA.rootNode.getChildById($selectedEl.attr("id"));
        this.setActionButtons($toolbar, shadowNode);
    }

    static hideToolbar() {
        $(".soc-shadows-panel").children(".selected").removeClass("selected");
        const $toolbar = $("#soc-component-toolbar");
        $toolbar.hide();
        $toolbar.children().remove();
    }

    /**
     * Create toolbar button
     * @param title for button
     * @param classValue for button
     * @param shadowNode to obtain some pies of data
     * @param fn to execute on click event
     * @return {JQuery<TElement extends Node> | jQuery | HTMLElement | string | any}
     */
    static toolbarButton(title, classValue, shadowNode, fn) {
        const $btn = $("<button></button>").attr("title", title);
        $btn.append($("<i></i>").attr("class", classValue));
        if (shadowNode) {
            $btn.data("id", shadowNode.id);
            fn && $btn.click(fn);
        }
        return $btn;
    }

    /**
     * Set buttons according to shadow element data
     * @param $toolbar where buttons have to be added
     * @param shadowNode {TreeNode}
     * @return {*}
     */
    setActionButtons($toolbar, shadowNode) {
        $toolbar.children().remove();
        $("<button>" + shadowNode.startData.title + "</button>").appendTo($toolbar);
        if (shadowNode.startData.dlg) {
            $toolbar.append(ShadowComponentMenu.toolbarButton("Edit", "fa fa-pencil", shadowNode, this.editBtnAction));
        }
        $toolbar.append(ShadowComponentMenu.toolbarButton("Delete", "fa fa-trash-o", shadowNode, this.deleteBtnAction));
        const parentArea = shadowNode.parentArea();
        if (parentArea) {
            $toolbar.append(ShadowComponentMenu.toolbarButton("Parent area", "fa fa-level-up", parentArea, this.parentBtnAction));
        }
        $toolbar.append(
            ShadowComponentMenu.toolbarButton(
                "Close menu",
                "fa fa-window-close-o",
                shadowNode,
                ShadowComponentMenu.closeBtnAction
            )
        );
        return $toolbar;
    }

    editBtnAction() {
        console.debug("Edit button clicked: %s", $(this).data('id'));
        const treeNode = SOC_DATA.rootNode.getChildById($(this).data('id'));
        treeNode && new EditComponentDialog(treeNode).show();
    }

    deleteBtnAction() {
        const id = $(this).data('id');
        if (id) {
            console.debug("Edit button clicked: %s", id);
            SOC_DATA.eventBus.trigger("soc.delete.component", {componentId: id});
        }
    }

    parentBtnAction() {
        console.debug("Parent button clicked: %s", $(this).data('id'));
        const shadowArea = $("#" + $(this).data('id'));
        shadowArea && $(shadowArea).click();
    }

    static closeBtnAction(e) {
        SOC_DATA.selectedEl = undefined;
        ShadowComponentMenu.hideToolbar();
    }
}