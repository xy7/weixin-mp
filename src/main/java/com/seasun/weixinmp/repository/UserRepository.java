/**
 * 
 */
package com.seasun.weixinmp.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seasun.weixinmp.service.UserService;
import com.seasun.weixinmp.service.vo.Group;
import com.seasun.weixinmp.service.vo.User;

/**
 *
 */
@Service
public class UserRepository {
//	private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

	@Autowired
	private UserService userService;
	
	private long expiredTime;

	private Map<String, User> userMap = new HashMap<>();

	private Map<String, Group> groupMap = new HashMap<>();

	public void load() {
		// 获取分组
		Map<String, Group> newGroupMap = userService.getGroupMap();
		// 获取用户
		Map<String, User> newUserMap = userService.getUserMap();
		// 建立用户和分组的联系
		if (newGroupMap != null && newUserMap != null) {
			for (Entry<String, User> entry : newUserMap.entrySet()) {
				String groupId = entry.getValue().getGroupid();
				Group group = newGroupMap.get(groupId);
				if (group != null) {
					group.getUserMap().put(entry.getValue().getOpenid(), entry.getValue());
				}
			}
			if (newGroupMap.size() > 0) {
				this.groupMap = newGroupMap;
			}
			if (newUserMap.size() > 0) {
				this.userMap = newUserMap;
			}
		}
//		this.expiredTime = System.currentTimeMillis() + 7200 * 1000;
		this.expiredTime = System.currentTimeMillis() + 60 * 1000;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() >= expiredTime;
	}
	
	/**
	 * @return the userMap
	 */
	public Map<String, User> getUserMap() {
		if (userMap == null || this.isExpired()) {
			this.load();
		}
		return userMap;
	}

	/**
	 * @return the groupMap
	 */
	public Map<String, Group> getGroupMap() {
		if (groupMap == null || this.isExpired()) {
			this.load();
		}
		return groupMap;
	}

}
