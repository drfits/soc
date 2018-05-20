import {MirrorUtil} from "../../util/mirror_util.js";
import {PageUtil} from "../../util/page_util.js";
import {ShadowComponentMenu} from "../../components/menu/shadow_component_menu.js";
import {Layout} from "./layout.js";

/**
 * Base class for editor layout.
 * @type {EditLayout}
 */
export class EditLayout extends Layout {

    constructor() {
        super();
        console.debug("Initialize Editor class");

        const $MIRROR = $(".soc-page-mirror");
        const $SHADOWS_PANEL = $(".soc-shadows-panel");
        const $initShadowsRef = this.initShadows;

        // Listeners
        const _SOC_DATA = super.SOC_DATA;

        _SOC_DATA.eventBus.on("soc.delete.shadows-panel", () => {
            console.debug("Delete shadows-panel");
            // Wait before render due to some delay between insert html and browser render
            setTimeout(() => {
                $SHADOWS_PANEL.children(".shadow-component").remove();
                ShadowComponentMenu.hideToolbar();
            }, 500);
        });

        _SOC_DATA.eventBus.on("soc.reload.shadows-panel", () => {
            console.debug("Reload shadows-panel");
            // Wait before render due to some delay between insert html and browser render
            setTimeout(() => {
                $SHADOWS_PANEL.children(".shadow-component").remove();
                $initShadowsRef($MIRROR, $SHADOWS_PANEL, _SOC_DATA);
                ShadowComponentMenu.hideToolbar();
            }, 500);
        });

        _SOC_DATA.eventBus.on("soc.reload.component", (event, data) => {
            console.debug("Update component: %s", data.componentId);
            const shadow = _SOC_DATA.rootNode.getChildById(data.componentId);
            if (shadow) {
                new MirrorUtil().updateComponent(shadow);
                setTimeout(() => {
                    _SOC_DATA.eventBus.trigger("soc.reload.shadows-panel");
                }, 500);
            }
        });

        _SOC_DATA.eventBus.on("soc.reload.mirror", () => {
            console.debug("Reload page");
            $MIRROR.attr("src", $MIRROR.attr("src"));
            _SOC_DATA.eventBus.trigger("soc.reload.shadows-panel");
        });

        _SOC_DATA.eventBus.on("soc.delete.component", (event, data) => {
            console.debug("Delete component: %s", data.componentId);
            const shadow = _SOC_DATA.rootNode.getChildById(data.componentId);
            if (shadow) {
                new MirrorUtil().deleteComponent(shadow);
                setTimeout(() => {
                    _SOC_DATA.eventBus.trigger("soc.reload.shadows-panel");
                }, 500);
            }
        });
    }

    /**
     * Function for initialize shadows panel and additional listeners
     * @param $MIRROR
     * @param $SHADOWS_PANEL
     * @param _SOC_DATA - data object of this class
     */
    initShadows($MIRROR, $SHADOWS_PANEL, _SOC_DATA) {
        const $mirror_content = $($MIRROR.contents());
        console.debug(
            "Mirror is loaded: width[%s], height[%s]",
            $mirror_content.width(),
            $mirror_content.height()
        );

        // Set width and height for proper position
        $MIRROR.height($mirror_content.height());
        $SHADOWS_PANEL.height($mirror_content.height());

        // Calculate shadows and set correct positions
        const mirrorUtil = new MirrorUtil();
        _SOC_DATA.rootNode = mirrorUtil.getPageComponents($mirror_content.find("body"));
        const pageUtil = new PageUtil();
        pageUtil.shadowsTreeNodes(_SOC_DATA.rootNode).forEach((el) => {
            $SHADOWS_PANEL.append(el);
        });

        const HOVER_BORDER_SIZE = 2; // in px
        mirrorUtil
            .getShadows(_SOC_DATA.rootNode.children)
            .forEach((el) => {
                console.debug("Set position of: %s", el[4]);
                const shadow = $("#" + el[4]);
                if (shadow) {
                    shadow.css({
                        "top": el[0],
                        "left": el[1] + HOVER_BORDER_SIZE,
                        "width": el[2] - HOVER_BORDER_SIZE * 4,
                        "height": el[3] - HOVER_BORDER_SIZE * 2
                    });
                }
            });

        const _menu = new ShadowComponentMenu();

        $SHADOWS_PANEL.on("click", ".shadow-component", (e) => {
            const id = $(e.target).attr("id");
            if (id) {
                const selectedShadow = _SOC_DATA.rootNode.getChildById(id);
                if (selectedShadow) {
                    _SOC_DATA.selectedEl = selectedShadow;
                }
            }
            _menu.showToolbar(e.target);
        });
        $SHADOWS_PANEL.show();
    }

}
