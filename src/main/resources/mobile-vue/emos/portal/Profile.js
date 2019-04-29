$class("emos.portal.Profile",{
    extend:"/ssdev.ux.vue.VueContainer",
    conf:{
        avatar:$env.resourcesHome + "images/avatars/1.jpg"
    },
    tpl:".",
    css:".emos.portal.css.profile",
    initComponent:function (conf) {
        var me = this;
        var evtHandlers = {
            dropMenuClick:function (e) {
                var node = e.target;
                if(node.tagName.toLowerCase() == "a" && node.hasAttributes("data-act")){
                    let act = node.getAttribute("data-act");
                }
            }
        };
        me.evtHandlers = evtHandlers;
        me.callParent(arguments);
    }
});