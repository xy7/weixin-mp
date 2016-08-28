/**
 * 
 */
package com.seansun.weixinmp.service.vo;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 *
 */
public class UploadNewsRequest {
	// articles 鏄� 鍥炬枃娑堟伅锛屼竴涓浘鏂囨秷鎭敮鎸�1鍒�10鏉″浘鏂�
	private List<ArticleMessage> articles = new ArrayList<>();

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public void addArticle(ArticleMessage message) {
		this.articles.add(message);
	}

	/**
	 * @return the articles
	 */
	public List<ArticleMessage> getArticles() {
		return articles;
	}

	/**
	 * @param articles
	 *            the articles to set
	 */
	public void setArticles(List<ArticleMessage> articles) {
		this.articles = articles;
	}

}
