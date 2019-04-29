$class("emos.portal.AppsDashboard",{
    extend:"/ssdev.ux.vue.VueContainer",
    tpl:".",
    css:".emos.portal.css.apps",
    deps:"/lib.lodash.lodash-min",
    conf:{
        pageSize:8,
        animateDirection:"next",
        tabAppId:"",
    },
    initComponent:function (conf) {
        var me = this,apps = conf.apps;
        conf.tabAppId = apps[0].id;
        me.evtHandlers = {
            tabClick:function (index) {
                var vm = this;
                var old = vm.currentApp;
                var app = vm.apps[index];
                vm.tabAppId = app.id;
                vm.currentApp = index;
                this.animateDirection =  (index > old) ? 'right' : "left";
                //this.animateDirection =  "fade"
            },
            goPrevPage:function () {
                var vm = this,currentApp = vm.currentApp,app = vm.apps[currentApp];
                if(app.pageNo > 0){
                    app.pageNo --;
                }
                vm.animateDirection = "prev";
            },
            goNextPage:function () {
                var vm = this,currentApp = vm.currentApp,app = vm.apps[currentApp],totalPages = app.totalPages;
                if(app.pageNo < totalPages - 1){
                    app.pageNo ++;
                }
                vm.animateDirection = "next";
            },
            tabAppSwitch:function (e) {
                var t = e.target.closest("a");

                var old = this.currentApp;
                var n   = parseInt(t.getAttribute("data-index"));

                this.animateDirection = "fade";

                if(n > old){
                    this.animateDirection = "right"
                }
                else{
                    this.animateDirection = "left";
                }
                this.currentApp = n;
            },
            appIconClick:function (e) {
                var vm = this,currentApp = vm.currentApp,app = vm.apps[currentApp];
                var t =  e.target.closest("a");
                var mid = t.getAttribute("data-mid");
                var mod = app.items.find(function (it) {
                    return it.id === mid;
                });
                if(!mod.hasOwnProperty("active")){
                    vm.$set(mod,"active",false);
                    vm.$set(mod,"loading",true);
                }
                else{
                    mod.loading = true;
                }
                me.fireEvent("appsIconClick",mod);
            }
        };
        me.callParent(arguments);
    },
    afterVueConfInited:function (vueConf) {
        var me = this, data = me.conf;
        data.currentApp = 0;

        vueConf.computed = {
            showTabs: function () {
                return this.apps.length > 1;
            },
            isFirstPage: function () {
                var vm = this, currentApp = vm.currentApp, app = vm.apps[currentApp];
                return app.pageNo === 0;
            },
            isLastPage: function () {
                var vm = this, currentApp = vm.currentApp, app = vm.apps[currentApp];
                return app.totalPages === app.pageNo + 1;
            },
            appPageMods: function () {
                var vm = this, currentApp = vm.currentApp, app = vm.apps[currentApp], pageSize = vm.pageSize;
                if (app.items.length <= vm.pageSize) {
                    app.totalPages = 1;
                    vm.$set(app, "pageNo", 0);
                    return app.items;
                }
                var pageMods = app.pageMods;
                if (!pageMods) {
                    app.pageMods = pageMods = _.chunk(app.items, vm.pageSize);
                    app.totalPages = pageMods.length;
                    vm.$set(app, "pageNo", 0);
                }
                return pageMods[app.pageNo];
            }
        }
    }
});