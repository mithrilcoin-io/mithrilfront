package io.mithrilcoin.front.biz.message;

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

import io.mithril.vo.entity.MithrilApiResult;
import io.mithril.vo.entity.MithrilPlayCode;
import io.mithril.vo.member.UserInfo;
import io.mithril.vo.message.Message;
import io.mithrilcoin.front.common.redis.RedisDataRepository;
import io.mithrilcoin.front.common.rest.IRestTemplate;
import io.mithrilcoin.front.response.MithrilResponseEntity;

@Controller
@RequestMapping("/message")
public class MessageController {

	@Autowired
	@Qualifier("mithrilAPIRestTemplate")
	private IRestTemplate mithrilRestTemplate;
	
	@Autowired
	private ObjectMapper objMapper;
	

	@Autowired
	private RedisDataRepository<String, UserInfo> userRedisSessionInfo;
	
	@PostMapping("/send/{id}")
	public MithrilResponseEntity<MithrilApiResult> sendPushMessage(@PathVariable String id, @RequestBody Message message) throws Exception
	{
		ParameterizedTypeReference<Message> ref = new ParameterizedTypeReference<Message>() {};
		String parameter = objMapper.writeValueAsString(message);
		Message msg = mithrilRestTemplate.post("/message/send", parameter , ref);
		MithrilApiResult result = new MithrilApiResult();
		if( msg.getIdx() > 0)
		{
			result.setCode(MithrilPlayCode.SUCCESS);
		}
		else
		{
			result.setCode(MithrilPlayCode.API_FAIL);
		}
		return new MithrilResponseEntity<MithrilApiResult>(result, HttpStatus.OK, id, userRedisSessionInfo);
	}
	
	@PostMapping("/insert/response/{id}")
	public MithrilResponseEntity<MithrilApiResult> insertResponseMessage(@PathVariable String id, @RequestBody Message message) throws Exception
	{
		ParameterizedTypeReference<Message> ref = new ParameterizedTypeReference<Message>() {};
		String parameter = objMapper.writeValueAsString(message);
		Message msg = mithrilRestTemplate.post("/message/insert/response", parameter , ref);
		MithrilApiResult result = new MithrilApiResult();
		if( msg.getIdx() > 0)
		{
			result.setCode(MithrilPlayCode.SUCCESS);
		}
		else
		{
			result.setCode(MithrilPlayCode.API_FAIL);
		}
		return new MithrilResponseEntity<MithrilApiResult>(result, HttpStatus.OK, id, userRedisSessionInfo);
		
	}
}
