package com.orangehrm.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtility {

	public static List<String[]> getSheetData(String filePath, String sheetName) {
		// data variable is defined as a list of arrays of string 
		List<String[]> data = new ArrayList<>();
		// Try with resources statements
		try (FileInputStream fis = new FileInputStream(filePath); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
			XSSFSheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new IllegalArgumentException("Sheet" + sheetName + " doesn't exist");
			}

			// iterate through rows
			for (Row row : sheet) {
				if (row.getRowNum() == 0) { // Skip reading columns
					continue;
				}
				// Read all cells in the row
				List<String> rowData = new ArrayList<>();
				for (Cell cell : row) {
					rowData.add(getCellValue(cell));
				}
				// Convert rowData to String[]
				// data.add(rowData.toArray(new String[0]));
				data.add(rowData.toArray(new String[rowData.size()])); // Alternate syntax
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	private static String getCellValue(Cell cell) {
		if (cell == null)
			return "";

		switch (cell.getCellType()) {
		case STRING:
			 return cell.getStringCellValue();

		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getStringCellValue().toString();
			}
			return String.valueOf((int) cell.getNumericCellValue());

		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());

		default:
			return "";

		}

	}

}
