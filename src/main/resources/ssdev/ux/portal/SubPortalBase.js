$class("ssdev.ux.portal.SubPortalBase",{
    mixins:"/ssdev.ux.portal.PortalApi",
    
    constructor:function (conf) {
    	var me = this;
    	me.popwins = {};
        me.popovers = {};
        me.tabovers = {};
        me.midiMods = {};
    },
    
    afterInitComponent:function () {
        var me = this;
        me.on("collapse", me.doCollapse);
    },
    
    doOpenModule:function(mod, topid, param) {
    	const me = this, conf = me.conf, currentAppId = conf.appId, topPortal = me.$portal;
    	if(mod) {
    		me.initModuleActive(mod);
        	if(!topid || currentAppId == topid) {
        		me.openModule(mod, param);
        	}
        	else {
        		if(topPortal) {
        			topPortal.onAppClick(topid, mod, param);
        		}
        	}
    	}    	   	
    },
    
    onOpen:function(appid, mid, param) {
    	var me = this, topPortal = me.$portal, nodes = me.conf.apps, mod;
    	if(topPortal) {
    		var app = topPortal.findApp(appid);
    		if(app) {
    			appid = app.id;
    		}
    		mod = topPortal.findModule(appid, mid);
    	}
    	else {
    		appid = "";
    		mod = me.loop(nodes, mid);
    	}    	
    	me.doOpenModule(mod, appid, param);
    },
    
    doCollapse:function() {
    	
    },

    setModConf:function (mod, conf) {    //把 subPortal 对象传给 每个打开的 module
    	const me = this;
    	mod.$subPortal = me;
    	mod.fireEvent("addporal");
    	me.callParent(arguments);
    },
    
    getTopPortal:function() {
    	const me = this;
    	return me.$portal;
    },

    initModuleActive:function(mod) {
    	
    },
    
    destroy:function(){
    	var me = this, el = me.el, tabs = me.tabs;
    	if(tabs) {
    		tabs.removeAll();
    	}

    	for (var mid in me.popovers) {
            var pop = me.popovers[mid];
            if(pop){
                me.quit(pop);
            }
        }
    },
    
    loop: function(items, mid) {
    	var me = this, mod;
    	if(items) {
    		for(var i=0; i<items.length; i++) {
    			var it = items[i];
    			if(it.id === mid || it.cd === mid) {
    				mod = it;
    			}
    			else{
    				mod = me.loop(it.items, mid);
    			}
    			if(mod) {
    				return mod;
    			}
    		}
    	}
    	return "";
    }
    
});