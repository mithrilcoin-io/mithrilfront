package io.mithrilcoin.front.biz.transaction;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mithril.vo.blockchain.Transaction;
import io.mithril.vo.member.Member;
import io.mithril.vo.member.MemberInfo;
import io.mithril.vo.member.UserInfo;
import io.mithrilcoin.front.common.redis.RedisDataRepository;
import io.mithrilcoin.front.common.rest.IRestTemplate;
import io.mithrilcoin.front.response.MithrilResponseEntity;
import io.mithrilcoin.front.util.ParameterChanger;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

	@Autowired
	@Qualifier("mithrilAPIRestTemplate")
	private IRestTemplate mithrilApiTemplate;

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	private RedisDataRepository<String, UserInfo> userRedisSessionInfo;

	@Autowired
	private RedisDataRepository<String, String> redisDataRepo;

	@PostMapping("/insert/{id}")
	public MithrilResponseEntity<Transaction> insertTransaction(@RequestBody Transaction transaction,
			@PathVariable String id) throws Exception {

		String email = redisDataRepo.getData("email_" + id);
		Transaction result = transaction;
		MemberInfo member = new MemberInfo();
		member.setEmail(email);

		ParameterizedTypeReference<Member> ref = new ParameterizedTypeReference<Member>() {
		};
		Map<String, String> paramMap = ParameterChanger.extractFieldNameValueMap(MemberInfo.class, member);
		String memberString = ParameterChanger.urlEncodeUTF8(paramMap);

		Member findMember = mithrilApiTemplate.get("/member/select/", memberString, ref);
		if (findMember.getIdx() > 0) {
			transaction.setMember_idx(findMember.getIdx());
			String applistParam = objMapper.writeValueAsString(transaction);
			ParameterizedTypeReference<Transaction> typeRef = new ParameterizedTypeReference<Transaction>() {
			};

			result = mithrilApiTemplate.post("/transaction/insert", applistParam, typeRef);
		} else {
			// -1 떨어지면 인서트 실패.
			result.setIdx(-1);
		}

		return new MithrilResponseEntity<Transaction>(result, HttpStatus.OK, id, userRedisSessionInfo);

	}

	@GetMapping("/select/listnopage/{id}")
	public MithrilResponseEntity<ArrayList<Transaction>> selectlistNopage(Transaction transaction,
			@PathVariable String id) {
		String email = redisDataRepo.getData("email_" + id);
		MemberInfo member = new MemberInfo();
		member.setEmail(email);

		ParameterizedTypeReference<Member> ref = new ParameterizedTypeReference<Member>() {
		};
		Map<String, String> paramMap = ParameterChanger.extractFieldNameValueMap(MemberInfo.class, member);
		String memberString = ParameterChanger.urlEncodeUTF8(paramMap);

		ArrayList<Transaction> list = new ArrayList<>();

		Member findMember = mithrilApiTemplate.get("/member/select/", memberString, ref);
		if (findMember.getIdx() > 0) {
			
			transaction.setMember_idx(findMember.getIdx());
			ParameterizedTypeReference<ArrayList<Transaction>> typeRef = new ParameterizedTypeReference<ArrayList<Transaction>>() {
			};
			Map<String, String> map = ParameterChanger.extractFieldNameValueMap(Transaction.class, transaction);
			String transParamString = ParameterChanger.urlEncodeUTF8(map);

			list = mithrilApiTemplate.get("/transaction/select/listnopage", transParamString, typeRef);
		}
		return new MithrilResponseEntity<ArrayList<Transaction>>(list, HttpStatus.OK, id, userRedisSessionInfo);

	}

}
