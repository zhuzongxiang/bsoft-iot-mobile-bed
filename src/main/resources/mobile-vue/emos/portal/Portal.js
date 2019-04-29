$class("emos.portal.Portal", {
    extend: "/ssdev.ux.vue.VueContainer",
    deps: "/ssdev.ux.vue.window.Popover",
    mixins:"/ssdev.ux.portal.PortalBase",
    winConf: {
        title: "Demo",
        autoShow:false
    },
    tpl:".",
    css:".emos.portal.css.portal",
    popovers:{},
    initComponent:function (conf) {
        var me = this;
        var evtHandlers = {};
        me.callParent(arguments);
    },
    afterAppend:function () {
        var me = this,el = me.el;
        me.showLogin();
    },
    createPopover:function (conf) {
        return new ssdev.ux.vue.window.Popover(conf);
    },
    showLogin:function () {
        var me = this,el = me.el,pop = me.popovers["$login"];
        if(!pop){
            var pop = me.createPopover({
                historyEnable:false,
                disableEnterAnimate:true,
                title:"DEMO",
                headerIcon:"zmdi zmdi-key",
                renderTo:el,
                noClose:true
            });
            pop.setModule(".emos.login.Login").then(function (login) {
                login.on("appsDefineLoaded",function (urt,apps) {
                    pop.hide();
                    me.afterLogin({
                        urt: urt,
                        apps:apps
                    });
                });
                login.on("relogin",function () {
                   pop.hide();
                });

                me.fireEvent("ready");
                pop.show();
            });
            me.popovers["$login"] = pop;
        }
        else{
            pop.show();
        }
    },
    afterLogin: function (data) {
        var me = this,el = me.el,ce = el.querySelector(".portal-container");

        $create(".emos.portal.Profile",data.urt).then(function (profile) {
            profile.appendTo(ce);
        }).then(function () {
            return $create(".emos.portal.AppsDashboard",{apps:data.apps});
        }).then(function (appDashboard) {
            appDashboard.on("appsIconClick",function (m) {
               if(!m.url){
                   m.loading = false;
                   return;
               }
               me.showInPopover(m);
            });
            appDashboard.appendTo(ce);
            me.appDashboard = appDashboard;
        }).fail(function(e){
            console.error(e);
        })
    }
});