package io.mithrilcoin.front.biz.gamedata;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.convert.RedisData;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mithril.vo.entity.MithrilApiResult;
import io.mithril.vo.member.MemberInfo;
import io.mithril.vo.member.UserInfo;
import io.mithril.vo.playdata.Playstoreappinfo;
import io.mithrilcoin.front.common.redis.RedisDataRepository;
import io.mithrilcoin.front.common.rest.IRestTemplate;
import io.mithrilcoin.front.response.MithrilResponseEntity;

@Controller
@RequestMapping("/gamedata")
public class GamedataController {

	// public ArrayList<Playdat>

	@Autowired
	@Qualifier("mithrilAPIRestTemplate")
	private IRestTemplate mithrilApiTemplate;

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	private RedisDataRepository<String, UserInfo> userRedisSessionInfo;

	@PostMapping("/validate/{id}")
	public MithrilResponseEntity<ArrayList<Playstoreappinfo>> filteringGameApp(
			@RequestBody ArrayList<Playstoreappinfo> applist, @PathVariable String id) throws Exception {
		String applistParam = objMapper.writeValueAsString(applist);
		ParameterizedTypeReference<ArrayList<Playstoreappinfo>> typeRef = new ParameterizedTypeReference<ArrayList<Playstoreappinfo>>() {
		};
		ArrayList<Playstoreappinfo> result = mithrilApiTemplate.post("/gamedata/validate", applistParam, typeRef);

		return new MithrilResponseEntity<ArrayList<Playstoreappinfo>>(result, HttpStatus.OK, id, userRedisSessionInfo);
	}

}
