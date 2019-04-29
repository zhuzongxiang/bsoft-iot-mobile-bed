$class("emos.login.Login",{
    extend:"/ssdev.ux.vue.VueContainer",
    mixins:[
        "/ssdev.utils.ServiceSupport",
        "/ssdev.ux.login.LoginApi"
    ],
    tpl:".",
    css:".emos.login.css.login",
    initComponent:function(conf){
        var me = this;
        me.evtHandlers = {
          onLoginBtnClk:function () {
              var el = me.el;
              var fm = this.formData;
              if(!fm.uid){
                  me.uidFieldFocus();
                  return;
              }
              if(!fm.pwd){
                  me.pwdFieldFocus();
                  return;
              }
              me.doLogin(fm);
          }
        };

        var formData = me.getLocalUserData();

        formData.urts = [];

        me.data = {
            loading:false,
            formData:formData,
            errorInfo:null,
            wxVersion:$env.weixinVersion
        };
        me.callParent(arguments);
    },
    pwdFieldFocus:function(){
        var me = this,el = me.el;
        el.querySelector("input#pwd").focus();
    },
    uidFieldFocus:function (){
        var me = this,el = me.el;
        el.querySelector("input#uid").focus();
    },
    showError:function (s) {
        var me = this;
        me.data.errorInfo = s;
    }
});