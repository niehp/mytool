package com.walk_nie.mytool.genddl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.common.collect.Lists;
import com.walk_nie.mytool.NieConfigUtil;
import com.walk_nie.mytool.excel.NieExcelLayoutUtil;

public class GenerateDDLV2 {
	private ColumnIndexObject indexObj = null;

	public static void main(String[] args) throws Exception {
		new GenerateDDLV2().generate(args[0]);
	}

	public void generate(String entityExcelFilePath) throws IOException {
		File excelFile = new File(entityExcelFilePath);
		initIdex();
		List<TableObject> tableList = parse(excelFile);
		if (tableList == null) {
			return;
		}
		for (TableObject table : tableList) {
			makeColumnTypeToString(table);
			writeOut(table);
		}
	}

	private void initIdex() {
		indexObj = new ColumnIndexObject();
		String mainConfigKey = "genddl.";
		// 物理名
		String configKey = mainConfigKey + "tblNamePhi.rowColumnIndex";
		Map<String, String> map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				String[] sp = entry.getValue().split(",");
				indexObj.tableNamePhiRowIndexDefault = Integer.parseInt(sp[0]);
				indexObj.tableNamePhiColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(sp[1]);
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				String[] sp = entry.getValue().split(",");
				indexObj.tableNamePhiRowIndex.put(tblName, Integer.parseInt(sp[0]));
				indexObj.tableNamePhiColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(sp[1]));
			}
		}
		// 論理名
		configKey = mainConfigKey + "tblNameLogi.rowColumnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				String[] sp = entry.getValue().split(",");
				indexObj.tableNameLogiRowIndexDefault = Integer.parseInt(sp[0]);
				indexObj.tableNameLogiColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(sp[1]);
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				String[] sp = entry.getValue().split(",");
				indexObj.tableNameLogiRowIndex.put(tblName, Integer.parseInt(sp[0]));
				indexObj.tableNameLogiColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(sp[1]));
			}
		}

		// startRowIndex
		configKey = mainConfigKey + "startRow.rowIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.startRowIndexDefault = Integer.parseInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.startRowIndex.put(tblName, Integer.parseInt(entry.getValue()));
			}
		}

		// 属性(論理)
		configKey = mainConfigKey + "columnPhi.columnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.columnPhiColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.columnPhiColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(entry.getValue()));
			}
		}
		// 属性(物理)
		configKey = mainConfigKey + "columnLogi.columnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.columnLogiColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.columnLogiColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(entry.getValue()));
			}
		}
		// ドメイン
		configKey = mainConfigKey + "domain.columnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.domainColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.domainColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(entry.getValue()));
			}
		}
		// 区
		configKey = mainConfigKey + "kuFlag.columnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.kuFlagColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.kuFlagColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(entry.getValue()));
			}
		}
		// PK
		configKey = mainConfigKey + "pkFlag.columnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.pkFlagColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.pkFlagColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(entry.getValue()));
			}
		}
		// FK
		configKey = mainConfigKey + "fkFlag.columnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.fkFlagColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.fkFlagColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(entry.getValue()));
			}
		}
		// AK
		configKey = mainConfigKey + "akFlag.columnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.akFlagColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.akFlagColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(entry.getValue()));
			}
		}
		// IE
		configKey = mainConfigKey + "ieFlag.columnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.ieFlagColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.ieFlagColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(entry.getValue()));
			}
		}
		// NN
		configKey = mainConfigKey + "notNullFlag.columnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.notNullFlagColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.notNullFlagColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(entry.getValue()));
			}
		}
		// 参照先
		// データ型
		configKey = mainConfigKey + "dataType.columnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.dataTypeColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.dataTypeColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(entry.getValue()));
			}
		}
		// 長さ/精度
		configKey = mainConfigKey + "size.columnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.sizeColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.sizeColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(entry.getValue()));
			}
		}
		// 初期値
		configKey = mainConfigKey + "defaultValue.columnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.defaultValueColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.defaultValueColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(entry.getValue()));
			}
		}
		// 定義
		configKey = mainConfigKey + "desp.columnIndex";
		map = NieConfigUtil.getConfigsByPrefix(configKey);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(configKey)) {
				indexObj.despColumnIndexDefault = NieExcelLayoutUtil.alphebetToInt(entry.getValue());
			} else {
				String tblName = entry.getKey().substring(configKey.length() + 1);
				indexObj.despColumnIndex.put(tblName, NieExcelLayoutUtil.alphebetToInt(entry.getValue()));
			}
		}
		configKey = mainConfigKey + "excludeSheetName";
		String val = NieConfigUtil.getConfig(configKey);
		String[] sp = val.split(",");
		for (String str : sp) {
			indexObj.excludeSheetName.add(str);
		}

	}

	private void makeColumnTypeToString(TableObject table) {
		for (ColumnObject column : table.columnList) {
			if (column.size.endsWith(".0")) {
				column.size = column.size.substring(0, column.size.lastIndexOf(".0"));
			}
			if (column.dataType.equalsIgnoreCase("VARCHAR") || column.dataType.equalsIgnoreCase("VARCHAR2")) {
				column.dataTypeString = "VARCHAR2(" + column.size + ")";
			} else if (column.dataType.equalsIgnoreCase("INTEGER")) {
				column.dataTypeString = "NUMBER(" + column.size + ",0)";
			} else if (column.dataType.equalsIgnoreCase("DECIMAL") || column.dataType.equalsIgnoreCase("NUMERIC")
					|| column.dataType.equalsIgnoreCase("NUMBER")) {
				if (column.size.indexOf(",") == -1) {
					column.dataTypeString = "NUMBER(" + column.size + ",0)";
				} else {
					column.dataTypeString = "NUMBER(" + column.size + ")";
				}
			} else if (column.dataType.equalsIgnoreCase("TIMESTAMP")) {
				column.dataTypeString = "TIMESTAMP";
			} else if (column.dataType.equalsIgnoreCase("DATE")) {
				column.dataTypeString = "DATE";
			}
		}
	}

	private void writeOut(TableObject table) throws IOException {
		String sqlseperator = "/";// [/] or [;]
		String crlf = "\n";
		StringBuffer outLine = new StringBuffer();
		outLine.append("DROP TABLE " + table.namePhi).append(crlf);
		outLine.append(sqlseperator).append(crlf);
		outLine.append(String.format("CREATE TABLE %s (", table.namePhi)).append(crlf);
		for (ColumnObject column : table.columnList) {
			String str = String.format("  %-33s %s", column.namePhi, column.dataTypeString);
			if (!StringUtils.isEmpty(column.defaultValue)) {
				if (column.defaultValue.endsWith(".0")) {
					column.defaultValue = column.size.substring(0, column.defaultValue.lastIndexOf(".0"));
				}
				str += " DEFAULT '" + column.defaultValue + "'";
			}
			if (column.notNullFlag) {
				str += " NOT NULL ENABLE";
			}
			outLine.append(str).append(",").append(crlf);
		}
		String pk = String.format("  CONSTRAINT PK_%s PRIMARY KEY (", table.namePhi);
		for (ColumnObject column : table.columnList) {
			if (column.pkFlag) {
				pk += column.namePhi + ",";
			}
		}
		pk = pk.substring(0, pk.length() - 1);
		pk += ")";
		outLine.append(pk).append(crlf);
		outLine.append(")").append(crlf);
		outLine.append(sqlseperator).append(crlf);
		outLine.append(String.format("COMMENT ON TABLE %s IS '%s'", table.namePhi, table.nameLogi)).append(crlf);
		outLine.append(sqlseperator).append(crlf);
		for (ColumnObject column : table.columnList) {
			outLine.append(
					String.format("COMMENT ON COLUMN %s.%s IS '%s'", table.namePhi, column.namePhi, column.nameLogi))
					.append(crlf);
			outLine.append(sqlseperator).append(crlf);
		}
		File outputFile = null;
		try {
			String outputRoot = System.getenv("outputRoot");
			outputRoot = StringUtils.isEmpty(outputRoot) ? "./work" : outputRoot;
			outputFile = Paths.get(outputRoot, "dll", table.namePhi + ".sql").normalize().toFile();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (outputFile != null) {
			System.out.println("[INFO]save file to :" + outputFile.getAbsolutePath());
			// org.apache.commons.io.FileUtils.write(outputFile, outLine.toString(),
			// "UTF-8", false);
			org.apache.commons.io.FileUtils.write(outputFile, outLine.toString(), "Shift-JIS", false);
		}
	}

	private List<TableObject> parse(File excelFile) throws IOException {
		List<TableObject> tableList = Lists.newArrayList();
		if (!excelFile.exists()) {
			System.out.println("[INFO][File NOT Exist]" + excelFile.getCanonicalPath());
			return null;
		}
		if (!excelFile.canRead()) {
			System.out.println("[INFO][File Can NOT read]" + excelFile.getCanonicalPath());
			return null;
		}
		if (excelFile.isDirectory()) {
			for (File f : excelFile.listFiles()) {
				List<TableObject> tbl = parse(f);
				if (tbl != null) {
					tableList.addAll(tbl);
				}
			}
			return tableList;
		}
		Workbook book = WorkbookFactory.create(excelFile);
		String fileName = excelFile.getName();
		int sheetsize = book.getNumberOfSheets();
		for (int i = 0; i < sheetsize; i++) {
			Sheet sheet = book.getSheetAt(i);
			if (!isTarget(sheet)) {
				continue;
			}
			String sheetName = sheet.getSheetName();

			if (indexObj.tableNamePhiColumnIndexDefault == -1
					&& indexObj.tableNamePhiColumnIndex.get(sheetName) == null) {
				System.out.println(String.format("[ERROR][Tabel Name ColumnIndex IS Empty]fileName=%s sheetName=%s",
						fileName, sheetName));
				continue;
			}
			if (indexObj.tableNamePhiRowIndexDefault == -1 && indexObj.tableNamePhiRowIndex.get(sheetName) == null) {
				System.out.println(String.format("[ERROR][Tabel Name RowIndex IS Empty]fileName=%s sheetName=%s",
						fileName, sheetName));
				continue;
			}

			TableObject table = parseTable(sheet, sheetName);
			if (table != null) {
				if (StringUtils.isEmpty(table.namePhi)) {
					System.out.println(String.format("[ERROR][Tabel NamePhi IS Empty]fileName=%s sheetName=%s",
							fileName, sheetName));
					continue;
				}
				if (StringUtils.isEmpty(table.nameLogi)) {
					System.out.println(String.format("[ERROR][Tabel NameLogi IS Empty]fileName=%s sheetName=%s",
							fileName, sheetName));
					continue;
				}
				tableList.add(table);
			}
		}

		return tableList;
	}

	private TableObject parseTable(Sheet sheet, String sheetName) {
		TableObject table = new TableObject();

		Integer rowIndex = indexObj.tableNamePhiRowIndex.get(sheetName);
		if (rowIndex == null) {
			rowIndex = indexObj.tableNamePhiRowIndexDefault;
		}
		Integer columnIndex = indexObj.tableNamePhiColumnIndex.get(sheetName);
		if (columnIndex == null) {
			columnIndex = indexObj.tableNamePhiColumnIndexDefault;
		}
		table.namePhi = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIndex - 1, columnIndex);

		rowIndex = indexObj.tableNameLogiRowIndex.get(sheetName);
		if (rowIndex == null) {
			rowIndex = indexObj.tableNameLogiRowIndexDefault;
		}
		columnIndex = indexObj.tableNameLogiColumnIndex.get(sheetName);
		if (columnIndex == null) {
			columnIndex = indexObj.tableNameLogiColumnIndexDefault;
		}
		table.nameLogi = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIndex - 1, columnIndex).toUpperCase();

		rowIndex = indexObj.startRowIndex.get(sheetName);
		if (rowIndex == null) {
			rowIndex = indexObj.startRowIndexDefault;
		}
		int rowIdx = rowIndex - 1;
		while (true) {
			rowIdx++;
			ColumnObject colum = new ColumnObject();
			Integer colIndx = indexObj.columnPhiColumnIndex.get(sheetName);
			if (colIndx == null) {
				colIndx = indexObj.columnPhiColumnIndexDefault;
			}
			colum.namePhi = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIdx, colIndx).toUpperCase();
			if (StringUtils.isEmpty(colum.namePhi)) {
				break;
			}

			colIndx = indexObj.columnLogiColumnIndex.get(sheetName);
			if (colIndx == null) {
				colIndx = indexObj.columnLogiColumnIndexDefault;
			}
			colum.nameLogi = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIdx, colIndx);

			colIndx = indexObj.domainColumnIndex.get(sheetName);
			if (colIndx == null) {
				colIndx = indexObj.domainColumnIndexDefault;
			}
			if (colIndx != -1) {
				colum.domain = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIdx, colIndx);
			}

			colIndx = indexObj.kuFlagColumnIndex.get(sheetName);
			if (colIndx == null) {
				colIndx = indexObj.kuFlagColumnIndexDefault;
			}
			if (colIndx != -1) {
				String val = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIdx, colIndx);
				colum.kuFlag = val.equals("○") ? true : false;
			}

			colIndx = indexObj.pkFlagColumnIndex.get(sheetName);
			if (colIndx == null) {
				colIndx = indexObj.pkFlagColumnIndexDefault;
			}
			if (colIndx != -1) {
				String val = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIdx, colIndx);
				colum.pkFlag = val.equals("○") ? true : false;
			}

			colIndx = indexObj.fkFlagColumnIndex.get(sheetName);
			if (colIndx == null) {
				colIndx = indexObj.fkFlagColumnIndexDefault;
			}
			if (colIndx != -1) {
				String val = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIdx, colIndx);
				colum.fkFlag = val.equals("○") ? true : false;
			}

			colIndx = indexObj.akFlagColumnIndex.get(sheetName);
			if (colIndx == null) {
				colIndx = indexObj.akFlagColumnIndexDefault;
			}
			if (colIndx != -1) {
				String val = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIdx, colIndx);
				colum.akFlag = val.equals("○") ? true : false;
			}

			colIndx = indexObj.ieFlagColumnIndex.get(sheetName);
			if (colIndx == null) {
				colIndx = indexObj.ieFlagColumnIndexDefault;
			}
			if (colIndx != -1) {
				String val = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIdx, colIndx);
				colum.ieFlag = val.equals("○") ? true : false;
			}

			colIndx = indexObj.notNullFlagColumnIndex.get(sheetName);
			if (colIndx == null) {
				colIndx = indexObj.notNullFlagColumnIndexDefault;
			}
			if (colIndx != -1) {
				String val = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIdx, colIndx);
				colum.notNullFlag = val.equals("○") || val.equals("○") ? true : false;
			}

			colIndx = indexObj.dataTypeColumnIndex.get(sheetName);
			if (colIndx == null) {
				colIndx = indexObj.dataTypeColumnIndexDefault;
			}
			if (colIndx != -1) {
				colum.dataType = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIdx, colIndx);
			}

			colIndx = indexObj.sizeColumnIndex.get(sheetName);
			if (colIndx == null) {
				colIndx = indexObj.sizeColumnIndexDefault;
			}
			if (colIndx != -1) {
				colum.size = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIdx, colIndx);
			}

			colIndx = indexObj.defaultValueColumnIndex.get(sheetName);
			if (colIndx == null) {
				colIndx = indexObj.defaultValueColumnIndexDefault;
			}
			if (colIndx != -1) {
				colum.defaultValue = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIdx, colIndx);
			}

			colIndx = indexObj.despColumnIndex.get(sheetName);
			if (colIndx == null) {
				colIndx = indexObj.despColumnIndexDefault;
			}
			if (colIndx != -1) {
				colum.desp = NieExcelLayoutUtil.getCellValueAsString(sheet, rowIdx, colIndx);
			}

			table.columnList.add(colum);
		}
		return table;
	}

	private boolean isTarget(Sheet sheet) {
		String sheetName = sheet.getSheetName();
		return !indexObj.excludeSheetName.contains(sheetName);
	}

}
