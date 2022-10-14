package com.walk_nie.mytool.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class NieExcelLayoutUtil {

	public static String getCellValueAsString(Sheet sheet, int rowIdx, int colIdx) {
		Row row = sheet.getRow(rowIdx);
		if (row == null)
			return "";
		Cell cell = row.getCell(colIdx);
		if (cell == null)
			return "";
		CellType type = cell.getCellType();
		if (type == CellType.NUMERIC) {
			return Double.toString(cell.getNumericCellValue());
		}
		if (type == CellType.STRING) {
			return cell.getStringCellValue();
		}
		if (type == CellType.FORMULA) {
			return getStringFormulaValue(cell);
		}
		if (type == CellType.BLANK) {
			return "";
		}
		if (type == CellType.BOOLEAN) {
			return Boolean.toString(cell.getBooleanCellValue());
		}
		if (type == CellType.ERROR) {
			return Boolean.toString(cell.getBooleanCellValue());
		}

		return "";
	}

	public static String getStringFormulaValue(Cell cell) {

		Workbook book = cell.getSheet().getWorkbook();
		CreationHelper helper = book.getCreationHelper();

		FormulaEvaluator evaluator = helper.createFormulaEvaluator();
		CellValue value = evaluator.evaluate(cell);
		if (value == null)
			return "";
		switch (value.getCellType()) {
		case STRING:
			return value.getStringValue();
		case NUMERIC:
			return Double.toString(value.getNumberValue());
		case BOOLEAN:
			return Boolean.toString(value.getBooleanValue());
		default:
			System.out.println(value.getCellType());
			return null;
		}
	}

	public static int alphebetToInt(String s) {
		if (s == null || "".equals(s) || "-1".equals(s)) {
			return -1;
		}
		int ttl = -1;
		for (int i = 0; i < s.length(); ++i) {
			char ch = s.charAt(i);
			int n = (int) Character.toUpperCase(ch) - (int) 'A' + 1;
			ttl += n + i * 26;
		}
		return ttl;
	}

}
