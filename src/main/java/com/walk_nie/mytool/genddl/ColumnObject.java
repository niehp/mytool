package com.walk_nie.mytool.genddl;

public class ColumnObject {

	public int pos;
	/** 属性(論理) */
	public String nameLogi;
	/** 属性(物理) */
	public String namePhi;
	/** ドメイン */
	public String domain;

	/** 区 */
	public boolean kuFlag;

	/** */
	public boolean pkFlag;
	/** */
	public boolean fkFlag;

	/** */
	public boolean akFlag;

	/** */
	public boolean ieFlag;

	/** */
	public boolean notNullFlag;

	/** データ型 */
	public String dataType;
	public String dataTypeString;
	/** 長さ/精度 */
	public String size;
	/** 初期値 */
	public String defaultValue;
	/** 説明 */
	public String desp;

}
