import {SOC_DATA} from "../../util/global_objects.js";

/**
 * Property which hold dialog type. Requires to select which submit logic should be used.
 * @type {string}
 */
const DIALOG_TYPE = 'type';

/**
 * Dialog for edit component data. This dialog have to be instantiated from {TreeNode}
 * Send events:
 * 1. name - "soc.reload.component", parameters - {componentId}
 * 2. name - "soc.reload.mirror"
 */
export class EditComponentDialog {

    /**
     * Edit dialog will be instantiated for specified node
     * @param treeNode {TreeNode}
     */
    constructor(treeNode) {
        this.treeNode = treeNode;
    }

    /**
     * Show dialog
     */
    show() {
        const dlgPath = this.treeNode.startData.dlg;
        if (!this.treeNode.startData.dlg) {
            console.debug("Property 'dlg' doesn't exists");
            return;
        }
        const $dlg = $(".soc-shadow-dialog-form");
        $dlg.empty();
        $dlg.load(dlgPath + ".html", (response, status) => {
            if (status !== "error") {
                const $dlgForm = $($dlg.find("form"));
                $dlgForm.attr("action", this.treeNode.startData.path);
                $dlgForm.data(DIALOG_TYPE, this.treeNode.startData.type);
                const $dialog = $dlgForm.parents(".soc-component-edit-modal");
                if ($dialog) {
                    $dialog.on('show.bs.modal', (e) => {
                        if ($dlgForm.data(DIALOG_TYPE) === 'component') {
                            $dlgForm.submit(this.submitComponentDialog);
                        } else if ($dlgForm.data(DIALOG_TYPE) === 'area') {
                            $dlgForm.submit(this.submitAreaDialog);
                        }
                    });
                    $dialog.modal("show");
                }
            }
        });
    }

    /**
     * Submit operation for component dialog. Allow to update component properties
     * @param e - event
     */
    submitComponentDialog(e) {
        // Stop form from submitting normally
        e.preventDefault();
        const $form = $(this);
        const $modal = $form.closest(".soc-component-edit-modal");
        // Get some values from elements on the page:
        const url = $form.attr("action");
        $.ajax({
            type: "POST",
            cache: false,
            url: url,
            data: $form.serialize(),
            success: () => {
                switch ($modal.data("dlg-config-reload")) {
                    case "container":
                        SOC_DATA.eventBus.trigger("soc.reload.component", {"componentId": SOC_DATA.selectedEl.id});
                        break;
                    case "page":
                        SOC_DATA.eventBus.trigger("soc.reload.mirror");
                        break;
                    default:
                        break;
                }
                $modal.modal("hide");
            },
            error: (jqXHR) => {
                $modal.find(".soc-modal-alert-position").empty().append(
                    $("<div></div>").attr({
                        class: "alert alert-danger",
                        role: "alert"
                    }).text(jqXHR.responseText)
                );
            }
        });
    }

    /**
     * Submit operation for area dialog. Allow to create component based on selected value
     * @param e - event
     */
    submitAreaDialog(e) {
        // Stop form from submitting normally
        e.preventDefault();
        // Get some values from elements on the page:
        const $form = $(this);
        const $modal = $form.closest(".soc-component-edit-modal");
        // Get some values from elements on the page:
        const url = $form.attr("action");
        const option = $form.find('.available-components').children("option").filter(":selected");
        $.ajax({
            type: "POST",
            cache: false,
            url: url,
            data: {
                ":operation": "import",
                ":contentType": "json",
                ":nameHint": option.text(),
                ":content": JSON.stringify({
                    "jcr:primaryType": "nt:unstructured",
                    "sling:resourceType": option.val()
                })
            },
            success: (msg) => {
                switch ($modal.data("dlg-config-reload")) {
                    case "container":
                        SOC_DATA.eventBus.trigger("soc.reload.component", {"componentId": SOC_DATA.selectedEl.id});
                        break;
                    case "page":
                        SOC_DATA.eventBus.trigger("soc.reload.mirror");
                        break;
                    default:
                        break;
                }
                $modal.modal("hide");
            },
            error: (jqXHR) => {
                $modal.find(".soc-modal-alert-position").empty().append(
                    $("<div></div>").attr({
                        class: "alert alert-danger",
                        role: "alert"
                    }).text(jqXHR.responseText)
                );
            }
        });
    }
}
