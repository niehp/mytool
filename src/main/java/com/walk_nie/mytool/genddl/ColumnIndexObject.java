package com.walk_nie.mytool.genddl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ColumnIndexObject {
	// 物理名(rowIndex,columnIndex)
	public int tableNamePhiRowIndexDefault = -1;
	public int tableNamePhiColumnIndexDefault = -1;
	// 物理名(rowIndex,columnIndex)--テーブルごとカスタマイズ
	Map<String, Integer> tableNamePhiRowIndex = Maps.newHashMap();
	Map<String, Integer> tableNamePhiColumnIndex = Maps.newHashMap();

	// 論理名(rowIndex,columnIndex)
	public int tableNameLogiRowIndexDefault = -1;
	public int tableNameLogiColumnIndexDefault = -1;
	// 論理名(rowIndex,columnIndex)--テーブルごとカスタマイズ
	Map<String, Integer> tableNameLogiRowIndex = Maps.newHashMap();
	Map<String, Integer> tableNameLogiColumnIndex = Maps.newHashMap();

	public int startRowIndexDefault = -1;
	Map<String, Integer> startRowIndex = Maps.newHashMap();
	
	//属性(物理)				
	public int columnPhiColumnIndexDefault = -1;
	Map<String, Integer> columnPhiColumnIndex = Maps.newHashMap();
	// 属性(論理)				
	public int columnLogiColumnIndexDefault = -1;
	Map<String, Integer> columnLogiColumnIndex = Maps.newHashMap();
	/** ドメイン */
	public int domainColumnIndexDefault = -1;
	Map<String, Integer> domainColumnIndex = Maps.newHashMap();
	/** */
	public int kuFlagColumnIndexDefault = -1;
	Map<String, Integer> kuFlagColumnIndex = Maps.newHashMap();
	/** */
	public int pkFlagColumnIndexDefault = -1;
	Map<String, Integer> pkFlagColumnIndex = Maps.newHashMap();
	/** */
	public int fkFlagColumnIndexDefault = -1;
	Map<String, Integer> fkFlagColumnIndex = Maps.newHashMap();
	/** */
	public int akFlagColumnIndexDefault = -1;
	Map<String, Integer> akFlagColumnIndex = Maps.newHashMap();
	/** */
	public int ieFlagColumnIndexDefault = -1;
	Map<String, Integer> ieFlagColumnIndex = Maps.newHashMap();
	/** */
	public int notNullFlagColumnIndexDefault = -1;
	Map<String, Integer> notNullFlagColumnIndex = Maps.newHashMap();
	/** データ型 */
	public int dataTypeColumnIndexDefault = -1;
	Map<String, Integer> dataTypeColumnIndex = Maps.newHashMap();
	/** 長さ/精度 */
	public int sizeColumnIndexDefault = -1;
	Map<String, Integer> sizeColumnIndex = Maps.newHashMap();
	/** 初期値 */
	public int defaultValueColumnIndexDefault = -1;
	Map<String, Integer> defaultValueColumnIndex = Maps.newHashMap();
	/** 説明 */
	public int despColumnIndexDefault = -1;
	Map<String, Integer> despColumnIndex = Maps.newHashMap();

	public List<String> excludeSheetName = Lists.newArrayList();
}
