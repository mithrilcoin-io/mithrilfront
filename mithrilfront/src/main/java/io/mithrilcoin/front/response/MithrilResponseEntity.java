package io.mithrilcoin.front.response;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.mithril.vo.entity.MithrilResponse;
import io.mithril.vo.member.UserInfo;

/**
 * 미스릴 API 요청에 대응하는 미스릴 응답 메세지 클래스
 * 
 * @author Kei
 *
 * @param <T>
 *            body로 맵핑 되서 전달되는 객체 T type
 */
public class MithrilResponseEntity<T> extends ResponseEntity<T> {

	public MithrilResponseEntity(T body, HttpStatus status, HttpSession session) {
		super(setBody(body, session), status);
	}

	@SuppressWarnings("unchecked")
	private static <T> T setBody(T body, HttpSession session) {
		MithrilResponse<T> response = new MithrilResponse<T>(body);
		UserInfo userInfo = session.getAttribute("userInfo") == null ? null
				: (UserInfo) session.getAttribute("userInfo");
		
		if(userInfo != null)
		{
			response.setUserInfo(userInfo);
		}
		return (T) response;
	}

}
