/**
 * 
 */
package com.seansun.weixinmp.service.vo;

/**
 *
 */
public class FilterObject {
	// is_to_all 鍚�
	// 鐢ㄤ簬璁惧畾鏄惁鍚戝叏閮ㄧ敤鎴峰彂閫侊紝鍊间负true鎴杅alse锛岄�夋嫨true璇ユ秷鎭兢鍙戠粰鎵�鏈夌敤鎴凤紝閫夋嫨false鍙牴鎹甮roup_id鍙戦�佺粰鎸囧畾缇ょ粍鐨勭敤鎴�
	private boolean is_to_all;
	// group_id 鍚� 缇ゅ彂鍒扮殑鍒嗙粍鐨刧roup_id锛屽弬鍔犵敤鎴风鐞嗕腑鐢ㄦ埛鍒嗙粍鎺ュ彛锛岃嫢is_to_all鍊间负true锛屽彲涓嶅～鍐檊roup_id
	private String group_id;

	/**
	 * @return the is_to_all
	 */
	public boolean isIs_to_all() {
		return is_to_all;
	}

	/**
	 * @param is_to_all
	 *            the is_to_all to set
	 */
	public void setIs_to_all(boolean is_to_all) {
		this.is_to_all = is_to_all;
	}

	/**
	 * @return the group_id
	 */
	public String getGroup_id() {
		return group_id;
	}

	/**
	 * @param group_id
	 *            the group_id to set
	 */
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

}
