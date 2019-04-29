$class("ssdev.ux.home.quick.QuickPanel", {
    extend: "/ssdev.ux.vue.VueContainer",
    mixins: "/ssdev.utils.ServiceSupport",
    css: ".ssdev.ux.home.quick.quick",
    deps: ".ssdev.ux.window.WindowStub",
    tpl: ".",
    winConf:{
        title:"主页面"
    },
    initComponent: function(conf) {
    	var me = this;
    	var evtHandlers = {
    	    clickHistory: function() {
                var subPortal = me.$subPortal;
                if(subPortal) {
                    subPortal.onOpen('bsoft-iot-emos', "fu-812");        //模块跳转
                }
            },
            clickPerform: function(row) {
                var performName = 'config';
                var id = row.id;
                $create(".ssdev.ux.window.WindowStub",{
                    cls:'com.bsoft.iot.emos.ux.perform.Perform',
                    width:800,
                    height:400,
                    title:'执行',
                    modConf:{ data: id, performName: performName, row: row }
                }).then(function(stub){
                    stub.on('moduleLoaded',function (mod) {
                        mod.on('itemclick',function(node){
                            if(node.status){
                                var subPortal = me.$subPortal;
                                if(subPortal) {
                                    subPortal.onOpen('bsoft-iot-emos', "fu-812");        //模块跳转
                                }
                            }
                        })
                    })
                    stub.on('close',function(){});
                    stub.show();
                });
            }
        }
        me.on("addporal", function(){  //监听 module设置 $subPortal(二级门户对象)的事件
            var subPortal = me.$subPortal;
            var apps = me.$subPortal.conf.apps;
            for (let i = 0; i < apps.length; i++) {
                var item = apps[i];
                if (item.cd === 'bsoft-iot-emos') {
                    me.initData(item.id)
                }
            }
        });
        me.evtHandlers = evtHandlers;
        me.data = {
            id: '',
            list: [],
        }
        me.setupService([
            {
                beanName: "emos.configService",
                method: ["findByGroupByGroupName"]
            },
            {
                beanName: "bbp.paramConfig",
                method: [ "getParameter" ]
            }
        ]);
        me.callParent(arguments);
    },
    initData: function(id) {
        const me = this;
        var urt = $env.globalContext.get("urt");
        console.log(urt);
        var roleId = urt.roleId;
        var userId = urt.userId;
        me.service.getParameter('roleId', id).then(function(data) {
            var parameterDefines = data.parameterDefines;
            for (let i = 0; i < parameterDefines.length; i++) {
                var item = parameterDefines[i];
                var parameterDefineLimits = item.parameterDefineLimits[0].limitValue;
                if (roleId === parameterDefineLimits) {
                    var paramValue = item.paramValue;
                    switch (paramValue) {
                        case '1':
                            userId = userId;
                            break;
                        case '2':
                            userId = '';
                            break;
                        case '0':
                            userId = '';
                            break;
                        default:
                            userId = userId;
                            break;
                    }
                }
            }
            me.service.findByGroupByGroupName(userId).then(function(data) {
                var data = JSON.parse(data);
                if (data.status === 0) {
                    me.data.list = data.msg
                }
            })
        })
    }
});