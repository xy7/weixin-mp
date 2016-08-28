/**
 * 
 */
package com.seansun.weixinmp.service.vo;

/**
 *
 */
public class ArticleMessage {
	// thumb_media_id 鏄� 鍥炬枃娑堟伅缂╃暐鍥剧殑media_id锛屽彲浠ュ湪鍩虹鏀寔-涓婁紶澶氬獟浣撴枃浠舵帴鍙ｄ腑鑾峰緱
	private String thumb_media_id;
	// author 鍚� 鍥炬枃娑堟伅鐨勪綔鑰�
	private String author;
	// title 鏄� 鍥炬枃娑堟伅鐨勬爣棰�
	private String title;
	// content_source_url 鍚� 鍦ㄥ浘鏂囨秷鎭〉闈㈢偣鍑烩�滈槄璇诲師鏂団�濆悗鐨勯〉闈�
	private String content_source_url;
	// content 鏄� 鍥炬枃娑堟伅椤甸潰鐨勫唴瀹癸紝鏀寔HTML鏍囩銆傚叿澶囧井淇℃敮浠樻潈闄愮殑鍏紬鍙凤紝鍙互浣跨敤a鏍囩锛屽叾浠栧叕浼楀彿涓嶈兘浣跨敤
	private String content;
	// digest 鍚� 鍥炬枃娑堟伅鐨勬弿杩�
	private String digest;
	// show_cover_pic 鍚� 鏄惁鏄剧ず灏侀潰锛�1涓烘樉绀猴紝0涓轰笉鏄剧ず
	private String show_cover_pic;
	public static final String SHOW_COVER_PIC = "1";
	public static final String SHOW_COVER_PIC_NO = "0";

	/**
	 * @return the thumb_media_id
	 */
	public String getThumb_media_id() {
		return thumb_media_id;
	}

	/**
	 * @param thumb_media_id
	 *            the thumb_media_id to set
	 */
	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the content_source_url
	 */
	public String getContent_source_url() {
		return content_source_url;
	}

	/**
	 * @param content_source_url
	 *            the content_source_url to set
	 */
	public void setContent_source_url(String content_source_url) {
		this.content_source_url = content_source_url;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the digest
	 */
	public String getDigest() {
		return digest;
	}

	/**
	 * @param digest
	 *            the digest to set
	 */
	public void setDigest(String digest) {
		this.digest = digest;
	}

	/**
	 * @return the show_cover_pic
	 */
	public String getShow_cover_pic() {
		return show_cover_pic;
	}

	/**
	 * @param show_cover_pic
	 *            the show_cover_pic to set
	 */
	public void setShow_cover_pic(String show_cover_pic) {
		this.show_cover_pic = show_cover_pic;
	}

}
