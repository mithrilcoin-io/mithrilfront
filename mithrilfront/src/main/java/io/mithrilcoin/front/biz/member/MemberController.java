package io.mithrilcoin.front.biz.member;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLEngineResult.Status;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mithril.vo.entity.MithrilApiResult;
import io.mithril.vo.entity.MithrilPlayCode;
import io.mithril.vo.member.Member;
import io.mithril.vo.member.MemberDetail;
import io.mithril.vo.member.MemberInfo;
import io.mithril.vo.member.UserInfo;
import io.mithril.vo.message.Message;
import io.mithrilcoin.front.common.redis.RedisDataRepository;
import io.mithrilcoin.front.common.rest.IRestTemplate;
import io.mithrilcoin.front.config.ServerInfoConfiguration;
import io.mithrilcoin.front.response.MithrilResponseEntity;
import io.mithrilcoin.front.util.DateUtil;
import io.mithrilcoin.front.util.HashingUtil;
import io.mithrilcoin.front.util.MailTemplateUtil;
import io.mithrilcoin.front.util.ParameterChanger;

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

	@Autowired
	private MailTemplateUtil mailTemplateUtil;

	@Autowired
	private ServerInfoConfiguration serverInfo;

	@Autowired
	private RedisDataRepository<String, String> redisDataRepo;

	@Autowired
	private RedisDataRepository<String, UserInfo> userRedisSessionInfo;

	/**
	 * 회원가입
	 * 
	 * @return
	 */
	@PostMapping("/signup")
	public MithrilResponseEntity<MithrilApiResult> signUp(@RequestBody @Valid MemberInfo signupMember,
			HttpServletRequest request) {

		logger.info("/member/singup/ ");
		MithrilApiResult result = new MithrilApiResult();
		String key = "";
		try {
			result.setRequestDate(new Date());
			request.getSession().invalidate();
			String memberRequest = objMapper.writeValueAsString(signupMember);
			ParameterizedTypeReference<MemberInfo> typeRef = new ParameterizedTypeReference<MemberInfo>() {
			};
			MemberInfo memberInfo = mithrilApiTemplate.post("/member/signup/", memberRequest, typeRef);

			// 정상적으로 회원등록완료
			if (memberInfo != null && memberInfo.getIdx() > 0) {
				// redis에 로그인으로 처리

				key = hashingUtil.getHashedString(memberInfo.getEmail() + memberInfo.getDeviceid());
				assignUserSession(memberInfo, null, key);
				result.setCode(MithrilPlayCode.SUCCESS);

			} else // 회원가입 실패
			{
				// 이미 있는 회원 문제
				if (memberInfo != null && memberInfo.getIdx() < 0) {

					result.setCode(MithrilPlayCode.EXIST_EMAIL);
					// return new MithrilResponseEntity<MithrilApiResult>("Email is already exist.",
					// HttpStatus.OK,
					// request.getSession());
				} else {
					result.setCode(MithrilPlayCode.API_FAIL);
					// return new MithrilResponseEntity<MithrilApiResult>("Fail", HttpStatus.OK,
					// request.getSession());
				}
			}

		} catch (Exception e) {
			result.setCode(MithrilPlayCode.API_ERROR);
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		result.setResponseDate(new Date());
		return new MithrilResponseEntity<MithrilApiResult>(result, HttpStatus.OK, key, userRedisSessionInfo);
	}

	/**
	 * 로그인
	 * 
	 * @return
	 */
	@PostMapping("/signin")
	public MithrilResponseEntity<MithrilApiResult> signIn(@RequestBody @Valid Member member,
			HttpServletRequest request) {
		logger.info("/member/signin/ ");
		request.getSession().invalidate();
		MithrilApiResult result = new MithrilApiResult();
		result.setRequestDate(new Date());
		String key = "";
		try {
			String memberRequest = objMapper.writeValueAsString(member);
			ParameterizedTypeReference<Member> typeRef = new ParameterizedTypeReference<Member>() {
			};
			Member findMember = mithrilApiTemplate.post("/member/signin/", memberRequest, typeRef);
			if (findMember != null) {
				if (findMember.getIdx() > 0) {

					ParameterizedTypeReference<UserInfo> ref = new ParameterizedTypeReference<UserInfo>() {
					};

					UserInfo userInfo = mithrilApiTemplate.get("/member/select/userInfo/" + findMember.getIdx() + "/",
							"", ref);

					key = hashingUtil.getHashedString(findMember.getEmail() + userInfo.getDeviceid());
					userInfo.setId(key);
					userInfo.setRecentLoginTime(dateUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					userInfo.setState(findMember.getState());
					userRedisSessionInfo.setData(key, userInfo, 30, TimeUnit.DAYS);
					redisDataRepo.setData("email_" + key, member.getEmail(), 30, TimeUnit.DAYS);

					// request.getSession().setAttribute("userInfo", userInfo);
					// hash id로 이메일 값 저장
					// request.getSession().setAttribute(userInfo.getId(), findMember.getEmail());
					result.setCode(MithrilPlayCode.SUCCESS);

				} else {
					result.setCode(MithrilPlayCode.PASSWORD_MATCH_FAIL);
					// return new MithrilResponseEntity<MithrilApiResult>("Invalid Password",
					// HttpStatus.OK, request.getSession());
				}
			} else {
				result.setCode(MithrilPlayCode.INVALID_USER);
				// return new MithrilResponseEntity<MithrilApiResult>("Invalid User",
				// HttpStatus.OK, request.getSession());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		result.setResponseDate(new Date());
		return new MithrilResponseEntity<MithrilApiResult>(result, HttpStatus.OK, key, userRedisSessionInfo);
	}

	/**
	 * 로그아웃
	 * 
	 * @return
	 */
	@PostMapping("/signout/{id}")
	public MithrilResponseEntity<MithrilApiResult> signOut(@PathVariable String id, HttpServletRequest request) {

		// HttpSession session = request.getSession();
		// UserInfo userInfo = session.getAttribute("userInfo") == null ? null
		// : (UserInfo) session.getAttribute("userInfo");

		UserInfo userInfo = userRedisSessionInfo.getData(id);

		MithrilApiResult result = new MithrilApiResult();
		result.setRequestDate(new Date());

		if (userInfo != null && userInfo.getId().equals(id)) {
			// 세션 정보 클리어
			// request.getSession().invalidate();
			userRedisSessionInfo.deleteData(id);
			redisDataRepo.deleteData("email_" + id);
			result.setCode(MithrilPlayCode.SUCCESS);
		} else {
			result.setCode(MithrilPlayCode.INVALID_USER);
			// return new MithrilResponseEntity<String>("WHO ARE U?", HttpStatus.OK,
			// request.getSession());
		}

		result.setResponseDate(new Date());
		return new MithrilResponseEntity<MithrilApiResult>(result, HttpStatus.OK, id, userRedisSessionInfo);
	}

	/**
	 * 회원 기본 정보 수정
	 * 
	 * @return
	 */

	// @PostMapping("/update/")
	public MithrilResponseEntity<String> updateMemberInfo() {
		return null;
	}

	/**
	 * 회원 추가 정보 수정
	 * 
	 * @return
	 */
	@PostMapping("/update/memberDetail/{id}")
	public MithrilResponseEntity<MithrilApiResult> updateMemberDetail(@PathVariable String id,
			@RequestBody MemberDetail memberDetail) {

		MithrilApiResult result = new MithrilApiResult();
		result.setRequestDate(new Date());
		try {

			String email = redisDataRepo.getData("email_" + id);

			MemberInfo member = new MemberInfo();
			member.setEmail(email);

			ParameterizedTypeReference<Member> ref = new ParameterizedTypeReference<Member>() {};
			Map<String,String> paramMap = ParameterChanger.extractFieldNameValueMap(MemberInfo.class, member);
			String memberString = ParameterChanger.urlEncodeUTF8(paramMap);
			 
			Member findMember = mithrilApiTemplate.get("/member/select/", memberString, ref);
			if (findMember.getIdx() > 0) {
				memberDetail.setMember_idx(findMember.getIdx());
				ParameterizedTypeReference<MemberDetail> typeref = new ParameterizedTypeReference<MemberDetail>() {};
				String detailParam = objMapper.writeValueAsString(memberDetail);
				MemberDetail resultDetail = mithrilApiTemplate.post("/member/update/memberdetail", detailParam, typeref);
				
				if( resultDetail.getIdx() > 0)
				{
					UserInfo info = userRedisSessionInfo.getData(id);
					info.setMemberDetail(resultDetail);
					info.setState("M001003");
					userRedisSessionInfo.setData(id, info, 30 , TimeUnit.DAYS);
					result.setCode(MithrilPlayCode.SUCCESS);
				}
				else
				{
					result.setCode(MithrilPlayCode.API_FAIL);
				}
				
			} else {
				result.setCode(MithrilPlayCode.NOT_FOUND);
			}

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			result.setCode(MithrilPlayCode.API_ERROR);

		}
		result.setResponseDate(new Date());
		return new MithrilResponseEntity<MithrilApiResult>(result, HttpStatus.OK, id, userRedisSessionInfo);
	}

	@PostMapping("/sendmail/auth/{id}")
	public MithrilResponseEntity<MithrilApiResult> sendAuthMail(@PathVariable String id, HttpServletRequest request) {

		MithrilApiResult result = new MithrilApiResult();
		result.setRequestDate(new Date());

		String newHashedString = hashingUtil.getHashedString("authMail_" + id);
		String emailId = redisDataRepo.getData("authMail_" + newHashedString);
		if (emailId == null) {
			String emailAddress = redisDataRepo.getData("email_" + id);
			Message authMail = new Message();
			authMail.setSender(mailTemplateUtil.getDefaultSender());
			authMail.setReceiver(emailAddress);
			authMail.setBody(
					mailTemplateUtil.getAuthMailBody(serverInfo.getMyFullUrl() + "/member/auth/" + newHashedString));
			authMail.setState("M002001");
			authMail.setTypecode("T001001");
			authMail.setTitle(mailTemplateUtil.getAuthTitle() + emailAddress);
			// authMail.setBody(body);

			try {
				String mailParam = objMapper.writeValueAsString(authMail);
				ParameterizedTypeReference<Message> typeRef = new ParameterizedTypeReference<Message>() {
				};
				Message resultMessage = mithrilApiTemplate.post("/message/send", mailParam, typeRef);
				if (resultMessage != null && "M002003".equals(resultMessage.getState())) {
					result.setCode(MithrilPlayCode.SUCCESS);
					// 24 시간 동안만 유효한 코드 생성
					redisDataRepo.setData("authMail_" + newHashedString, emailAddress, 24, TimeUnit.HOURS);
				} else {
					result.setCode(MithrilPlayCode.API_FAIL);
				}

			} catch (Exception e) {
				result.setCode(MithrilPlayCode.API_ERROR);
				e.printStackTrace();
			}
		} else {
			result.setCode(MithrilPlayCode.ALREADY_SENDED);
		}

		result.setResponseDate(new Date());
		return new MithrilResponseEntity<MithrilApiResult>(result, HttpStatus.OK, id, userRedisSessionInfo);
	}

	@GetMapping("/auth/{id}")
	public String authorizeMemberMail(@PathVariable String id, Model model) {
		String emailId = redisDataRepo.getData("authMail_" + id);
		String verifyResult = "display: none;";

		model.addAttribute("success", verifyResult);
		model.addAttribute("duplicate", verifyResult);
		model.addAttribute("fail", "");

		// 정상적으로 생성된 아이디를 가져왔을 경우
		if (emailId != null) {

			try {
				Member member = new Member();
				member.setEmail(emailId);
				String memberRequest;
				memberRequest = objMapper.writeValueAsString(member);
				ParameterizedTypeReference<Member> typeRef = new ParameterizedTypeReference<Member>() {
				};
				Member updateMember = mithrilApiTemplate.post("/member/authorize/", memberRequest, typeRef);

				if (updateMember != null) {

					if (!"".equals(updateMember.getState())) {
						// 처리후 삭제
						redisDataRepo.deleteData("authMail_" + id);

						ParameterizedTypeReference<UserInfo> ref = new ParameterizedTypeReference<UserInfo>() {
						};

						UserInfo userInfo = mithrilApiTemplate
								.get("/member/select/userInfo/" + updateMember.getIdx() + "/", "", ref);

						String key = hashingUtil.getHashedString(updateMember.getEmail() + userInfo.getDeviceid());

						UserInfo info = userRedisSessionInfo.getData(key);
						info.setState(updateMember.getState());
						userRedisSessionInfo.setData(key, info, 30, TimeUnit.DAYS);

						model.addAttribute("success", "");
						model.addAttribute("fail", verifyResult);
					} else {
						model.addAttribute("duplicate", "");
						model.addAttribute("fail", verifyResult);
					}

				} else {
					model.addAttribute("fail", "");
				}

			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "member/emailLanding";
	}

	/**
	 * 사용자 정보 세션 셋팅 함수
	 * 
	 * @param info
	 * @param detail
	 * @param session
	 * @return
	 */
	private UserInfo assignUserSession(MemberInfo info, MemberDetail detail, String key) {

		UserInfo userInfo = new UserInfo();

		// hashing 된 고유 아이디 전달
		userInfo.setId(key);
		userInfo.setRecentLoginTime(dateUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
		userInfo.setDeviceid(info.getDeviceid());
		userInfo.setState(info.getState());
		userInfo.setMemberDetail(detail);
		userRedisSessionInfo.setData(key, userInfo, 30, TimeUnit.DAYS);
		redisDataRepo.setData("email_" + key, info.getEmail(), 30, TimeUnit.DAYS);

		// session.setAttribute("userInfo", userInfo);
		// hash id로 이메일 값 저장
		// session.setAttribute(userInfo.getId(), info.getEmail());
		return userInfo;
	}

	@GetMapping("/select/userInfo/{id}")
	public MithrilResponseEntity<MithrilApiResult> getMyAccountInfo(@PathVariable String id) {

		MithrilApiResult result = new MithrilApiResult();

		result.setRequestDate(new Date());

		UserInfo userInfo = userRedisSessionInfo.getData(id);

		if (userInfo != null) {
			result.setCode(MithrilPlayCode.SUCCESS);
		} else {
			result.setCode(MithrilPlayCode.INVALID_USER);
		}

		result.setResponseDate(new Date());
		return new MithrilResponseEntity<MithrilApiResult>(result, HttpStatus.OK, id, userRedisSessionInfo);

	}

}
