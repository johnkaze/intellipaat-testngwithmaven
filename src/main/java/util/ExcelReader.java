package util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReader {

    //static String path = System.getProperty("user.dir")+"src\\test\\resources\\excelfiles\\TestData.xlsx";
    //FileInputStream fis;
    //XSSFWorkbook workbook;

    public String path = System.getProperty("user.dir") + "src\\test\\resources\\excelfiles\\TestData.xlsx";

    public FileInputStream fis = null;
    public FileOutputStream fileOut = null;

    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private XSSFRow row = null;
    private XSSFCell cell = null;

    /*public ExcelReader(String path) {
        this.path = path;
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public ArrayList<String> getData(String path, String SheetName, String testName) throws IOException {

        ArrayList<String> a = new ArrayList<>();

        fis = new FileInputStream(path);
        workbook = new XSSFWorkbook(fis);

        int sheets = workbook.getNumberOfSheets();
        for (int i = 0; i < sheets; i++) {
            if (workbook.getSheetName(i).equalsIgnoreCase(SheetName)) {
                sheet = workbook.getSheetAt(i);
                //Identify Testcases coloum by scanning the entire 1st row

                Iterator<Row> rows = sheet.iterator();// sheet is collection of rows
                Row firstrow = rows.next();
                Iterator<Cell> ce = firstrow.cellIterator();//row is collection of cells
                int k = 0;
                int coloumn = 0;
                while (ce.hasNext()) {
                    Cell value = ce.next();

                    if (value.getStringCellValue().equalsIgnoreCase("TestName")) {
                        coloumn = k;

                    }

                    k++;
                }
                //System.out.println(coloumn);

                ////once coloumn is identified then scan entire testcase coloum to identify purcjhase testcase row
                while (rows.hasNext()) {

                    Row r = rows.next();

                    if (r.getCell(coloumn).getStringCellValue().equalsIgnoreCase(testName)) {

                        ////after you grab purchase testcase row = pull all the data of that row and feed into test

                        Iterator<Cell> cv = r.cellIterator();
                        while (cv.hasNext()) {
                            Cell c = cv.next();
                            if (c.getCellTypeEnum() == CellType.STRING) {

                                a.add(c.getStringCellValue());
                            } else {

                                a.add(NumberToTextConverter.toText(c.getNumericCellValue()));

                            }
                        }
                    }


                }


            }
        }
        return a;

    }

    public static void main(String[] args) throws IOException {

        String path = System.getProperty("user.dir") + "\\src\\test\\resources\\excelfiles\\TestData.xlsx";

        ExcelReader excelReader = new ExcelReader();

        List<String> excelData = excelReader.getData(path, "TC_001", "Sanity");

        for (String d : excelData) {
            System.out.println("Data from excel for Smoke Test is :: " + d);
        }

    }


}
