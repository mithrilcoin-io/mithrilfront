package io.mithrilcoin.front.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.mithril.vo.member.UserInfo;
import io.mithril.vo.mtp.MtpTotal;
import io.mithrilcoin.front.common.redis.RedisDataRepository;
import io.mithrilcoin.front.common.rest.IRestTemplate;

@Component
public class MithrilplayInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private RedisDataRepository<String, UserInfo> userInforedis;

	@Autowired
	private RedisDataRepository<String, String> redisdataRepo;

	@Autowired
	@Qualifier("mithrilAPIRestTemplate")
	private IRestTemplate mithrilApiTemplate;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String[] urls = request.getRequestURI().split("/");
		String key = urls[urls.length - 1];
		
		if(userInforedis.hasContainKey(key))
		{
			UserInfo userInfo = userInforedis.getData(key);
			if (userInfo == null) {
				response.sendError(HttpServletResponse.SC_CONFLICT, "다른 기기에서 로그인 되었습니다.");
				return false;
			}
			userInfo = updatePersonalMTP(key, userInfo);
			userInforedis.setData(key, userInfo, 30, TimeUnit.DAYS);
		}
		else
		{
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
			return false;
		}

		return true;
	}

	private UserInfo updatePersonalMTP(String key, UserInfo userInfo) throws UnsupportedEncodingException {
		String email = redisdataRepo.getData("email_" + key);
		String encodeEmail = URLEncoder.encode(email, "UTF-8");
		ParameterizedTypeReference<MtpTotal> typeRef = new ParameterizedTypeReference<MtpTotal>() {
		};
		MtpTotal total = mithrilApiTemplate.get("/mtp/select/" + encodeEmail, "", typeRef);
		userInfo.setMtptotal(total);
		return userInfo;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

}
