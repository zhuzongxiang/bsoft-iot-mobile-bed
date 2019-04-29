$class("ssdev.ux.portal.PortalApi",{
    popwins:{},
    popovers:{},
    tabovers:{},
    midiMods: {},
    loading: false,
    	
    openModule:function (m, param) {
        const me = this;
        if(!m.url){
            return;
        }
        var loader = m.properties && m.properties.loader;
        if(loader && loader === "desktop-vue2") {
        	m.properties.loader = $env.loader;
        }
        if(param) {
        	m._exConf = {};
        	$apply(m._exConf, param);
        }
        else if(m.hasOwnProperty("_exConf")){
        	delete m['_exConf'];
    	}
        
        var target = (m.properties && m.properties.target);

        switch(target){
            case "popover":
                me.showInPopover(m);
                break;

            case 'window':
            	me.showInWindow(m);
            	break;
            default:
            	me.showInTabPanel(m);
            	
        }

    },
    
    closeModule:function (m) {
        const me = this,mid = m.id,mod = me.popovers[mid] || me.popwins[mid];
        if(mod){
            mod.destroy();
        }
    },
    
    setModConf:function (mod,conf) {
        if($is.Function(mod.setModConf)){
            mod.setModConf(conf._exConf);
        }
    },
    
    reloadModule:function(mid, conf) {
    	const me = this;
    	var mod = me.midiMods[mid];
    	if(mod) {
    		if($is.Function(mod.setModConf)){
                mod.setModConf(conf._exConf);
            }
    	}
    	
    },
    
    showInTabPanel:function(m) {
    	var me = this, mid = m.id, vm = me.vue, tabs = me.tabs;
    	if(me.loading) {
    		return;
    	}
    	
    	if(!tabs) {
    		tabs = me.createTabPanel({
    			closable: true
    		});
    		tabs.appendTo(me.containerEl);
    		me.tabs = tabs;
    	}
    	
    	if(tabs) {
    		var tabItem = me.tabovers[mid];
    		if(!tabItem) {
    			var url = m.url;
                if(url){
                	me.loading = true;
                	
                    var loader = m.properties && m.properties.loader;
                    var modConf = {
                    	cls:url,
                        loader: loader
                    };
                    $apply(modConf, m);
                    
                    tabs.setModule(modConf).then(function (mod) {
                        me.setModConf(mod, m);
                        me.onModuleActive(m);
                        me.midiMods[mid] = mod;
                        setTimeout(function () {
                        	me.loading = false;
                        }, 500);
                        
                    }).fail(function (e) {
                        me.loading = false;
                        console.error(e);
                    });
                    
                    me.tabovers[mid] = m;
                }
    		}
    		else {
    			tabs.changeTab(m);
    			me.reloadModule(mid, m);
    		}
   		
    		if(!tabs.listenerCount("removeTab")) {
    			tabs.on("removeTab", function (id) {
                	var it = me.tabovers[id];
                	if(it) {
                		me.onModuleDeactive(it);
                		delete me.tabovers[id];
                		delete me.midiMods[id];
                	}                    
                });
    		}
    		if(!tabs.listenerCount("change")) {
    			tabs.on("change", function (currentId) {
                	var mod = me.midiMods[currentId];
                	if(mod) {
                		mod.fireEvent("tabchange");
                	}
                	for(var id in me.tabovers) {
                		var it = me.tabovers[id];
                		if(id !== currentId) {
                			me.onModuleDeactive(it);
                		}
                		else {
                			me.onModuleActive(it);
                		}
                	}
                });
    		}   
    	}    	
    },
    
    showInWindow:function(m){
        const me = this,mid = m.id;
        var win = me.popwins[mid];
        if(!win){
            const url = m.url;
            const props = m.properties;

            if(url && props){
                win = me.createWinStub({
                    title:m.name,
                    cls:url,
                    loader:props.loader,
                    width:parseInt(props.width),
                    height:parseInt(props.height),
                    modal: props.modal === "true" ? true : false,
                    fullscreen: props.maximize === "true" ? true : false,
                    modConf:m
                });
                win.on("close",function(){
                    me.onModuleDeactive(m);
                    delete me.popwins[mid];
                });
                win.on("moduleLoaded",function (mod) {
                    m.loading = false;
                    me.setModConf(mod,m);
                });
                me.popwins[mid] = win;
                win.show();
            }
        }
        else{
            if(win.module){
                m.loading = false;
            }
            win.show();
        }
        me.onModuleActive(m);
    },
    
    showInPopover:function (m) {
        var me = this,el = me.containerEl || document.body,mid = m.id;

        if(mid !== me.currentMod){
            let pr = me.popovers[me.currentMod];
            if(pr){
                me.quit(pr);
            }            
        }
        
        var popover = me.popovers[mid];
        if(!popover){
            var url = m.url;
            if(url){
                var loader = m.properties && m.properties.loader;

                popover = me.createPopover({
                	historyEnable:$env['pop.historyEnable'],                   
                    title: m.name,
                    mid:mid,
                    headerIcon: m.icon,
                    renderTo:el,
                    disableEnterAnimate:true,
                    disableLeaveAnimate:true,
                    noHeader: true,
                    closeAction: 'close'
                });

                var modConf = {
                    cls:url,
                    loader: loader
                };
                $apply(modConf, m);

                popover.setModule(modConf).then(function (mod) {
                    me.setModConf(mod,m);
                    me.onModuleActive(m);
                    me.midiMods[mid] = mod;
                   // me.bind(mod);
                }).fail(function (e) {
                    console.error(e);
                });

                popover.on("close",function () {
                    me.onModuleDeactive(m);
                    me.currentMod = null;
                    delete me.popovers[mid];
                    delete me.midiMods[mid];
                });
                popover.show();
                me.popovers[mid] = popover;
                me.currentMod = mid;
            }
        }
        else {
            popover.show();
            me.reloadModule(mid, m);
            me.onModuleActive(m);
        }
    },
    
    quit: function(popover) {
        if(popover.conf.closeAction == "hide"){
        	popover.hide();
        }
        else{
        	popover.close();
        }                                
    },
    
    createTabPanel:function(conf) {
    	
    },
    
    createWinStub:function (conf) {

    },
    
    createPopover:function (conf) {

    },
    
    onModuleActive:function (m) {
        m.activity = true;
    },
    
    onModuleDeactive:function (m) {
        m.activity = false;
    },
    
    bind:function(m) {
    	
    },
    
    destroy:function() {
        var me = this, popwins = me.popwins, popovers = me.popovers;
        var id;
        for (id in popwins) {
            var win = popwins[id];
            //win.destroy();
        }
        for (id in popovers) {
            var pop = popovers[id];
            pop.destroy();
        }
    }
});