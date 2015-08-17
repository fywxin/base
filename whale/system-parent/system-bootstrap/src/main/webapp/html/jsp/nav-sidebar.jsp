<%@page import="org.whale.system.common.constant.SysConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.whale.system.common.exception.NotLoginException"%>
<%@page import="org.whale.system.base.UserContext"%>
<%
	UserContext uc = (UserContext)request.getSession().getAttribute(SysConstant.USER_CONTEXT_KEY);
	if(uc == null){
		throw new NotLoginException("未登录");
	}
	request.setAttribute("uc", uc);

%>
		<nav class="navbar-default navbar-static-side" role="navigation">
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">
                        <div class="dropdown profile-element"> <span>
                            <img alt="image" class="img-circle" src="${html }/img/profile_small.jpg" />
                             </span>
                            <a data-toggle="dropdown" class="dropdown-toggle" href="${ctx }/main">
                                <span class="clear"> <span class="block m-t-xs"> <strong class="font-bold">${uc.userName }</strong>
                             </span>  <span class="text-muted text-xs block">${uc.realName } <b class="caret"></b></span> </span>
                            </a>
                            <ul class="dropdown-menu animated fadeInRight m-t-xs">
                                <li><a href="form_avatar.html">修改密码</a>
                                </li>
                                <li><a href="profile.html">个人资料</a>
                                </li>
                                <li class="divider"></li>
                                <li><a href="#" onclick="loginOut();">安全退出</a>
                                </li>
                            </ul>
                        </div>
                        <div class="logo-element">
                            Whale
                        </div>
                    </li>
                    ${uc.customDatas.menusStr }
                </ul>
            </div>
        </nav>