/**
 *	
 * @Title:TreeNode.java
 * @author Riozen
 *	@date 2013-12-25 下午2:06:43
 *	
 */
package com.riozenc.quicktool.common.util.json;

public class TreeNode {
	private String id;// id
	private String text;// 中文名
	private boolean leaf;// 是否是叶子节点
	private String mUrl;// 连接的地址
	private String cls;// 节点样式
	private String button;
	private String sortNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getmUrl() {
		return mUrl;
	}

	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public String getButton() {
		return button;
	}

	public void setButton(String button) {
		this.button = button;
	}

	public String getSortNo() {
		return sortNo;
	}

	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}

}
