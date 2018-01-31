package io.mithrilcoin.front.response;

import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.mithril.vo.entity.MithrilResponse;
import io.mithril.vo.member.UserInfo;
import io.mithrilcoin.front.common.redis.RedisDataRepository;
/**

 * 미스릴 API 요청에 대응하는 미스릴 응답 메세지 클래스
 * 
 * @author Kei
 *
 * @param <T>
 *            body로 맵핑 되서 전달되는 객체 T type
 */
public class MithrilResponseEntity<T> extends ResponseEntity<T> {
	public static final long VALID_PLAY_TIME = 60000;
	public MithrilResponseEntity(T body, HttpStatus status, String id, RedisDataRepository<String, UserInfo> redisSession) {
		super(setBody(body, redisSession, id), status);
	}

	@SuppressWarnings("unchecked")
	private static <T> T setBody(T body,  RedisDataRepository<String, UserInfo> redisSession, String id) {
		MithrilResponse<T> response = new MithrilResponse<T>(body);
		UserInfo userInfo = redisSession.getData(id);
		
		if(userInfo != null && !"".equals(id))
		{
			if(userInfo.getMemberDetail() != null)
			{
				userInfo.getMemberDetail().setIdx(0);
				userInfo.getMemberDetail().setMember_idx(0);
			}
			if( userInfo.getMtptotal() != null)
			{
				userInfo.getMtptotal().setMember_idx(0);
			}
			userInfo.setValidtime(MithrilResponseEntity.VALID_PLAY_TIME);
			response.setUserInfo(userInfo);
			
			// data expire time 갱신 
			redisSession.setData(id, userInfo, 30, TimeUnit.DAYS);
		}
		return (T) response;
	}
	

}
