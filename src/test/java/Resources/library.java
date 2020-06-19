package Resources;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class library {
    public static RequestSpecification res;

    public static RequestSpecification requestspecification() throws IOException {
        /*
        This will set the Base URI and create session
         */
        if (res == null) {
            PrintStream log = new PrintStream(new FileOutputStream("Logging.txt"));
            res = new RequestSpecBuilder().setBaseUri(GlobalValue("baseUrl"))
                    .addHeader("Content-Type", "application/json")
                    .addFilter(RequestLoggingFilter.logRequestTo(log)).addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .addHeader("Cookie", "JSESSIONID=" + createSession()).build();

            return res;
        }
        return res;
    }

    public static String createSession() throws IOException {
        String response = given().header("Content-Type", "application/json")
                .body("{ \"username\": \"" + GlobalValue("username") + "\", \"password\": \"" + GlobalValue("password") + "\" }")
                .when().post("/rest/auth/1/session").asString();
        JsonPath js = new JsonPath(response);
        String jsessionid = js.get("session.value");
        //System.out.println(jsessionid);
        return jsessionid;

    }

    public static String GlobalValue(String key) throws IOException {
        /*
            To get value from property file
         */
        Properties pro = new Properties();
        FileInputStream fis = new FileInputStream("src/test/java/Resources/global.properties");
        pro.load(fis);
        String property_value = pro.getProperty(key);
        return property_value;
    }

    public static void writevalue() throws IOException, ConfigurationException {
        /*
            To update project key and name to property file to make it unique
         */

        //get the existing property value
        String projkey = GlobalValue("projectKey");
        String projname = GlobalValue("projectname");

        //split the value and take the value after 0
        String updatedkey_number = StringSplit_number(projkey);
        String updatedname_number = StringSplit_number(projname);

        //split the value and take the value before 0
        String updatedkey_string = StringSplit_String(projkey);
        String updatedname_string = StringSplit_String(projname);

        //update the property file
        PropertiesConfiguration p;
        p = new PropertiesConfiguration("src/test/java/Resources/global.properties");
        p.setProperty("projectKey", updatedkey_string + "Z" + updatedkey_number);
        p.setProperty("projectname", updatedname_string + "Z" + updatedname_number);
        p.save();

    }

    public static String StringSplit_number(String s) {
        int n2;
        String s1 = s.split("Z")[1];
        int n = Integer.parseInt(s1);
        n2 = n + 1;
        String s2 = Integer.toString(n2);
        return s2;
    }

    public static String StringSplit_String(String s) {
        String s1 = s.split("Z")[0];
        return s1;
    }

    public static ArrayList<String> DataDriven(String SheetName, String testcasename) throws IOException {
        ArrayList<String> datalist = new ArrayList<String>();
        FileInputStream fis = new FileInputStream("src/test/java/Resources/TestData.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        int sheet_Number = workbook.getNumberOfSheets();

        for (int i = 0; i < sheet_Number; i++) {
            String sheetName = workbook.getSheetName(i);
            if (sheetName.equalsIgnoreCase(SheetName)) {
                XSSFSheet sheet = workbook.getSheetAt(i);
                Iterator<Row> row = sheet.iterator();
                Row firstRow = row.next();
                Iterator<Cell> cell = firstRow.cellIterator();
                int TC_ColumnNumber = 0;
                int k = 0;
                while (cell.hasNext()) {
                    Cell cellValue = cell.next();
                    if (cellValue.getStringCellValue().equalsIgnoreCase("Scenario")) {
                        TC_ColumnNumber = k;
                        break;
                    }
                    k++;
                }
                while (row.hasNext()) {
                    Row r = row.next();
                    if (r.getCell(TC_ColumnNumber).getStringCellValue().equalsIgnoreCase(testcasename)) {
                        Iterator<Cell> cellI = r.cellIterator();
                        while (cellI.hasNext()) {
                            Cell c = cellI.next();
                            if (c.getCellType() == CellType.STRING) {
                                datalist.add(c.getStringCellValue());
                            } else {
                                datalist.add(NumberToTextConverter.toText(c.getNumericCellValue()));
                            }

                        }
                    }

                }

            }
        }
        return datalist;
    }
}
