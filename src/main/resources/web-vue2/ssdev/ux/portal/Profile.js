$class("ssdev.ux.portal.Profile",{
    extend:"/ssdev.ux.vue.VueContainer",
    conf:{
    	avatar:"resources/images/avatars/1.jpg"
    },
    tpl:".",
    css:".ssdev.ux.portal.css.profile",
    deps:[".ssdev.ux.window.Popover"],
    popovers:{},

    initComponent:function (conf) {
        var me = this;
        var evtHandlers = {
            listMenuClick:function (e) {
                var node = e.target, link = node.closest('a');
                if(link && link.hasAttributes("data-act")){
                    let act = link.getAttribute("data-act");
                    switch(act){
                        case 'changeRoles':
                        	me.doChangeRoles();
                        	break;
                        case 'changePwd':
                        	me.doChangePwd();
                        	break;
                        case 'changeSkin':
                          me.doChangeSkin();
                          break;
                        case 'screenLock':
                        	me.doScreenLock();
                        	break;
                    }
                }
            },

            logonOut:function() {
            	me.doLogout();
            }
        };
        me.evtHandlers = evtHandlers;

        me.data = {
        	avatar: conf.avatar || me.conf.avatar,
        	manageUnitName: conf.manageUnitName,
        	userName: conf.userName,
        	userBars: [
        	    {name:'角色切换', icon:'fa fa-id-card-o', cmd:'changeRoles'},
        	    {name:'密码修改', icon:'fa fa-cog', cmd:'changePwd'},
              {name:'一键换肤', icon:'fa fa-snapchat-ghost', cmd:'changeSkin'},
        	    {name:'屏幕锁定', icon:'fa fa-unlock-alt', cmd:'screenLock'}

        	]
        };
        me.callParent(arguments);
    },

    doLogout: function() {
    	var me = this;
        return $ajax({
            url:"logon/logout",
            method:"GET"
        }).then(function(){
        	setTimeout(function(){
        		window.location.href="index.html"
        	}, 500);
        })
    },

    showInPopover:function (m) {
    	var me = this, el = me.el, mid = m.id;
    	var pop = me.popovers[mid];
    	if(!pop) {
    		var url = m.url;
    		pop = new ssdev.ux.window.Popover({
                historyEnable:false,
                //disableEnterAnimate:true,
                //disableLeaveAnimate:true,
                noHeader:true,
                title: m.name,
                mid:mid,
                closeAction: 'close',
                renderTo:el
            });
    		pop.setModule(url).then(function (mod) {
    			mod.$profile = me;
            });
    		pop.on("close",function () {
                delete me.popovers[mid];
            });

    		pop.show();
            me.popovers[mid] = pop;
            me.currentMid = mid;
    	}
    	else {
    		pop.show();
    	}

    },

    quitPop: function() {
    	var me = this, mid = me.currentMid, pop = me.popovers[mid];
    	if(pop) {
    		pop.close();
    	}
    },
    
    closeAllPop: function() {
    	var me = this;
    	for(var mid in me.popovers) {
    		var pop = me.popovers[mid];
    		if(pop) {
    			pop.close();
    		}
    	}
    },

    doChangePwd: function() {
    	var me = this;
    	var m = {
    		id:'pwdChange',
    		name:'密码修改',
    		url: ".ssdev.ux.profile.PwdChange"
    	};
    	me.showInPopover(m);
    },

    doChangeRoles: function() {
    	var me = this;
    	var m = {
    		id:'roleConvert',
    		name:'角色切换',
    		url: ".ssdev.ux.profile.RoleConvert"
    	};
    	me.showInPopover(m);
    },
    doChangeSkin: function() {
      var me = this;
      var m = {
        id:'skinChange',
        name:'一键换肤',
        url: ".ssdev.ux.profile.Skin"
      };
      me.showInPopover(m);
    },
    doScreenLock: function() {

    }
});
