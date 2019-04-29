$class("ssdev.ux.profile.Skin",{
    extend:"/ssdev.ux.vue.VueContainer",
    mixins:"/ssdev.utils.ServiceSupport",
    conf:{
    },
    tpl:".",
    css:".ssdev.ux.profile.css.skin",
    initComponent:function (conf) {
        var me = this;
        var evtHandlers = {
            backClick:function(){
              var profile = me.$profile;
              profile.currentMid='skinChange';
              if(profile) {
                profile.quitPop();
              }
            },
            choseSkin:function(index){
              var selectedIndex=me.data.skinSelected,selectAfter="";
                if(me.data.skinList[index].theme){
                  selectAfter=me.data.skinList[index].theme+".css";
                }
              if(selectedIndex!==index){      //选择当前不触发换肤
                me.service.saveSkin(selectAfter).then(function(data){
                    $env.globalContext.put("skinTheme","");
                    me.data.skinSelected = index;
                    me.changeSkin(data.skinName.split(".")[0]);
                    if(me.data.skinList[selectedIndex].theme){
                      $rStyleSheet("ssdev.bbp.theme."+me.data.skinList[selectedIndex].theme);//移除样式
                    }
                    me.vue.$message({
                      message:'换肤成功',
                      type:'success'
                    });
                })
              }
              },
        };
      me.setupService([{
        beanName: "bbp.skinService",
        method: ["loadCssName","saveSkin"]
      }]);
      me.evtHandlers = evtHandlers;
        me.data = {
            skinSelected:0,
            skinList:[{"color":"#2a3c57"}]
        };
        me.callParent(arguments);
    },
    afterAppend:function () {
        var me=this;
            me.service.loadCssName().then(function(data){
                if(data) {
                  for (var i = 0; i < data.length; i++) {
                    me.data.skinList.push({"color": data[i].split("-")[0], "theme": data[i].split(".")[0]});
                  }
                  if ($env.globalContext.get("skinTheme")) {
                    var selectedBefore = $env.globalContext.get("skinTheme");
                    me.data.skinList.forEach(function (val, index) {
                      if (val.theme == selectedBefore) {
                        me.data.skinSelected = index;
                      }
                    })
                  }
                }
            })
    },
    changeSkin:function(skinName){
        if(skinName) {
            $env.globalContext.put("skinTheme",skinName);
            $styleSheet("ssdev.bbp.theme." + skinName);
        }
    }
});
