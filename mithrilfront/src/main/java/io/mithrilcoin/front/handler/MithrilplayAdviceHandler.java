package io.mithrilcoin.front.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import io.mithril.vo.entity.MithrilResponse;
import io.mithril.vo.member.UserInfo;
import io.mithril.vo.mtp.MtpTotal;
import io.mithril.vo.rate.MemberRating;
import io.mithrilcoin.front.common.redis.RedisDataRepository;
import io.mithrilcoin.front.common.rest.IRestTemplate;

@ControllerAdvice
public class MithrilplayAdviceHandler implements ResponseBodyAdvice<Object> {

	@Autowired
	private RedisDataRepository<String, UserInfo> userInforedis;

	@Autowired
	private RedisDataRepository<String, String> redisdataRepo;

	@Autowired
	@Qualifier("mithrilAPIRestTemplate")
	private IRestTemplate mithrilApiTemplate;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {

		if (body instanceof MithrilResponse) {

			MithrilResponse mithriResponse = ((MithrilResponse) body);
			if (mithriResponse.getUserInfo() != null) {
				String key = mithriResponse.getUserInfo().getId();
				if (userInforedis.hasContainKey(key)) {
					UserInfo userInfo = userInforedis.getData(key);
					if (userInfo != null) {

						try {
							userInfo = updatePersonalMTP(key, userInfo);
							// call realtime Rank data.
							userInfo.setMemberRating(updateUserRating(key));
							mithriResponse.setUserInfo(userInfo);

						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						userInforedis.setData(key, userInfo, 30, TimeUnit.DAYS);
					}
				}
			}

		}
		return body;
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

	private MemberRating updateUserRating(String key) throws UnsupportedEncodingException {
		String email = redisdataRepo.getData("email_" + key);
		String encodeEmail = URLEncoder.encode(email, "UTF-8");
		ParameterizedTypeReference<MemberRating> typeRef = new ParameterizedTypeReference<MemberRating>() {
		};
		MemberRating rate = mithrilApiTemplate.get("/rate/select/" + encodeEmail, "", typeRef);

		return rate;
	}

}
