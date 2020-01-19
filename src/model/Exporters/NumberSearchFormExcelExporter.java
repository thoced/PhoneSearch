package model.Exporters;

import com.Vickx.Biblix.Date.DateTime;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.Record;
import model.RecordCollection;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.time.Period;
import java.util.ArrayList;

public class NumberSearchFormExcelExporter extends Service {

    Workbook workbook;
    RecordCollection selectedRecords;
    ArrayList<String> columns;
    String fileName;

    public NumberSearchFormExcelExporter( String fileName, RecordCollection selectedRecords, ArrayList<String> columns) {
        this.selectedRecords = selectedRecords;
        this.fileName = fileName;
        this.columns = columns;
    }

    @Override
    protected Task createTask() {
        return null;
    }

    public Workbook createWorkBookSync(){
        this.createWorkBook();
        return this.workbook;
    }

    private void createWorkBook(){

        workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet("PhoneSearch - Phone numbers results");

        int r = 0;
        Row row = sheet.createRow(r);
        int i=0;
        org.apache.poi.ss.usermodel.Cell headerCell = row.createCell(0);

        boolean numero = this.columns.contains("number"),nom = this.columns.contains("name"), prenom = this.columns.contains("firstName"), dateDeNaissance = this.columns.contains("dateOfBirth"),
                age = this.columns.contains("age"), connu = this.columns.contains("knownFor"), adresse = this.columns.contains("adress"), dateDeConvocation = this.columns.contains("dateDeConvocation"),
                lastOccruence = this.columns.contains("lastOccurence"), source = this.columns.contains("source");

        //region Cellules d'en-tête

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setUnderline(Font.U_SINGLE);
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short)(11));
        headerStyle.setFont(font);
        //row.setHeight((short)50);

        headerCell.setCellStyle(headerStyle);
        headerCell.setCellValue("#");
        headerCell = row.createCell(++i);

        if(numero) {
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Numéro");
            headerCell = row.createCell(++i);
        }
        if(nom) {
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Nom");
            headerCell = row.createCell(++i);
        }
        if(prenom) {
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Prénom");
            headerCell = row.createCell(++i);
        }
        if(dateDeNaissance) {
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Date de naissance");
            headerCell = row.createCell(++i);
        }
        if(age) {
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Âge");
            headerCell = row.createCell(++i);

            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Mineur");
            headerCell = row.createCell(++i);
        }
        if(connu) {
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Connu");
            headerCell = row.createCell(++i);
        }
        if(adresse) {
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Adresse");
            headerCell = row.createCell(++i);
        }
        if(dateDeConvocation) {
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Date de convocation");
            headerCell = row.createCell(++i);
        }
        if(lastOccruence) {
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Dernière occurence");
            headerCell = row.createCell(++i);
        }

        headerCell.setBlank();

        //endregion

        //region Ajout des lignes

        short fontSize = (short) 11;
        String fontName = "calibri";

        for(Record record : selectedRecords){

            i=0;
            row = sheet.createRow(++r);
            org.apache.poi.ss.usermodel.Cell cell = row.createCell(i);

            boolean paire =  r%2 == 0;
            short paireColor = IndexedColors.PALE_BLUE.getIndex();
            short impaireColor = IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex();

            cell.setCellValue(String.valueOf(r));
            CellStyle style;
            if(paire)
                style = this.getStyle(paireColor,false,false, fontSize,fontName,HorizontalAlignment.LEFT);
            else
                style =this.getStyle(impaireColor,false,false, fontSize,fontName, HorizontalAlignment.LEFT);

            cell.setCellStyle(style);

            cell = row.createCell(++i);

            if(numero){
                cell.setCellValue(record.getPhoneNumber().toString());
                if(paire)
                    cell.setCellStyle(this.getStyle(paireColor,false,false, fontSize,fontName,HorizontalAlignment.LEFT));
                else
                    cell.setCellStyle(this.getStyle(impaireColor,false,false, fontSize,fontName,HorizontalAlignment.LEFT));
                cell = row.createCell(++i);
            }
            if(nom){
                cell.setCellValue(record.getIdentity().getName().toUpperCase());
                if(paire)
                    cell.setCellStyle(this.getStyle(paireColor,true,true, fontSize,fontName,HorizontalAlignment.LEFT));
                else
                    cell.setCellStyle(this.getStyle(impaireColor,true,true, fontSize,fontName,HorizontalAlignment.LEFT));

                cell = row.createCell(++i);
            }
            if(prenom){
                cell.setCellValue(record.getIdentity().getFirstName());
                if(paire)
                    cell.setCellStyle(this.getStyle(paireColor,true,false, fontSize,fontName,HorizontalAlignment.LEFT));
                else
                    cell.setCellStyle(this.getStyle(impaireColor,true,false, fontSize,fontName,HorizontalAlignment.LEFT));
                cell = row.createCell(++i);
            }
            if(dateDeNaissance){
                cell.setCellValue(record.getIdentity().getDateDeNaissance().toLocalDateTime().toLocalDate());
                if(paire)
                    cell.setCellStyle(this.getStyleForDate(paireColor,false,false, fontSize,fontName,HorizontalAlignment.CENTER));
                else
                    cell.setCellStyle(this.getStyleForDate(impaireColor,false,false, fontSize,fontName,HorizontalAlignment.CENTER));
                cell = row.createCell(++i);
            }
            if(age){
                Period between = DateTime.between(record.getIdentity().getDateDeNaissance(), DateTime.Now());
                int a = between.getYears();

                cell.setCellValue(a + " ans");
                if(paire)
                    cell.setCellStyle(this.getStyle(paireColor,false,false, fontSize,fontName,HorizontalAlignment.CENTER));
                else
                    cell.setCellStyle(this.getStyle(impaireColor,false,false, fontSize,fontName,HorizontalAlignment.CENTER));

                cell = row.createCell(++i);

                if(a >= 18) {
                    cell.setCellValue("Majeur");
                }
                else {
                    cell.setCellValue("Mineur");
                }

                if(paire)
                    cell.setCellStyle(this.getStyle(paireColor,false,false, fontSize,fontName,HorizontalAlignment.CENTER));
                else
                    cell.setCellStyle(this.getStyle(impaireColor,false,false, fontSize,fontName,HorizontalAlignment.CENTER));
                cell = row.createCell(++i);
            }
            if(connu){
                String value = "";
                String knownFor = record.getIdentity().getKnownFor();

                if(knownFor.contains("C"))
                    value = value.concat("Crimes ");
                if(knownFor.contains("E"))
                    value = value.concat("Ecofin ");
                if(knownFor.contains("M"))
                    value = value.concat("Moeurs ");
                if(knownFor.contains("S"))
                    value = value.concat("Stups ");
                if(knownFor.contains("V"))
                    value = value.concat("Vols");

                cell.setCellValue(value);

                if(paire)
                    cell.setCellStyle(this.getStyle(paireColor,false,false, fontSize,fontName,HorizontalAlignment.CENTER));
                else
                    cell.setCellStyle(this.getStyle(impaireColor,false,false, fontSize,fontName,HorizontalAlignment.CENTER));
                cell = row.createCell(++i);
            }
            if(adresse){
                String value =  record.getIdentity().getStreet() + " " + (record.getIdentity().getNumber()!=0?record.getIdentity().getNumber():" ") + " " +
                                (record.getIdentity().getPostCode()!=0?record.getIdentity().getPostCode():"") + " " + record.getIdentity().getCity();
                if(value.equals(","))
                    value = "";

                cell.setCellValue(value);

                if(paire)
                    cell.setCellStyle(this.getStyle(paireColor,false,false, fontSize,fontName,HorizontalAlignment.CENTER));
                else
                    cell.setCellStyle(this.getStyle(impaireColor,false,false, fontSize,fontName,HorizontalAlignment.CENTER));
                cell = row.createCell(++i);
            }
            if(dateDeConvocation){
                cell.setCellValue("");

                if(paire)
                    cell.setCellStyle(this.getStyleForDateTime(paireColor,false,false, fontSize,fontName,HorizontalAlignment.CENTER));
                else
                    cell.setCellStyle(this.getStyleForDateTime(impaireColor,false,false, fontSize,fontName,HorizontalAlignment.CENTER));
                cell = row.createCell(++i);
            }
            if(lastOccruence){
                String value = record.getLastOccurence().toString();

                if(source)
                    value = value.concat(" (" + record.getSource() + ")");

                cell.setCellValue(value);

                if(paire)
                    cell.setCellStyle(this.getStyle(paireColor,false,false, fontSize,fontName,HorizontalAlignment.LEFT));
                else
                    cell.setCellStyle(this.getStyle(impaireColor,false,false, fontSize,fontName,HorizontalAlignment.LEFT));
                cell = row.createCell(++i);
            }
            cell.setBlank();


        }

        //endregion

        //region taille des colonnes

        sheet.setColumnWidth(0, 4 * 256);
        int colCount = 1;
        if(numero)
            sheet.setColumnWidth(colCount++,15*256);
        if(nom)
            sheet.setColumnWidth(colCount++,20*256);
        if(prenom)
            sheet.setColumnWidth(colCount++,15*256);
        if(dateDeNaissance)
            sheet.setColumnWidth(colCount++,22*256);
        if(age) {
            sheet.setColumnWidth(colCount++, 15 * 256);
            sheet.setColumnWidth(colCount++, 15*256);
        }
        if(connu)
            sheet.setColumnWidth(colCount++, 30*256);
        if(adresse)
            sheet.setColumnWidth(colCount++,40*256);
        if(dateDeConvocation)
            sheet.setColumnWidth(colCount++,22*256);
        if(lastOccruence)
            sheet.setColumnWidth(colCount,25 * 256);

        //endregion

        sheet.setAutoFilter(new CellRangeAddress(0,0,0, columns.size()));
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setSelectedRecords(RecordCollection selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private CellStyle getStyle(short color, boolean bold, boolean underlined, short fontSize, String fontName, HorizontalAlignment alignment){

        CellStyle style = this.workbook.createCellStyle();
        if(color != 0){
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFillForegroundColor(color);
        }
        Font font = workbook.createFont();
        font.setUnderline(underlined?Font.U_SINGLE:Font.U_NONE);
        font.setBold(bold);
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        style.setFont(font);
        style.setAlignment(alignment);

        return style;
    }

    private CellStyle getStyleForDate(short color, boolean bold, boolean underlined, short fontSize, String fontName, HorizontalAlignment alignment){

        CellStyle style = getStyle(color,bold,underlined,fontSize,fontName,alignment);
        CreationHelper creationHelper = workbook.getCreationHelper();
        style.setDataFormat(creationHelper.createDataFormat().getFormat("dd/mm/yy"));

        return style;
    }

    private CellStyle getStyleForDateTime(short color, boolean bold, boolean underlined, short fontSize, String fontName, HorizontalAlignment alignment){

        CellStyle style = getStyle(color,bold,underlined,fontSize,fontName,alignment);
        CreationHelper creationHelper = workbook.getCreationHelper();
        style.setDataFormat(creationHelper.createDataFormat().getFormat("dd/mm/yy hh:mm"));

        return style;
    }
}
