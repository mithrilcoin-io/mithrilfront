package io.mithrilcoin.front.biz.gamedata;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

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

import io.mithril.vo.member.UserInfo;
import io.mithril.vo.playdata.PlayData;
import io.mithril.vo.playdata.Playstoreappinfo;
import io.mithril.vo.playdata.TemporalPlayData;
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

	@Autowired
	private RedisDataRepository<String, String> redisdataRepo;

	@PostMapping("/validate/{id}")
	public MithrilResponseEntity<ArrayList<Playstoreappinfo>> filteringGameApp(
			@RequestBody ArrayList<Playstoreappinfo> applist, @PathVariable String id) throws Exception {
		String applistParam = objMapper.writeValueAsString(applist);
		ParameterizedTypeReference<ArrayList<Playstoreappinfo>> typeRef = new ParameterizedTypeReference<ArrayList<Playstoreappinfo>>() {
		};
		ArrayList<Playstoreappinfo> result = mithrilApiTemplate.post("/gamedata/validate", applistParam, typeRef);

		return new MithrilResponseEntity<ArrayList<Playstoreappinfo>>(result, HttpStatus.OK, id, userRedisSessionInfo);
	}

	@PostMapping("/insert/{id}")
	public MithrilResponseEntity<ArrayList<TemporalPlayData>> insertPlayData(
			@RequestBody ArrayList<TemporalPlayData> playdatalist, @PathVariable String id) throws Exception {

		String email = redisdataRepo.getData("email_" + id);
		email = URLEncoder.encode(email, "UTF-8");

		String datalistParam = objMapper.writeValueAsString(playdatalist);
		ParameterizedTypeReference<ArrayList<TemporalPlayData>> typeRef = new ParameterizedTypeReference<ArrayList<TemporalPlayData>>() {
		};

		ArrayList<TemporalPlayData> resultlist = mithrilApiTemplate.post("/gamedata/insert/" + email, datalistParam,
				typeRef);

		return new MithrilResponseEntity<ArrayList<TemporalPlayData>>(resultlist, HttpStatus.OK, id,
				userRedisSessionInfo);
	}

	@PostMapping("/insert/reward/{id}")
	public MithrilResponseEntity<TemporalPlayData> insertPlaydataReward(@RequestBody TemporalPlayData playdata,
			@PathVariable String id) throws Exception {
		if (playdata.getIdx() < 1) {
			return new MithrilResponseEntity<TemporalPlayData>(playdata, HttpStatus.OK, id, userRedisSessionInfo);
		}
		String email = redisdataRepo.getData("email_" + id);
		email = URLEncoder.encode(email, "UTF-8");
		String playdataParam = objMapper.writeValueAsString(playdata);
		ParameterizedTypeReference<TemporalPlayData> typeRef = new ParameterizedTypeReference<TemporalPlayData>() {
		};
		TemporalPlayData result = mithrilApiTemplate.post("/gamedata/insert/reward/" + email, playdataParam, typeRef);

		return new MithrilResponseEntity<TemporalPlayData>(result, HttpStatus.OK, id, userRedisSessionInfo);
	}

	@GetMapping("/select/nopage/{id}")
	public MithrilResponseEntity<ArrayList<PlayData>> selectPlayDataNopage(@PathVariable String id) throws Exception {
		String email = redisdataRepo.getData("email_" + id);
		email = URLEncoder.encode(email, "UTF-8");
		ParameterizedTypeReference<ArrayList<PlayData>> typeRef = new ParameterizedTypeReference<ArrayList<PlayData>>() {
		};
		ArrayList<PlayData> playdataList = mithrilApiTemplate.get("/gamedata/select/nopage/" + email, "", typeRef);

		return new MithrilResponseEntity<ArrayList<PlayData>>(playdataList, HttpStatus.OK, id, userRedisSessionInfo);

	}

}
