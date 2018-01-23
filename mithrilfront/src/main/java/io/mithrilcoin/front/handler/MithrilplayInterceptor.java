package io.mithrilcoin.front.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.mithril.vo.member.UserInfo;
import io.mithrilcoin.front.common.redis.RedisDataRepository;

@Component
public class MithrilplayInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private RedisDataRepository<String, UserInfo> userInforedis;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String[] urls = request.getRequestURI().split("/");
		String key = urls[urls.length - 1];
		
		UserInfo userInfo  = userInforedis.getData(key);

		if (userInfo == null) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
			return false;
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

}
