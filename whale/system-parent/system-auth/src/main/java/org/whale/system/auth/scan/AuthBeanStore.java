package org.whale.system.auth.scan;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.whale.system.auth.domain.AuthBean;
import org.whale.system.base.Cmd;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.Strings;
import org.whale.system.dao.AuthDao;
import org.whale.system.dao.MenuDao;
import org.whale.system.dao.RoleAuthDao;
import org.whale.system.domain.Auth;
import org.whale.system.domain.Menu;
import org.whale.system.domain.RoleAuth;

/**
 * 
 * 
 * @author 王金绍
 * @date 2014年12月23日 下午10:56:48
 */
public class AuthBeanStore {

	@Autowired
	private AuthDao authDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private RoleAuthDao roleAuthDao;
	
	/**
	 * 保存权限 同时匹配权限与菜单关系
	 * 修正角色权限数据，移除已失效的权限菜单，并发送告警信息
	 * 
	 * @return 返回异常数据告警信息
	 */
	public String doStore(){
		AuthAnnotationScaner.authScan();
		
		List<Menu> menus = this.menuDao.queryAll();
		if(menus == null){
			throw new SysException("系统未配置菜单");
		}
		
		StringBuilder warn = new StringBuilder();
		Map<Class<?>, Menu> class2Menu = this.conMenuAndController(warn);
		if(class2Menu.size() < 1){
			warn.append("没有匹配到任何菜单与Controller数据，请确认菜单URL是否配置正确! \n");
		}
		
		Set<String> authCodes = AuthBean.AUTH_HOLDER.keySet();
		List<RoleAuth> roleAuths = this.roleAuthDao.queryAll();
		if(roleAuths != null){
			for(RoleAuth roleAuth : roleAuths){
				if(!authCodes.contains(roleAuth.getAuthCode())){
					warn.append("角色权限表中的 权限["+roleAuth.getAuthCode()+"]没有找到相关@Auth 定义，请确认是否要删除; \n");
				}
			}
		}
		
		List<Auth> auths = new ArrayList<Auth>(AuthBean.AUTH_HOLDER.values().size());
		Menu targetMenu = null;
		Auth auth = null;
		Auth oldAuth = null;
		for(AuthBean authBean : AuthBean.AUTH_HOLDER.values()){
			auth = new Auth();
			auth.setAuthCode(authBean.getAuthCode());
			auth.setAuthName(authBean.getAuthName());
			
			targetMenu = class2Menu.get(authBean.getController());
			if(targetMenu == null){
				oldAuth = this.authDao.get(authBean.getAuthCode());
				if(oldAuth == null){
					warn.append("权限 "+authBean.toString()+" 没有匹配到菜单; \n");
				}else{
					auth.setMenuId(oldAuth.getMenuId());
				}
			}else{
				auth.setMenuId(targetMenu.getMenuId());
			}
			auths.add(auth);
		}
		
		this.authDao.delete(Cmd.newCmd(Auth.class));
		this.authDao.saveBatch(auths);
		
		return warn.toString();
	}
	
	/**
	 * 建立菜单与Controller之间的关系
	 * 
	 * TODO 一个Controller多个菜单该怎么处理
	 * 
	 * @param strb 告警信息
	 * @return
	 */
	private Map<Class<?>, Menu> conMenuAndController(StringBuilder strb){
		Map<Class<?>, Menu> rs = new HashMap<Class<?>, Menu>();
		
		List<Menu> menus = this.menuDao.queryAll();
		if(menus == null){
			throw new SysException("系统未配置菜单");
		}
		//url Menu
		Map<String, Menu> menuMap = new HashMap<String, Menu>();
		for(Menu menu : menus){
			if(menu.getMenuType() == Menu.MENU_URL && Strings.isNotBlank(menu.getMenuUrl())){
				menuMap.put(menu.getMenuUrl(), menu);
			}
		}
		
		boolean found = false;
		Set<Class<?>> controllers = AuthBean.CONTROLLER_Set;
		Set<String> urls = null;
		for(Class<?> controller : controllers){
			found = false;
			//1.获取controller所有uri
			//2.从menuMap 中匹配菜单信息
			//3.匹配到则继续下一个匹配
			//4.匹配不到，则进入告警信息，设置controller白名单类
			urls = this.getControllerUrls(controller);
			if(urls.size() > 0){
				for(String url : urls){
					if(menuMap.get(url) != null){
						found = true;
						rs.put(controller, menuMap.get(url));
						break;
					}
				}
			}else{
				strb.append("类["+controller.getName()+"]没有找到URL;\n");
			}
			if(!found){
				strb.append("类["+controller.getName()+"]没有对应菜单;\n");
			}
		}
		return rs;
	}
	
	/**
	 * 
	 * 获取类上的@RequestMapping 值
	 * @return
	 */
	private Set<String> getControllerUrls(Class<?> controller){
		Set<String> urlSet = new HashSet<String>();
		
		RequestMapping clazzReqMapping = controller.getAnnotation(RequestMapping.class);
		String urlPrex = "";
		if(clazzReqMapping != null){
			urlPrex = clazzReqMapping.value()[0];
			if(Strings.isNotBlank(urlPrex)){
				urlPrex = urlPrex.trim();
			}
		}
		
		String url = null;
		int restUrlBeginIndex = -1;
		String[] methodUrls = null;
		RequestMapping methodReqMapping = null;
		Method[] methods = controller.getDeclaredMethods();
		for(Method method : methods){
			methodReqMapping = method.getAnnotation(RequestMapping.class);
			if(methodReqMapping != null){
				methodUrls = methodReqMapping.value();
				if(methodUrls != null && methodUrls.length > 0){
					for(String methodUrl : methodUrls){
						if(Strings.isNotBlank(methodUrl)){
							if((restUrlBeginIndex = methodUrl.indexOf("{")) != -1){
								url = urlPrex+methodUrl.substring(0, restUrlBeginIndex-1).trim();
							}else{
								url = urlPrex+methodUrl.trim();
							}
							while(url.endsWith("/")){
								url = url.substring(0, url.length()-1).trim();
							}
							
							urlSet.add(url);
						}
					}
				}
			}
		}
		
		return urlSet;
	}
	
	public static void main(String[] args) {
		String a="/sdwe/{dfwe}/";
		System.out.println(a.substring(0, a.indexOf("{")-1));
	}
}
