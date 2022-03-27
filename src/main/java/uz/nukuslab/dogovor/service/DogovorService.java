package uz.nukuslab.dogovor.service;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.nukuslab.dogovor.entity.Company;
import uz.nukuslab.dogovor.entity.Dogovor;
import uz.nukuslab.dogovor.entity.User;
import uz.nukuslab.dogovor.payload.ApiResponse;
import uz.nukuslab.dogovor.payload.DateDto;
import uz.nukuslab.dogovor.payload.DogovorDto;
import uz.nukuslab.dogovor.repository.CompanyRepository;
import uz.nukuslab.dogovor.repository.DogovorRepository;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DogovorService {

    @Autowired
    DogovorRepository contractRepository;
    @Autowired
    CompanyRepository companyRepository;


    public ApiResponse getAllContract() {
        List<Dogovor> contracts = contractRepository.findAll();
        return new ApiResponse("All contracts", true, contracts);
    }


    public ApiResponse getContract(Long id) {
        Optional<Dogovor> optionalContract = contractRepository.findById(id);
        if (optionalContract.isPresent()) {
            Dogovor contract = optionalContract.get();
            oneContract(contract);
            return new ApiResponse("One contract", true, contract);
        }
        return new ApiResponse("Bunday id li dogovor bazada tabilmadi!!!", false);
    }

    private void oneContract(Dogovor dogovor) {
        String DEST = "src/main/resources/OneContract.pdf";

        String FONT_FILENAME = "src/main/resources/arial.ttf";
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(DEST));
            document.open();

            BaseFont bfComic = BaseFont.createFont("src/main/resources/arial.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(bfComic, 11, Font.BOLD);
            Font fontCity = new Font(bfComic, 11, Font.NORMAL);
            Font fontTxt = new Font(bfComic, 9, Font.NORMAL);
            Font fontTema = new Font(bfComic, 9, Font.BOLD);

            Paragraph paragraph = new Paragraph("ДОГОВОР № " + dogovor.getId(), fontTitle);
            paragraph.setAlignment(Element.ALIGN_CENTER);

            document.add(paragraph);

            document.add(new Paragraph("\n"));

            Table table = new Table(2);
            table.setBorder(Rectangle.NO_BORDER);

            Paragraph paragraph1 = new Paragraph("г. Нукус", fontCity);
            Cell cell = new Cell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.add(paragraph1);

            table.addCell(cell);

            Paragraph paragraph2 = new Paragraph(dogovor.getCreatedAt().toString() + " г", fontCity);
            paragraph2.setAlignment(Element.ALIGN_CENTER);

            Cell cell2 = new Cell();
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.add(paragraph2);

            table.addCell(cell2);

            document.add(table);

            document.add(new Paragraph("\n"));

            Paragraph paragraph3 = new Paragraph(dogovor.getCompany().getName() + ", именуемый в дальнейшем \"Заказчик\", в лице " + dogovor.getUser().getFirstName() + " " + dogovor.getUser().getLastName() + ", действующего на основании Устава, с одной стороны, и \n" +
                    "ООО «TEXNOPOS IT MEKTEBI», именуемое в дальнейшем \"Исполнитель\", в лице директора Калабаев Ш., действующей на основании Устава, с другой стороны, заключили настоящий Договор о нижеследующем:", fontTxt);
            paragraph3.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(paragraph3);


            Paragraph paragraph4 = new Paragraph("1.\tПредмет Договора ", fontTema);
            paragraph4.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph4);


            Paragraph paragraph5 = new Paragraph("1.1. Заказчик поручает, оплачивает и принимает, а Исполнитель    изготавливает, загружает и передаёт продукцию заказчику согласно нижеследующей спецификации." +
                    "1.2.Спецификация:", fontTxt);
            document.add(paragraph5);

            Table table1 = new Table(5);

            Cell cell1 = new Cell();

            Paragraph paragraph6 = new Paragraph("Наименование продукции", fontTema);
            table1.addCell(paragraph6);

            Paragraph paragraph7 = new Paragraph("Ед. изм.", fontTema);
            paragraph7.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph7);

            Paragraph paragraph8 = new Paragraph("Кол-во", fontTema);
            paragraph8.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph8);

            Paragraph paragraph9 = new Paragraph("Цена за единицу", fontTema);
            paragraph9.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph9);

            Paragraph paragraph10 = new Paragraph("Сумма без НДС", fontTema);
            paragraph10.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph10);

            Paragraph paragraph11 = new Paragraph("Создание web сайта", fontTxt);
            paragraph11.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph11);

            Paragraph paragraph12 = new Paragraph("услуга", fontTxt);
            paragraph12.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph12);

            Paragraph paragraph13 = new Paragraph("1", fontTxt);
            paragraph13.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph13);

            Paragraph paragraph14 = new Paragraph(String.valueOf(dogovor.getPrice()), fontTxt);
            paragraph14.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph14);

            Paragraph paragraph15 = new Paragraph(String.valueOf(dogovor.getPrice()), fontTxt);
            paragraph15.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph15);

            Paragraph paragraph16 = new Paragraph("Итого", fontTema);
            table1.addCell(paragraph16);

            Paragraph paragraph17 = new Paragraph("", fontTxt);
            paragraph17.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph17);

            Paragraph paragraph18 = new Paragraph("", fontTxt);
            paragraph18.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph18);

            Paragraph paragraph19 = new Paragraph("", fontTxt);
            paragraph19.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph19);

            Paragraph paragraph20 = new Paragraph(String.valueOf(dogovor.getPrice()), fontTxt);
            paragraph20.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph20);

            document.add(table1);

            document.add(new Paragraph("\n"));

            Paragraph paragraph21 = new Paragraph("Общая сумма договора: 15.000.000 (Пятнадцать миллионов) сум без НДС.", fontTxt);
            document.add(paragraph21);

            Paragraph paragraph22 = new Paragraph("2. Сроки и порядок оплаты,  отгрузка продукции ", fontTema);
            paragraph22.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph22);

            Paragraph paragraph23 = new Paragraph("2.1. Заказчик после подписания Договора перечисляет Исполнителю на расчетный счет, в порядке предварительной предоплаты,  сумму в размере  30 % договорной стоимости в течение 7-и банковских дней со дня подписания договора. ", fontTxt);
            document.add(paragraph23);

            Paragraph paragraph24 = new Paragraph("2.2. Отгрузка продукции производится в течение 15 рабочих  дней с момента поступления 70 % предоплаты.   ", fontTxt);
            document.add(paragraph24);

            Paragraph paragraph25 = new Paragraph("3. Ответственность сторон", fontTema);
            paragraph25.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph25);

            Paragraph paragraph26 = new Paragraph("3.1. Ответственность Сторон в иных случаях определяется в соответствии с действующим законодательством.", fontTxt);
            document.add(paragraph26);

            Paragraph paragraph27 = new Paragraph("3.2. В случае возникновения разногласий все вопросы решаются путем двусторонних мирных переговоров, а при невозможности прийти   к согласию – в Хозяйственном суде по месту нахождения Исполнителя.", fontTxt);
            document.add(paragraph27);

            Paragraph paragraph28 = new Paragraph("4. Срок действия Договора ", fontTema);
            paragraph28.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph28);

            Paragraph paragraph29 = new Paragraph("4.1. После подписания настоящего договора, все предыдущие письменные и устные соглашения, переписка, договоренности между сторонами, касающиеся настоящего договора, теряют юридическую силу.", fontTxt);
            document.add(paragraph29);

            Paragraph paragraph30 = new Paragraph("4.2. Все изменения и дополнения к настоящему договору либо иная договоренность между Заказчиком и Исполнителем, влекущая за собой новые обстоятельства, которые не вытекают из Договора, должны быть оформлены сторонами в форме дополнительных соглашений или изменений к Договору и закреплены печатями и подписями уполномоченными лицами сторон.", fontTxt);
            document.add(paragraph30);

            Paragraph paragraph31 = new Paragraph("4.3. Во всех остальных случаях, не предусмотренных настоящим Договором, применяются нормы действующего законодательства Республики Узбекистан.", fontTxt);
            document.add(paragraph31);

            Paragraph paragraph32 = new Paragraph("4.4. Договор вступает в силу со дня подписания сторонами и действует до 31.12.2021 г.", fontTxt);
            document.add(paragraph32);

            Paragraph paragraph33 = new Paragraph("4.5. Договор заключен в двух экземплярах, имеющих одинаковую юридическую силу.", fontTxt);
            document.add(paragraph33);


            Paragraph paragraph34 = new Paragraph("5. Адреса и банковские реквизиты Сторон:", fontTema);
            paragraph34.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph34);

            ////////////////////////

            Table table2 = new Table(2);
            table2.setBorder(Rectangle.NO_BORDER);

            Paragraph paragraph35 = new Paragraph("Заказчик:", fontTema);
            Cell cell3 = new Cell();
            cell3.setBorder(Rectangle.NO_BORDER);
            cell3.add(paragraph35);

            table2.addCell(cell3);

            Paragraph paragraph36 = new Paragraph("Исполнитель:", fontTema);
            paragraph36.setAlignment(Element.ALIGN_CENTER);

            Cell cell4 = new Cell();
            cell4.setBorder(Rectangle.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell4.add(paragraph36);

            table2.addCell(cell4);

            Paragraph paragraph37 = new Paragraph("" + dogovor.getCompany().getName() + "\n" +
                    "" + dogovor.getCompany().getAddress().getCity() + " " + dogovor.getCompany().getAddress().getDistrict() + "" + dogovor.getCompany().getAddress().getNumber() + "\n" +
                    "" + dogovor.getCompany().getProp().getAccountNumber() + "\n" +
                    "" + dogovor.getCompany().getProp().getBankName() + "\n" +
                    "МФО " + dogovor.getCompany().getProp().getMfo() + "\n" +
                    "ИНН " + dogovor.getCompany().getProp().getInn() + "\n" +
                    " ________" + dogovor.getCompany().getDirectorName(), fontTxt);
            paragraph37.setAlignment(Element.ALIGN_CENTER);


            Paragraph paragraph42 = new Paragraph("М.П.", fontTxt);
            paragraph42.setAlignment(Element.ALIGN_LEFT);

            Cell cell5 = new Cell();
            cell5.setBorder(Rectangle.NO_BORDER);
            cell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell5.add(paragraph37);
            cell5.add(new Paragraph("\n"));
            cell5.add(paragraph42);

            table2.addCell(cell5);

            Paragraph paragraph38 = new Paragraph("ООО “TEXNOPOS IT MEKTEBI”", fontTema);
            paragraph38.setAlignment(Element.ALIGN_CENTER);

            Paragraph paragraph39 = new Paragraph("РК, Нукус, Гарезсизлик кошеси 80/4", fontTxt);
            paragraph39.setAlignment(Element.ALIGN_CENTER);

            Paragraph paragraph40 = new Paragraph("Телефон: +99890 095 71 17  +99891 302 12 26\n" +
                    "Р/С 2020 8000 2052 9342 0001\n" +
                    "Bank  Aloqabank Qoraqalpogʻiston filiali \n" +
                    "МФО 00623\n" +
                    "ИНН 307832859\n", fontTxt);
            paragraph40.setAlignment(Element.ALIGN_CENTER);

            Paragraph paragraph41 = new Paragraph("___________Калабаев Ш", fontTema);
            paragraph41.setAlignment(Element.ALIGN_CENTER);

            Paragraph paragraph43 = new Paragraph("М.П.", fontTxt);
            paragraph43.setAlignment(Element.ALIGN_RIGHT);

            Cell cell6 = new Cell();
            cell6.setBorder(Rectangle.NO_BORDER);
            cell6.setHorizontalAlignment(Element.ALIGN_RIGHT);

            cell6.add(paragraph38);
            cell6.add(paragraph39);
            cell6.add(paragraph40);
            cell6.add(paragraph41);
            cell6.add(new Paragraph("\n"));
            cell6.add(paragraph43);

            table2.addCell(cell6);

            document.add(table2);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ApiResponse addContract(DogovorDto dogovorDto) {
        Dogovor contract = new Dogovor();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        contract.setUser(user);

        Optional<Company> optionalCompany = companyRepository.findById(dogovorDto.getCompanyId());
        if (!optionalCompany.isPresent()) {
            return new ApiResponse("Bunday company bazada tabilmadi!!!", false);
        }
        Company company = optionalCompany.get();

        contract.setCompany(company);

        contract.setPrice(dogovorDto.getPrice());

        contractRepository.save(contract);

        return new ApiResponse("Dogovor saqlandi!", true);
    }

    public ApiResponse editContract(Long id, DogovorDto dogovorDto) {
        Optional<Dogovor> optionalContract = contractRepository.findById(id);
        if (!optionalContract.isPresent()) {
            return new ApiResponse("Bunday id li dogovor bazada tabilmadi!!!", false);
        }
        Dogovor contract = optionalContract.get();
        contract.setPrice(dogovorDto.getPrice());

        Optional<Company> optionalCompany = companyRepository.findById(dogovorDto.getCompanyId());
        if (!optionalCompany.isPresent()) {
            return new ApiResponse("Bunday id li company bazada tabilmadi!!", false);
        }
        Company company = optionalCompany.get();

        contract.setCompany(company);
//        Timestamp timestamp = new Timestamp(2022);
//        timestamp.setYear(2022);
//        timestamp.setMonth(1);
//        timestamp.setNanos(1);
//        contract.setCreatedAt(timestamp);

        contractRepository.save(contract);

        return new ApiResponse("Contract updated", true);
    }

    public ApiResponse deleteContract(Long id) {
        try {
            contractRepository.deleteById(id);
            return new ApiResponse("Contract deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error!!!", false);
        }
    }

    public ApiResponse byAdmin(Integer id) {
        List<Dogovor> dogovorList = contractRepository.findByUserId(id);
        return new ApiResponse("Dogovor list by admin", true, dogovorList);
    }

    public ApiResponse byDate(DateDto dateDto) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse(dateDto.getStart());
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        int nanos = timestamp.getDate();
        System.out.println(nanos);
        Date date2 = dateFormat.parse(dateDto.getEnd());
        long time2 = date2.getTime();
        Timestamp timestamp2 = new Timestamp(time2);

        List<Dogovor> dogovorList = contractRepository.findByCreatedAtBetween(timestamp, timestamp2);

        return new ApiResponse("Contracts by date : ", true, dogovorList);
    }

    public ApiResponse reportLastYear() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp back = new Timestamp(System.currentTimeMillis());

        back.setYear(now.getYear() - 1);

        List<Dogovor> dogovorList = contractRepository.findByCreatedAtBetween(back, now);

        return new ApiResponse("Contract last year : ", true, dogovorList);
    }

    public ApiResponse reportLastMonth() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp back = new Timestamp(System.currentTimeMillis());

        back.setMonth(now.getMonth() - 1);

        List<Dogovor> dogovorList = contractRepository.findByCreatedAtBetween(back, now);

        return new ApiResponse("Contract last month : ", true, dogovorList);
    }

    public ApiResponse reportLastDay() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp back = new Timestamp(System.currentTimeMillis());

        back.setDate(now.getDate() - 1);

        List<Dogovor> dogovorList = contractRepository.findByCreatedAtBetween(back, now);

        return new ApiResponse("Contract last Day : ", true, dogovorList);
    }

    public ApiResponse exportWord(List<Dogovor> dogovorList) throws IOException {
        XWPFDocument document = new XWPFDocument();
        //Paragraf
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setBold(true);
        run.setText("Dogovor Report");

        //table
        XWPFTable table = document.createTable();

        XWPFTableRow row = table.getRow(0);
        XWPFTableCell cell = row.getCell(0);

        cell.setText("ID");
        row.createCell().setText("Company Name");
        row.createCell().setText("User");
        row.createCell().setText("Price");
        row.createCell().setText("Created At");

        for (Dogovor dogovor : dogovorList) {
            row = table.createRow();
            row.getCell(0).setText(dogovor.getId().toString());
            row.getCell(1).setText(dogovor.getCompany().getName());
            row.getCell(2).setText(dogovor.getUser().getFirstName());
            row.getCell(3).setText(String.valueOf(dogovor.getPrice()));
            row.getCell(4).setText(dogovor.getCreatedAt().toString());
        }

        FileOutputStream outputStream = new FileOutputStream(new File("src/main/resources/report.docx"));

        document.write(outputStream);
        document.close();
        return new ApiResponse("Success", true);
    }

    public ApiResponse exportPdf(List<Dogovor> dogovorList) {
        String DEST = "src/main/resources/report.pdf";

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(DEST));
            document.open();

            BaseFont bfComic = BaseFont.createFont("src/main/resources/arial.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(bfComic, 11, Font.BOLD);
            Font fontTxt = new Font(bfComic, 9, Font.NORMAL);

            Paragraph paragraph = new Paragraph("Contracts reports", fontTitle);
            paragraph.setAlignment(Element.ALIGN_CENTER);

            document.add(paragraph);

            document.add(new Paragraph("\n"));

            Table table1 = new Table(5);

            Cell cell1 = new Cell();

            Paragraph paragraph6 = new Paragraph("ID", fontTitle);
            paragraph6.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph6);

            Paragraph paragraph7 = new Paragraph("Company name", fontTitle);
            paragraph7.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph7);

            Paragraph paragraph8 = new Paragraph("User", fontTitle);
            paragraph8.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph8);

            Paragraph paragraph9 = new Paragraph("Price", fontTitle);
            paragraph9.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph9);

            Paragraph paragraph10 = new Paragraph("Created at", fontTitle);
            paragraph10.setAlignment(Element.ALIGN_CENTER);
            table1.addCell(paragraph10);

            for (Dogovor dogovor : dogovorList) {
                Paragraph paragraph11 = new Paragraph(dogovor.getId().toString(), fontTxt);
                paragraph11.setAlignment(Element.ALIGN_CENTER);
                table1.addCell(paragraph11);

                Paragraph paragraph12 = new Paragraph(dogovor.getCompany().getName(), fontTxt);
                paragraph12.setAlignment(Element.ALIGN_CENTER);
                table1.addCell(paragraph12);

                Paragraph paragraph13 = new Paragraph(dogovor.getUser().getFirstName(), fontTxt);
                paragraph13.setAlignment(Element.ALIGN_CENTER);
                table1.addCell(paragraph13);

                Paragraph paragraph14 = new Paragraph(String.valueOf(dogovor.getPrice()), fontTxt);
                paragraph14.setAlignment(Element.ALIGN_CENTER);
                table1.addCell(paragraph14);

                Paragraph paragraph15 = new Paragraph(dogovor.getCreatedAt().toString(), fontTxt);
                paragraph15.setAlignment(Element.ALIGN_CENTER);
                table1.addCell(paragraph15);
            }

            document.add(table1);

            document.close();
            return new ApiResponse("Success", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ApiResponse("Error", false);
    }

    public ApiResponse exportExcel(List<Dogovor> dogovors) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        FileOutputStream outputStream = new FileOutputStream(new File("src/main/resources/report.xlsx"));

        XSSFSheet sheet = workbook.createSheet("Report sheet");

        XSSFRow row = sheet.createRow(1);

        XSSFCell cell = row.createCell(3);
        cell.setCellValue("Contracts reports");

        CellStyle styleTitle = workbook.createCellStyle();
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);

        org.apache.poi.ss.usermodel.Font fontTitle = workbook.createFont();
        fontTitle.setBold(true);
        fontTitle.setFontName("Times New Roman");
        fontTitle.setFontHeightInPoints((short) 11);

        styleTitle.setFont(fontTitle);

        CellStyle styleBorder = workbook.createCellStyle();
        styleBorder.setBorderRight(BorderStyle.THIN);
        styleBorder.setBorderLeft(BorderStyle.THIN);
        styleBorder.setBorderTop(BorderStyle.THIN);
        styleBorder.setBorderBottom(BorderStyle.THIN);

        styleBorder.setAlignment(HorizontalAlignment.CENTER);
        styleBorder.setVerticalAlignment(VerticalAlignment.CENTER);

        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 11);

        styleBorder.setFont(font);

        cell.setCellStyle(styleTitle);

        XSSFRow row1 = sheet.createRow(3);

        XSSFCell cellId = row1.createCell(1);
        cellId.setCellValue("#");
        cellId.setCellStyle(styleBorder);

        // Row width
        sheet.setColumnWidth(1, 2000);
        sheet.setColumnWidth(2, 8000);
        sheet.setColumnWidth(3, 8000);
        sheet.setColumnWidth(4, 6000);
        sheet.setColumnWidth(5, 6000);

        XSSFCell cellCName = row1.createCell(2);
        cellCName.setCellValue("Company name");
        cellCName.setCellStyle(styleBorder);

        XSSFCell cellUser = row1.createCell(3);
        cellUser.setCellValue("User");
        cellUser.setCellStyle(styleBorder);

        XSSFCell cellPrice = row1.createCell(4);
        cellPrice.setCellValue("Price");
        cellPrice.setCellStyle(styleBorder);

        XSSFCell cellDate = row1.createCell(5);
        cellDate.setCellValue("Created at");
        cellDate.setCellStyle(styleBorder);

        //Style body
        CellStyle styleBody = workbook.createCellStyle();

        styleBody.setBorderRight(BorderStyle.THIN);
        styleBody.setBorderLeft(BorderStyle.THIN);
        styleBody.setBorderTop(BorderStyle.THIN);
        styleBody.setBorderBottom(BorderStyle.THIN);

        styleBody.setAlignment(HorizontalAlignment.LEFT);
        styleBody.setVerticalAlignment(VerticalAlignment.CENTER);

        org.apache.poi.ss.usermodel.Font fontBody = workbook.createFont();
        fontBody.setBold(false);
        fontBody.setFontName("Times New Roman");
        fontBody.setFontHeightInPoints((short) 10);

        styleBody.setFont(fontBody);

        //Style for roght text
        CellStyle styleR = workbook.createCellStyle();

        styleR.setBorderRight(BorderStyle.THIN);
        styleR.setBorderLeft(BorderStyle.THIN);
        styleR.setBorderTop(BorderStyle.THIN);
        styleR.setBorderBottom(BorderStyle.THIN);

        styleR.setAlignment(HorizontalAlignment.RIGHT);
        styleR.setVerticalAlignment(VerticalAlignment.CENTER);

        org.apache.poi.ss.usermodel.Font fontR = workbook.createFont();
        fontR.setBold(false);
        fontR.setFontName("Times New Roman");
        fontR.setFontHeightInPoints((short) 10);

        styleR.setFont(fontR);

        double jami = 0;

        for (int i = 0; i < dogovors.size(); i++) {
            jami = jami + dogovors.get(i).getPrice();
            row1 = sheet.createRow(i + 4);
            XSSFCell cellIdVal = row1.createCell(1);
            cellIdVal.setCellValue(i + 1);
            cellIdVal.setCellStyle(styleBody);

            XSSFCell cellCNameVal = row1.createCell(2);
            cellCNameVal.setCellValue(dogovors.get(i).getCompany().getName());
            cellCNameVal.setCellStyle(styleBody);

            XSSFCell cellUserVal = row1.createCell(3);
            cellUserVal.setCellValue(dogovors.get(i).getUser().getFirstName());
            cellUserVal.setCellStyle(styleBody);

            XSSFCell cellPriceVal = row1.createCell(4);
            cellPriceVal.setCellValue(dogovors.get(i).getPrice());
            cellPriceVal.setCellStyle(styleR);

            XSSFCell cellDateVal = row1.createCell(5);
            cellDateVal.setCellValue(dogovors.get(i).getCreatedAt().toString().substring(0, 10));
            cellDateVal.setCellStyle(styleR);
        }

        row1 = sheet.createRow(dogovors.size() + 4);

        XSSFCell cellIdJ = row1.createCell(1);
        cellIdJ.setCellValue("");
        cellIdJ.setCellStyle(styleBorder);

        XSSFCell cellIJami = row1.createCell(2);
        cellIJami.setCellValue("Ja'mi:");
        cellIJami.setCellStyle(styleBorder);

        XSSFCell cellJamiVal = row1.createCell(3);
        sheet.addMergedRegion(new CellRangeAddress(dogovors.size() + 4, dogovors.size() + 4, 3, 5));
        cellJamiVal.setCellValue(jami);
        cellJamiVal.setCellStyle(styleBorder);

        XSSFCell cellUserVal = row1.createCell(4);
        cellUserVal.setCellStyle(styleBody);
        XSSFCell cellPriceVal = row1.createCell(5);
        cellPriceVal.setCellStyle(styleBody);

        workbook.write(outputStream);
        workbook.close();
        return new ApiResponse("Success", true);
    }


}
