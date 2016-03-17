(function($){

    $.extend(loxia.regional['zh-CN'],{
        "NPS_BUTTON_OK":"确定",
        "NPS_BUTTON_CANCEL":"取消",
        "NPS_OPERATE_SUCCESS": "操作成功",
        "NPS_OPERATE_FAILURE": "操作失败",
        "NPS_FORM_CHECK_ERROR":"错误信息",

        "NPS_SYSTEM_ERROR": "系统出错，请联系系统管理员",
        "NPS_SYSTEM_CONNECTION_ERROR":"连接系统出错，出错信息为{0}，请重试或联系系统管理员",
        "NPS_OPERATE_FORM_SUBMIT_SUCCESS": "表单{0}成功提交"
    });

    if(typeof this["nps"] === "undefined"){
        this.nps = {
            withdashboard: true,
            popup: false,
            dashboardmenu: {label:"控制板", url:"pages/Dashboard.html", icon:"home", cssClass:"menu-dashboard"},

            //prop for main window
            loadingpane: undefined,
            infoblock: undefined,
            confirmblock: undefined,
            contentblock: undefined,
            iframeblock: undefined,
            menus: undefined,

            //prop for frame window
            errorblock: undefined,

            init: function(settings, loxiasetting){
                if(loxiasetting != undefined && loxia)
                    loxia.init(loxiasetting);

                $.extend(this,settings);

                if(this.pop){
                    //do nothing
                }else{
                    if(window == window.top){
                        //init info block in main window
                        if(!this.infoblock){
                            this.infoblock = $("<div class='notice' id='msg-info'><h5></h5><p></p><span class='cross'></span></div>").
                                prependTo(".content");
                            this.confirmblock = $("<div class='notice' id='msg-confirm'><h5></h5><p></p>" +
                                "<input type='button' class='button-ok' value='" + nps.i18n("NPS_BUTTON_OK") +"'>" +
                                "<input type='button' class='button-cancel' value='" + nps.i18n("NPS_BUTTON_CANCEL") +"'>" +
                                "<span class='cross'></span></div>").
                                prependTo(".content");
                            
                            this.infoblock.data("shown",false);
                            this.confirmblock.data("shown",false);

                            $(".cross", this.infoblock).click(function(){
                                nps.info();
                            });

                            $(".cross, .button-cancel", this.confirmblock).click(function(){
                                nps.confirmblock.stop(true,true).slideUp({
                                    duration: 500,
                                    complete: function(){
                                        $(this).data("shown",false);
                                        nps.confirmblock.data("success",null);

                                    }
                                });
                            });

                            $(".button-ok", this.confirmblock).click(function(){
                                loxia.hitch(nps.confirmblock.data("success"))();
                                nps.confirmblock.stop(true,true).slideUp({
                                    duration: 500,
                                    complete: function(){
                                        $(this).data("shown",false);
                                        nps.confirmblock.data("success",null);

                                    }
                                });
                            });
                        }

                        if(!this.contentblock){
                            this.contentblock = $("body div.content:has(iframe)");
                            this.iframeblock = this.contentblock.children("iframe");
                        }

                        if(!this.loadingpane){
                            this.loadingpane = this.contentblock.find(".loading");
                            if(this.loadingpane.length == 0){
                                this.loadingpane = $("<table class='loading'><tr><td><div><h5>正在加载，请稍候...</h5><p></p></div></td></tr></table>").
                                    appendTo(".content").hide();
                            }

                            $(window).resize(function(){
                                nps.loadingpane.css({"width":$(".content").outerWidth(),"height":$(".content").outerHeight()});
                            });
                        }

                        if($("#main-menu").length > 0){
                            this.menus = $("#main-menu");
                            var m = menus;
                            if(this.withdashboard)
                                m.splice(0,0,this.dashboardmenu);
                            this.menus.npsmenu({items: m});
                            this.menus.on("menuclick", function(event, item){
                                nps.openInFrame($(item).attr("url"));
                            });
                        }

                        //init action buttons
                        $(".head-control-refresh").on("click", function(){
                            //refresh iframe
                            var frameDoc, currFrame = $(".content").find("iframe")[0];
                            if(currFrame.contentDocument)
                                frameDoc = currFrame.contentDocument;
                            else if(currFrame.contentWindow)
                                frameDoc = currFrame.contentWindow;
                            else
                                frameDoc = currFrame.document;
                            if(frameDoc)
                                frameDoc.location.reload(true);
                        });

                        $(".head-control-info").on("click", function(){
                            nps.info();
                        });

                    }else{
                        //init frame window
                        $(window).scroll(function(){
                            if(nps.errorblock){
                                var f = nps.errorblock.data("fixed");
                                if(f){
                                    //fixed style, need to change back to origin style
                                    if((f == "bottom" && $(window).scrollTop() > nps.errorblock.data("scrollTop")) ||
                                        (f == "top") && $(window).scrollTop() < nps.errorblock.data("scrollTop")){
                                        nps.errorblock.prev("div").remove();
                                        $(".error-information").removeClass("error-fixed");
                                        nps.errorblock.data("fixed",null);
                                        nps.errorblock.data("scrollTop",null);
                                    }
                                }else{
                                    //origin style, need to change to fixed style
                                    var vp = loxia.getViewport();
                                    if($(window).scrollTop() > nps.errorblock.offset().top){
                                        $(".error-information").addClass("error-fixed");
                                        nps.errorblock.data("fixed","top");
                                        nps.errorblock.data("scrollTop",$(window).scrollTop());
                                    }else if($(window).scrollTop() + vp.height < nps.errorblock.offset().top + nps.errorblock.height()){
                                        $("<div></div>").insertBefore(nps.errorblock).css({"height":nps.errorblock.height()});
                                        $(".error-information").addClass("error-fixed");
                                        nps.errorblock.data("fixed","bottom");
                                        nps.errorblock.data("scrollTop",$(window).scrollTop());
                                    }
                                }
                            }
                        });
                    }
                }
            },

            i18n: function(msg, args){
                return loxia.getLocaleMsg(msg, args);
            },

            confirm: function(title, content, success){
                if(window == window.top){
                    if(nps.confirmblock){
                        if(title){
                            nps.confirmblock.find("h5").html(title);
                            nps.confirmblock.find("p").html(content);

                            nps.confirmblock.stop(true,true).slideDown({
                                duration: 500,
                                complete: function(){
                                    $(this).data("shown",true);
                                    nps.confirmblock.data("success",success);
                                }
                            });
                        }
                    }
                }else{
                    loxia.hitch(window.parent.nps,"confirm")(title,content,success);
                }
            },

            /* Show information on top.
             * If title is not blank, info block will be shown and will be disappeared in 5 secs.
             * Otherwise it will shown/close info block.
             * info(title,message) will show message with title
             * info(message) will only show the message
             * info(null) will clear the title & message but show nothing
             * info() will toggle the info panel
             * */
            info: function(title, content){
                if(window == window.top){
                    if(nps.infoblock){
                        if(title){
                            if(content == undefined){
                                content = title;
                                title = null;
                            }
                            nps.infoblock.data("title",title);
                            nps.infoblock.data("content",content);
                            if(title){
                                nps.infoblock.find("h5").html(title).show();
                            }else{
                                nps.infoblock.find("h5").hide();
                            }
                            nps.infoblock.find("p").html(content);

                            nps._openInfo(nps.infoblock);
                            setTimeout(function(){nps._closeInfo(nps.infoblock)}, 5000);
                        }else{
                            if(title == undefined){
                                if(nps.infoblock.data("shown")){
                                    //hide it
                                    nps._closeInfo(nps.infoblock);
                                }else{
                                    //show it
                                    if(!nps.infoblock.data("content")){
                                        nps.infoblock.find("p").html("无信息");
                                    }
                                    nps._openInfo(nps.infoblock);
                                }
                            }else{
                                if(nps.infoblock.data("shown")){
                                    //hide it
                                    nps._closeInfo(nps.infoblock);
                                }
                                nps.infoblock.data("title",null);
                                nps.infoblock.data("content",null);
                                nps.infoblock.find("h5").hide();
                            }
                        }
                    }
                }else{
                    loxia.hitch(window.parent.nps,"info")(title,content);
                }
            },

            _openInfo: function(block, aftercomplete){
                var pt = parseFloat(nps.iframeblock.css("padding-top"));

                block.stop(true,true).slideDown({
                    duration: 500,
                    step: function(now, fx){
                        if(fx.prop == "height"){
                            nps.iframeblock.css({"padding-top": (pt+now) + "px"});
                        }
                    },
                    complete: function(){
                        $(this).data("shown",true);
                        if(aftercomplete) loxia.hitch(aftercomplete)();
                    }
                });
            },

            _closeInfo: function(block, aftercomplete){
                var pt = parseFloat(nps.iframeblock.css("padding-top")),
                    h = block.outerHeight();

                block.stop(true,true).slideUp({
                    duration: 500,
                    step: function(now, fx){
                        if(fx.prop == "height"){
                            nps.iframeblock.css({"padding-top": (pt-h+now) + "px"});
                        }
                    },
                    complete: function(){
                        $(this).data("shown",false);
                        if(aftercomplete) loxia.hitch(aftercomplete)();
                    }
                });
            },

            /* Write Error info to frame window.
             * If title is not set, error info will be cleared, otherwise it'll be shown.
             * errDiv is the position for the error message. If it is not sent from argument, one div.error-information will
             * be used, and if there is no such div, a new one will be inserted before div.button-line
             */
            error: function(title, content, errDiv){
                if(window == window.top){
                    //do nothing now
                }else{
                    errDiv = errDiv?$(errDiv):$("div.content-box div.error-information");
                    if(errDiv.length == 0 && !title){
                        return;
                    }else if(!title){
                        //clear error information
                        this.errorblock = undefined;
                        errDiv.hide();
                    }else{
                        //show error information
                        if(errDiv.length == 0){
                            errDiv = $("<div class='error-information'><h5></h5><p></p></div>").insertBefore(".button-line");

                        }
                        errDiv.find("h5").html(title);
                        errDiv.find("p").html(content);
                        errDiv.show();
                        $(window).scrollTop($(window).scrollTop() + errDiv.outerHeight() + 10);
                        this.errorblock = errDiv;
                    }
                }
            },

            openInFrame: function(url){
                if(window == window.top){
                    $(".content iframe").attr("src",url);
                    if(this.loadingpane){
                        var idx = this.loadingtips.length > 1 ?
                            (Math.round(Math.random()*(this.loadingtips.length*100))%this.loadingtips.length): this.loadingtips.length;
                        this.loadingpane.find("p").html(this.loadingtips[idx]);
                        this.loadingpane.css({"width":$(".content").outerWidth(),"height":$(".content").outerHeight()});
                        this.loadingpane.stop(true,true).fadeIn(300);
                        $(".content iframe").load(function(){
                            nps.loadingpane.stop(true,true).fadeOut(300);
                        });
                    }
                }else{
                    alert("Please do not use [openInFrame] in a frame window.");
                    throw ("Please do not use [openInFrame] in a frame window.");
                }
            },

            refreshMenu: function(menus){
                if(window == window.top){
                    var m = menus;
                    if(this.withdashboard)
                        m.splice(0,0,this.dashboardmenu);
                    this.menus.npsmenu("refresh",{items: m});
                }
            },

            validateForm: function(form){
                var rtn = loxia.validateForm(form);
                if(rtn.length ==0){
                    this.error();
                    return true;
                }else{
                    this.error(nps.i18n("NPS_FORM_CHECK_ERROR"),
                        rtn.join("<br/>"));
                    return false;
                }
            },

            /* Submit a form with arguments
             * arguments:
             * mode: sync/async sync means page will be jump to a new page(default)
             * lock: true/false, default is true
             * success: function, async calls after success
             * error: function, async calls if error
             */
            submitForm: function(form, args){
                var f = loxia._getForm(form),
                    mode = args.mode||"sync",
                    lock = (args.lock == undefined || args.lock)?true:false;

                if(lock){
                    loxia.lockPage();
                }

                var c = this.validateForm(f);
                if(c){
                    if(mode == "sync")
                        f.submit();
                    else{
                        loxia.asyncXhr($(f).attr("action"),f,{
                            type: $(f).attr("method")||"POST",
                            success: args.success? args.success: function(data, textStatus){
                                if(data.exception){
                                    //with exception
                                    if(data.exception.statusCode == 1){
                                        nps.info(nps.i18n("NPS_OPERATE_FAILURE"),
                                            nps.i18n("NPS_SYSTEM_ERROR"));
                                    }else{
                                        nps.info(nps.i18n("NPS_OPERATE_FAILURE"), data.exception.message);
                                    }
                                }else{
                                    if(args.successHandler)
                                        loxia.hitch(args,"successHandler")(data,textStatus);
                                    else{
                                        nps.info(nps.i18n("NPS_OPERATE_SUCCESS"),
                                            nps.i18n("NPS_OPERATE_FORM_SUBMIT_SUCCESS",[$(f).attr("name")]));
                                    }
                                }
                                loxia.unlockPage();
                            },
                            error : args.error? args.error : function(XMLHttpRequest, textStatus, errorThrown){
                                nps.info(nps.i18n("NPS_OPERATE_FAILURE"),
                                    nps.i18n("NPS_SYSTEM_CONNECTION_ERROR",[textStatus]));
                                loxia.unlockPage();
                            }
                        })
                    }
                }else{
                    loxia.unlockPage();
                }
            },

            asyncXhr: function(url, data, args){
                if(!args.success && args.successHandler){
                    args = $.extend({}, args, {
                        success: function(data,textStatus){
                            if(data.exception){
                                //with exception
                                if(data.exception.statusCode == 1){
                                    nps.info(nps.i18n("NPS_OPERATE_FAILURE"),
                                        nps.i18n("NPS_SYSTEM_ERROR"));
                                }else{
                                    nps.info(nps.i18n("NPS_OPERATE_FAILURE"), data.exception.message);
                                }
                            }else{
                                if(args.successHandler)
                                    loxia.hitch(this,"successHandler")(data,textStatus);
                                else{
                                    nps.info(nps.i18n("NPS_OPERATE_SUCCESS"),
                                        nps.i18n("NPS_OPERATE_FORM_SUBMIT_SUCCESS",[$(f).attr("name")]));
                                }
                            }
                            loxia.unlockPage();
                        }
                    });
                }
                if(!args.error){
                    args = $.extend({}, args, {
                        error: function(XMLHttpRequest, textStatus, errorThrown){
                            nps.info(nps.i18n("NPS_OPERATE_FAILURE"),
                                nps.i18n("NPS_SYSTEM_CONNECTION_ERROR",[textStatus]));
                            loxia.unlockPage();
                        }
                    });
                }
                loxia.asyncXhr(url, data, args);
            },

            asyncXhrGet: function(url, data, args){
                args = $.extend({},args,{"type":"GET"});
                this.asyncXhr(url, data, args);
            },

            asyncXhrPost: function(url, data, args){
                args = $.extend({},args,{"type":"POST"});
                this.asyncXhr(url, data, args);
            },

            syncXhr: function(url, data, args){
                var _data, options = loxia._ajaxOptions(url, data, args);
                $.extend(options,{
                    async : false,
                    success : function(data, textStatus){
                        _data = data;
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown){
                        _data["exception"] = {
                            statusCode: -1,
                            message: nps.i18n("NPS_SYSTEM_CONNECTION_ERROR",[textStatus])
                        };
                    }
                });

                $.ajax(options);
                return _data;
            },

            syncXhrGet: function(url, data, args){
                args = $.extend({},args,{"type":"GET"});
                return this.syncXhr(url, data, args);
            },

            syncXhrPost: function(url, data, args){
                args = $.extend({},args,{"type":"POST"});
                return this.syncXhr(url, data, args);
            }
        };
    }

    nps.loadingtips = [
    "如果你想刷新页面，不要按F5，试试菜单最右边的刷新按钮。",
    "忘记了刚才的提示信息么？移动鼠标到菜单最右边的按钮上，下面会出现显示信息的选项哦。",
    "系统默认超时是1个小时，超过1个小时没有操作就要重新登录啦。",
    "表格头上有小箭头的列才可以被排序。"
    ];

    var menu = {
        seth: undefined,
        origin: "",

        _create: function(){
            $.Widget.prototype._create.apply( this, arguments );
            this.element.addClass("ui-nps-menu");
            this.origin = this.element.html();
            this.refresh();
        },

        refresh: function(items){

            if(items == undefined) items = this.option("items");
            var menus = ""
            for(var i=0; i< items.length; i++){
                menus += this._buildmenu(this.option("items")[i],true);
            }
            this.element.html(menus + this.origin);

            var _this = this;
            //enter main menu
            $("dd > a, ul", this.element).mouseenter(function(e){
                e.stopPropagation();
                if(_this.seth){
                    clearTimeout(_this.seth);
                    _this.seth = undefined;
                }
                if($(this).is("dd > a")){
					var d = $(this).parent().hasClass("menu-dashboard");

					$("dl.ui-nps-menu dd > a").css("background","#0d6780").stop(true,true).animate({"padding":"10px 0 10px 0","margin":"0"},50);
					$("dl.ui-nps-menu dd.menu-dashboard > a").css("background","#ff962b");
					$(this).stop(true,true).css("background","#27a2c5").animate({"padding":"20px 0 19px 0","margin":"-10px 0 0 0"},50);
					
                    $("ul", _this.element).stop(true,true).css("display","none");
                    $(this).siblings("ul").stop(true,true).slideDown(300);
                }
            });

            //add selected style when hovering on menuitem
            $("ul li a", this.element).hover(
                function(){
                    $(this).addClass("selected");

                    $(this).parent("li").parent("ul").find("ul").css("display","none");
                    $(this).siblings("ul").css("display","none");
                    $(this).next("ul").stop(true,true).slideDown(300);
                },
                function(){
                    $(this).removeClass("selected");
                }
            );

            //make selected state right for multiple depth menu
            $("ul li ul", this.element).hover(
                function(){
                    $(this).siblings("a").addClass("selected");
                    $(this).css("display","block").parents("ul").css("display","block");
                },
                function(){
                    $(this).siblings("a").removeClass("selected");
                }
            );

            $("dd", this.element).mouseleave(function(){
                var $d = $(this);
                _this.seth = setTimeout(function(){
                    $d.find("ul").stop(true,true).fadeOut(100).find("ul").stop(true,true).fadeOut(100);
					$("dl.ui-nps-menu dd > a").css("background","#0d6780").stop(true,true).animate({"padding":"10px 0 10px 0","margin":"0"},50);
					$("dl.ui-nps-menu dd.menu-dashboard > a").css("background","#ff962b");
                },500);

            });

            $("a[url]", this.element).click(function(){
                _this.element.trigger("menuclick",[this]);
            });
        },

        _buildmenu: function(item, root){
            var m = "<a href='javascript:void(0);'";
            if(item.url)
                m += " url='" + item.url + "'";
            if(root)
                m += "><span class='icon icon-"+ item.icon +"'></span>";
            else
                m += ">";
            m += item.label +
                (!root && item.children && $.isArray(item.children) && item.children.length>0 ? "<span class='more'> > </span>" : "") +
                "</a>";

            if(root){
                return "<dd" + (item.cssClass?" class='" + item.cssClass + "'":"") + ">" + m +
                    this._buildsubmenus(item.children) + "</dd>";
            }else{
                return "<li>" + m + this._buildsubmenus(item.children) + "</li>";
            }
        },

        _buildsubmenus: function(items){
            if(!items || !$.isArray(items) || items.length ==0) return "";
            var m = "<ul>";
            for(var i=0; i< items.length; i++)
                m += this._buildmenu(items[i],false);
            return m + "</ul>";
        }
    };
    $.widget("ui.npsmenu", menu);
    $.ui.npsmenu.prototype.options = {};
})(jQuery);