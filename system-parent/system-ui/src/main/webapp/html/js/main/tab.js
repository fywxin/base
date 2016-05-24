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
            t.actTab(rid.substring(2));
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
            this.actTab(param.id);
        }
    },

    _insertTab: function(param, show){

        $("#tabMain").append("<a class='J_menuTab' id='t_"+param.id+"'>"+param.name+" <i class='fa fa-times-circle'></i></a>");
        $("#frameMain").append("<iframe class='J_iframe' id='f_"+param.id+"' width='100%' height='"+($.h()-65)+"px' frameborder='0' style='display: none;'></iframe>");

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

        $("#f_"+param.id).load(function () {
            layer.close(loading);
        });
        this.tabArr.push(param);
        this._move4Add();
    },

    actTab: function(id){
        var preActiveTabId = this.activeTabId;
        $("#t_"+preActiveTabId).removeClass("active");
        $("#f_"+preActiveTabId).hide();

        $("#t_"+id).addClass("active");
        $("#f_"+id).show();
        this.activeTabId = id;

        // 总宽度
        var countWidth = $("#tabBar").width() - 80;

        // 可视区域宽度
        var visibleWidth = $('#tabMain').width();

        // 移动元素的marginLeft值
        var marginLeftVal = parseInt($('#tabMain').css('margin-left'));

        //当前tab左边位置
        var curLeft = $("#t_"+id).offset().left - 200;

        //当前激活tab左边位置
        var activeLeft = $("#t_"+preActiveTabId).offset().left - 200;

        if(visibleWidth > countWidth){
            console.info("curLeft="+curLeft+", activeLeft="+activeLeft+", countWidth="+countWidth+", visibleWidth="+visibleWidth+", marginLeftVal="+marginLeftVal);
            var visiblePosition = curLeft+marginLeftVal;

            console.info("visiblePosition="+visiblePosition);
            if((curLeft>0 && curLeft < countWidth) || (visiblePosition > 0 && visiblePosition < countWidth)){//可视区域 ,不移动
                if((curLeft>0 && curLeft < countWidth)){
                    console.info("可视区域curLeft:"+curLeft);
                }else{
                    console.info("可视区域visiblePosition:"+visiblePosition);
                }

            }else{
                var distance = activeLeft - curLeft;

                if(distance > 0){//左边
                    var pos = distance+marginLeftVal;
                    if(-pos<distance){
                        $('#tabMain').animate({
                            marginLeft: '10px'
                        });
                    }else{
                        $('#tabMain').animate({
                            marginLeft: pos+'px'
                        });
                    }
                }else{//右边
                    var pos = distance+marginLeftVal;
                    $('#tabMain').animate({
                        marginLeft: pos+'px'
                    });
                }
            }
        }
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