package ningbo.media.core.web.interceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ningbo.media.core.web.include.HttpInclude;
import ningbo.media.core.web.scope.Flash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 拦截器,用于存放渲染视图时需要的的共享变量
 * 
 * @author badqiu
 * 
 */
public class SharedRenderVariableInterceptor extends HandlerInterceptorAdapter
		implements InitializingBean {

	static Logger logger = LoggerFactory
			.getLogger(SharedRenderVariableInterceptor.class);

	private Map<String, Object> globalVariables = new HashMap<String, Object>();

	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.info(handler.getClass().getName()) ;
		System.out.println(handler.getClass().getName());
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.info("Please Attation,This will be saved any share vars by view in here.");
		if (null == modelAndView) {
			return;
		}

		String viewName = modelAndView.getViewName();
		if (null != viewName && !viewName.startsWith("redirect:")) {
			modelAndView.addAllObjects(globalVariables);
			modelAndView.addAllObjects(perRequest(request, response));
		}
		//super.postHandle(request, response, handler, modelAndView);
	}

	protected Map<String, Object> perRequest(HttpServletRequest request,
			HttpServletResponse response) {
		HashMap<String, Object> model = new HashMap<String, Object>();

		model.put("share_current_request_time", new Date());
		model.put("share_current_login_username", "admin");
		model.put("ctx", request.getContextPath());
		model.put("flash", Flash.current().getData());
		// 为freemarker,velocity提供<jsp:include
		// page="/some/page.jsp"/>功能,使用${httpInclude.include("/servlet/header.do")};
		// model.put("httpInclude", new HttpInclude(request, response));
		model.put("httpInclude", new HttpInclude(request, response));
		return model;
	}

	/** 用于初始化 sharedRenderVariables, 全局共享变量请尽量用global前缀 */
	private void initSharedRenderVariables() {
		globalVariables.put("global_system_start_time", new Date());
		globalVariables.put("url_prefix", "http://www.soningbo.com");
		globalVariables.put("img_url_prefix", "http://img.soningbo.com");
		globalVariables.put("api_url_prefix", "http://api.soningbo.com");
		globalVariables.put("static_url_prefix", "http://static.soningbo.com");

		// 也可以存放一些共享的工具类,以便视图使用,如StringUtils

	}

	/** 在系统启动时会执行 */
	public void afterPropertiesSet() throws Exception {
		initSharedRenderVariables();
	}

}
