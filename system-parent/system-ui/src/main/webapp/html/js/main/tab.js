var tab={}
var Tab=function (param) {
    tab = this;
    this.tabArr = [];
    this.home = param.home || {"id":"homeTab","url":ctx+"/index", "name":"首页"};

    this.activeTabId = this.home.id;
    this.tabArr.push(this.home);
    this.init();
};



Tab.prototype = {
    init: function(){
        var $tab = $("<div>" +
                    "<div class='row content-tabs'>" +
                        "<button class='roll-nav roll-left'><i class='fa fa-backward'></i></button>" +
                        "<nav class='page-tabs J_menuTabs'>" +
                            "<div class='page-tabs-content' style='margin-left: 10px;' id='tabMain'>" +
                                "<a href='javascript:;' class='J_menuTab "+(this.home.active ? "active" : "")+"' id='t_"+this.home.id+"'>"+this.home.name+"</a>" +
                            "</div>" +
                        "</nav>" +
                        "<button class='roll-nav roll-right J_tabRight'><i class='fa fa-forward'></i></button>" +
                    "</div>" +
                    "<div class='row J_mainContent' id='frameMain'>" +
                        "<iframe class='J_iframe' id='f_"+this.home.id+"' width='100%' height='100%' frameborder='0' src='"+this.home.url+"' "+(this.home.active ? "" : "style='display: none;'")+" ></iframe>" +
                    "</div>" +
                "</div>");
        $("#topContent").html($tab);

        var t = this;
        $("#tabMain").on("click","a", function(){
            var rid = $(this).attr("id");
            t.actTabById(rid.substring(2));
        });
        $("#tabMain").on("click","i", function(){
            var rid = $(this).parent().attr("id");
            t.delTab(rid.substring(2));
            return false;
        });
    },

    addTab: function(param){
        var tabHere = this.getTab(param.id);
        if(tabHere == null){
            this._insertTab(param, true);
        }else{
            this.actTab(tabHere);
        }
    },

    _insertTab: function(param, show){
        $("#tabMain").append("<a class='J_menuTab' id='t_"+param.id+"'>"+param.name+" <i class='fa fa-times-circle'></i></a>");
        $("#frameMain").append("<iframe class='J_iframe' id='f_"+param.id+"' width='100%' height='100%' frameborder='0' style='display: none;'></iframe>");
        if(show){
            var preActiveTabId = this.activeTabId;
            $("#t_"+preActiveTabId).removeClass("active");
            $("#f_"+preActiveTabId).hide();
            $("#f_"+param.id).show();
            $("#t_"+param.id).addClass("active");
            this.activeTabId = param.id;
        }
        $("#f_"+param.id).attr("src", param.url);
        this.tabArr.push(param);
    },

    actTabById: function(id){
        var obj = this.getTab(id);
        if(obj != null){
            this.actTab(obj);
        }
    },

    actTab: function(param){
        var preActiveTabId = this.activeTabId;
        $("#t_"+preActiveTabId).removeClass("active");
        $("#f_"+preActiveTabId).hide();

        $("#t_"+param.id).addClass("active");
        $("#f_"+param.id).show();

        this.activeTabId = param.id;
    },

    delTab: function(id){
        if(id == this.home.id){
            return ;
        }

        for(var i=1; i<this.tabArr.length; i++) {
            if(this.tabArr[i].id == id){//找到待删除tab
                $("#t_"+id).remove();
                $("#f_"+id).remove();
                this.tabArr.splice(i, 1);
                if(this.activeTabId == id){
                    this.activeTabId = this.tabArr[i-1].id;
                    $("#t_"+this.activeTabId).addClass("active");
                    $("#f_"+this.activeTabId).show();
                }
            }
        }
    },

    getTab: function(id){
        for(var i=0; i<this.tabArr.length; i++) {
            if(this.tabArr[i].id == id){
                return this.tabArr[i];
            }
        }
        return null;
    }
}
