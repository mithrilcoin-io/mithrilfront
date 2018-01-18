package io.mithrilcoin.front.biz.member;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mithril.vo.member.MemberDetail;
import io.mithril.vo.member.MemberInfo;
import io.mithril.vo.member.UserInfo;
import io.mithrilcoin.front.common.rest.IRestTemplate;
import io.mithrilcoin.front.response.MithrilResponseEntity;
import io.mithrilcoin.front.util.DateUtil;
import io.mithrilcoin.front.util.HashingUtil;

@Controller
@RequestMapping("/member")
public class MemberController {

	private static Logger logger = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	@Qualifier("mithrilAPIRestTemplate")
	private IRestTemplate mithrilApiTemplate;

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	private HashingUtil hashingUtil;

	@Autowired
	private DateUtil dateUtil;

	/**
	 * 회원가입
	 * 
	 * @return
	 */
	@PostMapping("/signup")
	public MithrilResponseEntity<String> signUp(@RequestBody @Valid MemberInfo signupMember,
			HttpServletRequest request) {

		logger.info("/member/singup/ ");
		String memberRequest;
		try {
			request.getSession().invalidate();
			memberRequest = objMapper.writeValueAsString(signupMember);
			ParameterizedTypeReference<MemberInfo> typeRef = new ParameterizedTypeReference<MemberInfo>() {
			};
			MemberInfo memberInfo = mithrilApiTemplate.post("/member/signup/", memberRequest, typeRef);
			// 정상적으로 회원등록완료
			if (memberInfo != null && memberInfo.getIdx() > 0) {
				// 세션에 로그인으로 처리
				assignUserSession(memberInfo, null, request.getSession());
				return new MithrilResponseEntity<String>("OK", HttpStatus.OK, request.getSession());
			} else // 회원가입 실패
			{
				// 이미 있는 회원 문제
				if (memberInfo.getIdx() < 0) {
					return new MithrilResponseEntity<String>("Request email is already exist.", HttpStatus.OK,
							request.getSession());
				} else {
					return new MithrilResponseEntity<String>("Fail", HttpStatus.OK, request.getSession());
				}
			}

		} catch (Exception e) {

			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return new MithrilResponseEntity<String>("Error", HttpStatus.OK, request.getSession());
	}

	/**
	 * 로그인
	 * 
	 * @return
	 */
	public MithrilResponseEntity<String> signIn() {
		return null;
	}

	/**
	 * 로그아웃
	 * 
	 * @return
	 */
	@PostMapping("/signout/{id}")
	public MithrilResponseEntity<String> signOut(@PathVariable String id, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		UserInfo userInfo = session.getAttribute("userInfo") == null ? null
				: (UserInfo) session.getAttribute("userInfo");
		if(userInfo != null && userInfo.getId().equals(id))
		{
			// 세션 정보 클리어 
			request.getSession().invalidate();
			return new MithrilResponseEntity<String>("OK", HttpStatus.OK, request.getSession());
		}
		else
		{
			return new MithrilResponseEntity<String>("WHO ARE U?", HttpStatus.OK, request.getSession());
		}
	
	}

	/**
	 * 회원 기본 정보 수정
	 * 
	 * @return
	 */
	public MithrilResponseEntity<String> updateMemberInfo() {
		return null;
	}

	/**
	 * 회원 추가 정보 수정
	 * 
	 * @return
	 */
	public MithrilResponseEntity<String> updateMemberDetail() {
		return null;
	}

	private UserInfo assignUserSession(MemberInfo info, MemberDetail detail, HttpSession session) {

		UserInfo userInfo = new UserInfo();

		// hashing 된 고유 아이디 전달
		userInfo.setId(hashingUtil.getHashedString(info.getEmail()));
		userInfo.setRecentLoginTime(dateUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
		userInfo.setDeviceid(info.getDeviceid());
		userInfo.setState(info.getState());
		userInfo.setMemberDetail(detail);
		session.setAttribute("userInfo", userInfo);

		return userInfo;
	}

}
