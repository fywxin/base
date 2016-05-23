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
                    "<div id='tabBar' class='row content-tabs'>" +
                        "<button id='leftBut' class='roll-nav roll-left'><i class='fa fa-backward'></i></button>" +
                        "<nav  class='page-tabs J_menuTabs'>" +
                            "<div class='page-tabs-content' style='margin-left: 10px;' id='tabMain'>" +
                                "<a href='javascript:;' class='J_menuTab "+(this.home.active ? "active" : "")+"' id='t_"+this.home.id+"'>"+this.home.name+"</a>" +
                            "</div>" +
                        "</nav>" +
                        "<button id='rightBut' class='roll-nav roll-right J_tabRight'><i class='fa fa-forward'></i></button>" +
                    "</div>" +
                    "<div class='row J_mainContent' id='frameMain'>" +
                        "<iframe class='J_iframe' id='f_"+this.home.id+"' width='100%' height='100%' frameborder='0' src='"+this.home.url+"' "+(this.home.active ? "" : "style='display: none;'")+" ></iframe>" +
                    "</div>" +
                "</div>");
        $("#topContent").html($tab);
        this._bindEvent();
    },

    _bindEvent: function(){
        var t = this;
        $("#tabMain").on("click","a", function(){
            var rid = $(this).attr("id");
            t.actTabById(rid.substring(2));
        });
        $("#tabMain").on("dblclick","a", function(){
            var rid = $(this).attr("id");
            t.refreshTab(rid.substring(2));
        });
        $("#tabMain").on("click","i", function(){
            var rid = $(this).parent().attr("id");
            t.delTab(rid.substring(2));
            return false;
        });
        $("#leftBut").click(function(){
            t._moveLeft();
        });
        $("#rightBut").click(function(){
            t._moveRight();
        });
    },

    refreshTab: function(id){
        var target = $('#f_'+id);
        var url = target.attr('src');

        //显示loading提示
        var loading = layer.load();

        target.attr('src', url).load(function () {
            //关闭loading提示
            layer.close(loading);

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

        var loading = layer.load();
        $("#f_"+param.id).attr("src", param.url);

        $('.J_mainContent iframe:visible').load(function () {
            //iframe加载完成后隐藏loading提示
            layer.close(loading);
        });
        this.tabArr.push(param);
        this._move4Add();
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

        // 总宽度
        var countWidth = $("#tabBar").width() - 80;

        // 可视区域宽度
        var visibleWidth = $('#tabMain').width();

        // 移动元素的marginLeft值
        var marginLeftVal = parseInt($('#tabMain').css('margin-left'));

        //当前tab左边位置
        var curLeft = $("#t_"+param.id).offset().left;

        if(visibleWidth > countWidth){
            if((curLeft-200) < countWidth){
                $('#tabMain').animate({
                    marginLeft: '10px'
                });
            }else{
                if(curLeft > visibleWidth)    {
                    $('#tabMain').animate({
                        marginLeft: (visibleWidth - curLeft-marginLeftVal) + 'px'
                    });
                }else{

                }
            }
        }

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
        this._move4Del();
    },

    getTab: function(id){
        for(var i=0; i<this.tabArr.length; i++) {
            if(this.tabArr[i].id == id){
                return this.tabArr[i];
            }
        }
        return null;
    },

    _moveRight: function(){
        // 移动元素的marginLeft值
        var marginLeftVal = parseInt($('#tabMain').css('margin-left'));

        if (marginLeftVal + 100 >= 0) {
            $('#tabMain').animate({
                marginLeft: marginLeftVal - marginLeftVal + 'px'
            });
            return;

        }
        if ((marginLeftVal + 100) < 0) {
            $('#tabMain').animate({
                marginLeft: marginLeftVal + 100 + 'px'
            });
        }
    },

    _moveLeft: function(){
        // 总宽度
        var countWidth = $("#tabBar").width() - 80;

        // 可视区域宽度
        var visibleWidth = $('#tabMain').width();

        // 移动元素的marginLeft值
        var marginLeftVal = parseInt($('#tabMain').css('margin-left'));

        // 可视区域的宽度大于总宽度
        if (visibleWidth > countWidth) {

            // 已到左边
            if (marginLeftVal == 0) {
                $('#tabMain').animate({
                    marginLeft: marginLeftVal + (-100) + 'px'
                });
            }

            // 超过左边
            if (marginLeftVal <= 0) {
                if (visibleWidth + marginLeftVal > countWidth)
                    $('#tabMain').animate({
                        marginLeft: marginLeftVal + (-100) + 'px'
                    });
            }

        }
    },

    _move4Add: function(){
        /*var len = $("#tabBar").width()- $("#tabMain").width()-80;
         if(len < 0){
         $("#tabMain").animate({
         marginLeft:len+"px"
         });
         }*/

        // 总宽度
        var countWidth = $("#tabBar").width() - 80;

        // 可视区域宽度
        var visibleWidth = $('#tabMain').width();

        // 可视区域的宽度大于总宽度
        if (visibleWidth > countWidth) {

            // 移动元素的marginLeft值
            var marginLeftVal = parseInt($('#tabMain').css('margin-left'));
            var areaWidth = visibleWidth - countWidth
            $('#tabMain').animate({
                marginLeft: '-' + areaWidth + 'px'
            });
        }
    },

    _move4Del: function(){
        // 总宽度
        var countWidth = $("#tabBar").width() - 80;

        // 可视区域宽度
        var visibleWidth = $('#tabMain').width();

        // 移动元素的marginLeft值
        var marginLeftVal = parseInt($('#tabMain').css('margin-left'));
        if (visibleWidth > countWidth) {

            // 已到左边
            if (marginLeftVal == 0) {
                if (visibleWidth + marginLeftVal > countWidth) {
                    $('#tabMain').animate({
                        marginLeft: marginLeftVal + (-100) + 'px'
                    });
                }
                return
            }

            if (marginLeftVal + 100 > 0) {
                $('#tabMain').animate({
                    marginLeft: marginLeftVal - marginLeftVal + 'px'
                });
                return;
            }

            // 超过左边
            if (marginLeftVal < 0) {
                if (visibleWidth > countWidth) {
                    $('#tabMain').animate({
                        marginLeft: marginLeftVal + (100) + 'px'
                    });
                    return
                }

            }

        } else if (visibleWidth < countWidth) {
            if (marginLeftVal + 100 > 0) {
                $('#tabMain').animate({
                    marginLeft: marginLeftVal - marginLeftVal + 'px'
                });
                return;
            } else {
                $('#tabMain').animate({
                    marginLeft: marginLeftVal + (100) + 'px'
                });
            }
        }
    }
}
