package uz.nukuslab.dogovor.service;

import com.lowagie.text.Document;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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

    private final
    DogovorRepository contractRepository;
    private final
    CompanyRepository companyRepository;

    public DogovorService(DogovorRepository contractRepository, CompanyRepository companyRepository) {
        this.contractRepository = contractRepository;
        this.companyRepository = companyRepository;
    }

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
            table1.setWidths(new int[]{250, 150, 100, 200, 200});
            table1.setPadding(5);

            Cell cellOne = new Cell();
            cellOne.setVerticalAlignment(com.lowagie.text.alignment.VerticalAlignment.CENTER);
            cellOne.setHorizontalAlignment(com.lowagie.text.alignment.HorizontalAlignment.CENTER);

            Paragraph paragraph6 = new Paragraph("Наименование продукции", fontTema);
            paragraph6.setAlignment(Element.ALIGN_CENTER);
            cellOne.add(paragraph6);
            table1.addCell(cellOne);

            Cell cellTwo = new Cell();
            cellTwo.setVerticalAlignment(com.lowagie.text.alignment.VerticalAlignment.CENTER);
            cellTwo.setHorizontalAlignment(com.lowagie.text.alignment.HorizontalAlignment.CENTER);

            Paragraph paragraph7 = new Paragraph("Ед. изм.", fontTema);
            paragraph7.setAlignment(Element.ALIGN_CENTER);
            cellTwo.add(paragraph7);
            table1.addCell(cellTwo);

            Cell cellThree = new Cell();
            cellThree.setVerticalAlignment(com.lowagie.text.alignment.VerticalAlignment.CENTER);
            cellThree.setHorizontalAlignment(com.lowagie.text.alignment.HorizontalAlignment.CENTER);

            Paragraph paragraph8 = new Paragraph("Кол-во", fontTema);
            paragraph8.setAlignment(Element.ALIGN_CENTER);
            cellThree.add(paragraph8);
            table1.addCell(cellThree);

            Cell cellFour = new Cell();
            cellFour.setVerticalAlignment(com.lowagie.text.alignment.VerticalAlignment.CENTER);
            cellFour.setHorizontalAlignment(com.lowagie.text.alignment.HorizontalAlignment.CENTER);

            Paragraph paragraph9 = new Paragraph("Цена за единицу", fontTema);
            paragraph9.setAlignment(Element.ALIGN_CENTER);
            cellFour.add(paragraph9);
            table1.addCell(cellFour);

            Cell cellFive = new Cell();
            cellFive.setVerticalAlignment(com.lowagie.text.alignment.VerticalAlignment.CENTER);
            cellFive.setHorizontalAlignment(com.lowagie.text.alignment.HorizontalAlignment.CENTER);

            Paragraph paragraph10 = new Paragraph("Сумма без НДС", fontTema);
            paragraph10.setAlignment(Element.ALIGN_CENTER);
            cellFive.add(paragraph10);
            table1.addCell(cellFive);

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
        FileOutputStream outputStream = new FileOutputStream(new File("src/main/resources/report.docx"));

        XWPFDocument document = new XWPFDocument();

        XWPFParagraph pTitle = document.createParagraph();
        pTitle.setAlignment(ParagraphAlignment.CENTER);
        pTitle.setVerticalAlignment(TextAlignment.CENTER);
        XWPFRun runTitle = pTitle.createRun();
        runTitle.setText("Report Contracts");
        runTitle.setFontFamily("Times New Roman");
        runTitle.setBold(true);
        runTitle.setFontSize(11);

        XWPFTable table = document.createTable();
        table.setWidth("100%");

        XWPFTableRow row = table.getRow(0);

        //Id column
        XWPFTableCell cell = row.getCell(0);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

        XWPFParagraph paragraph = cell.addParagraph();
        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setBold(true);
        run.setText("Id");
        run.setFontSize(11);
        paragraph.setAlignment(ParagraphAlignment.CENTER);

        table.getRow(0).getCell(0).removeParagraph(0);

        //Company name column
        XWPFTableCell cell2 = row.createCell();
        cell2.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        cell2.setWidth("2500");
        XWPFParagraph paragraph2 = cell2.addParagraph();
        XWPFRun run2 = paragraph2.createRun();
        run2.setFontFamily("Times New Roman");
        run2.setBold(true);
        run2.setText("Company name");
        run2.setFontSize(11);
        paragraph2.setAlignment(ParagraphAlignment.CENTER);

        table.getRow(0).getCell(1).removeParagraph(0);

        //User column
        XWPFTableCell cell3 = row.createCell();
        cell3.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        cell3.setWidth("3000");
        XWPFParagraph paragraph3 = cell3.addParagraph();
        XWPFRun run3 = paragraph3.createRun();
        run3.setFontFamily("Times New Roman");
        run3.setBold(true);
        run3.setText("User");
        run3.setFontSize(11);
        paragraph3.setAlignment(ParagraphAlignment.CENTER);

        table.getRow(0).getCell(2).removeParagraph(0);

        //Price column
        XWPFTableCell cell4 = row.createCell();
        cell4.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        cell4.setWidth("2000");
        XWPFParagraph paragraph4 = cell4.addParagraph();
        XWPFRun run4 = paragraph4.createRun();
        run4.setFontFamily("Times New Roman");
        run4.setBold(true);
        run4.setText("Price");
        run4.setFontSize(11);
        paragraph4.setAlignment(ParagraphAlignment.CENTER);

        table.getRow(0).getCell(3).removeParagraph(0);

        //Price column
        XWPFTableCell cell5 = row.createCell();
        cell5.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        XWPFParagraph paragraph5 = cell5.addParagraph();
        XWPFRun run5 = paragraph5.createRun();
        run5.setFontFamily("Times New Roman");
        run5.setBold(true);
        run5.setText("Created at");
        run5.setFontSize(11);
        paragraph5.setAlignment(ParagraphAlignment.CENTER);

        table.getRow(0).getCell(4).removeParagraph(0);

        double jamiSumma = 0;
        for (int i = 0; i < dogovorList.size(); i++) {
            jamiSumma = jamiSumma + dogovorList.get(i).getPrice();
            XWPFTableRow rowData = table.createRow();
            XWPFTableCell cellId;
            cellId = rowData.getCell(0);
            cellId.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            XWPFParagraph paragraphId = cellId.addParagraph();
            XWPFRun runId = paragraphId.createRun();
            runId.setFontFamily("Times New Roman");
            runId.setBold(false);
            runId.setText(String.valueOf(i + 1));
            runId.setFontSize(11);
            paragraphId.setAlignment(ParagraphAlignment.CENTER);

            XWPFTableCell cellCName = rowData.getCell(1);

            cellCName.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            XWPFParagraph paragraphCName = cellCName.addParagraph();
            XWPFRun runCName = paragraphCName.createRun();
            runCName.setFontFamily("Times New Roman");
            runCName.setBold(false);
            runCName.setText(dogovorList.get(i).getCompany().getName());
            runCName.setFontSize(11);
            paragraphCName.setAlignment(ParagraphAlignment.CENTER);

            XWPFTableCell cellUser = rowData.getCell(2);

            cellUser.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            XWPFParagraph paragraphUser = cellUser.addParagraph();
            XWPFRun runUser = paragraphUser.createRun();
            runUser.setFontFamily("Times New Roman");
            runUser.setBold(false);
            runUser.setText(String.valueOf(dogovorList.get(i).getUser().getFirstName()));
            runUser.setFontSize(11);
            paragraphUser.setAlignment(ParagraphAlignment.CENTER);

            XWPFTableCell cellPrice = rowData.getCell(3);

            cellPrice.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            XWPFParagraph paragraphPrice = cellPrice.addParagraph();
            XWPFRun runPrice = paragraphPrice.createRun();
            runPrice.setFontFamily("Times New Roman");
            runPrice.setBold(false);
            runPrice.setText(String.valueOf(dogovorList.get(i).getPrice()));
            runPrice.setFontSize(11);
            paragraphPrice.setAlignment(ParagraphAlignment.CENTER);

            XWPFTableCell cellDate = rowData.getCell(4);
            cellDate.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            XWPFParagraph paragraphDate = cellDate.addParagraph();
            XWPFRun runDate = paragraphDate.createRun();
            runDate.setFontFamily("Times New Roman");
            runDate.setBold(false);
            runDate.setText(String.valueOf(dogovorList.get(i).getCreatedAt()));
            runDate.setFontSize(11);
            paragraphDate.setAlignment(ParagraphAlignment.CENTER);
        }

        XWPFParagraph enter = document.createParagraph();
        XWPFRun enterRun = enter.createRun();
        enterRun.setText("\n");

        XWPFTable table1 = document.createTable();
        table1.setWidth("100%");
        table1.removeBorders();

        XWPFTableRow rowJami = table1.getRow(0);

        XWPFTableCell cellJami = rowJami.getCell(0);
        XWPFParagraph paragraphJami = cellJami.addParagraph();
        XWPFRun runJami = paragraphJami.createRun();
        runJami.setText("Jami : ");
        runJami.setBold(true);
        runJami.setFontFamily("Times New Roman");
        runJami.setFontSize(11);
        paragraphJami.setAlignment(ParagraphAlignment.LEFT);

        XWPFTableCell cellJamiVal = rowJami.createCell();
        cellJamiVal.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

        XWPFParagraph paragraphJamiVal = cellJamiVal.addParagraph();
        XWPFRun runJamiVal = paragraphJamiVal.createRun();
        runJamiVal.setText(String.valueOf(jamiSumma));
        runJamiVal.setBold(true);
        runJami.setFontFamily("Times New Roman");
        runJami.setFontSize(11);
        paragraphJamiVal.setAlignment(ParagraphAlignment.RIGHT);

        document.write(outputStream);
        document.close();
        return new ApiResponse("Success", true);
    }

    public ApiResponse exportPdf(List<Dogovor> dogovorList) {
        String DEST = "src/main/resources/report.pdf";

        String FONT_FILENAME = "src/main/resources/arial.ttf";
        com.lowagie.text.Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(DEST));
            document.open();

            BaseFont bfComic = BaseFont.createFont("src/main/resources/arial.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(bfComic, 11, Font.BOLD);
            Font fontCity = new Font(bfComic, 11, Font.NORMAL);
            Font fontTxt = new Font(bfComic, 9, Font.NORMAL);
            Font fontTema = new Font(bfComic, 9, Font.BOLD);

            com.lowagie.text.Paragraph paragraph = new com.lowagie.text.Paragraph("Contracts report", fontTitle);
            paragraph.setAlignment(Element.ALIGN_CENTER);

            document.add(paragraph);

            document.add(new com.lowagie.text.Paragraph("\n"));

            ////////////////////////

            Table table1 = new Table(5);
            table1.setWidths(new int[]{50, 250, 250, 200, 150});
            table1.setPadding(5);

            Cell cell1 = new Cell();
            cell1.setVerticalAlignment(com.lowagie.text.alignment.VerticalAlignment.CENTER);
            cell1.setHorizontalAlignment(com.lowagie.text.alignment.HorizontalAlignment.CENTER);
//            cell1.setWidth("10%");
            Paragraph paragraphId = new Paragraph("№", fontTema);
            paragraphId.setAlignment(Element.ALIGN_CENTER);
            cell1.add(paragraphId);
            table1.addCell(cell1);

            Cell cell2 = new Cell();
            cell2.setHorizontalAlignment(com.lowagie.text.alignment.HorizontalAlignment.CENTER);
            cell2.setVerticalAlignment(com.lowagie.text.alignment.VerticalAlignment.CENTER);
            Paragraph paragraphCName = new Paragraph("Company name", fontTema);
            paragraphCName.setAlignment(Element.ALIGN_CENTER);
            cell2.add(paragraphCName);
            table1.addCell(cell2);

            Cell cell3 = new Cell();
            cell3.setHorizontalAlignment(com.lowagie.text.alignment.HorizontalAlignment.CENTER);
            cell3.setVerticalAlignment(com.lowagie.text.alignment.VerticalAlignment.CENTER);
            Paragraph paragraphUser = new Paragraph("User", fontTema);
            paragraphUser.setAlignment(Element.ALIGN_CENTER);
            cell3.add(paragraphUser);
            table1.addCell(cell3);

            Cell cell4 = new Cell();
            cell4.setHorizontalAlignment(com.lowagie.text.alignment.HorizontalAlignment.CENTER);
            cell4.setVerticalAlignment(com.lowagie.text.alignment.VerticalAlignment.CENTER);
            Paragraph paragraphPrice = new Paragraph("Price", fontTema);
            paragraphPrice.setAlignment(Element.ALIGN_CENTER);
            cell4.add(paragraphPrice);
            table1.addCell(cell4);

            Cell cell5 = new Cell();
            cell5.setHorizontalAlignment(com.lowagie.text.alignment.HorizontalAlignment.CENTER);
            cell5.setVerticalAlignment(com.lowagie.text.alignment.VerticalAlignment.CENTER);
            Paragraph paragraphDate = new Paragraph("Created at", fontTema);
            paragraphDate.setAlignment(Element.ALIGN_CENTER);
            cell5.add(paragraphDate);
            table1.addCell(cell5);

            double jami = 0;

            for (int i = 0; i < dogovorList.size(); i++) {
                jami = jami + dogovorList.get(i).getPrice();
                Paragraph paragraphIdVal = new Paragraph(String.valueOf(i + 1), fontTxt);
                table1.addCell(paragraphIdVal);

                Paragraph paragraphCNameVal = new Paragraph(dogovorList.get(i).getCompany().getName(), fontTxt);
                table1.addCell(paragraphCNameVal);

                Paragraph paragraphUserVal = new Paragraph(dogovorList.get(i).getUser().getFirstName(), fontTxt);
                table1.addCell(paragraphUserVal);

                Paragraph paragraphPriceVal = new Paragraph(String.valueOf(dogovorList.get(i).getPrice()), fontTxt);
                table1.addCell(paragraphPriceVal);

                Paragraph paragraphDateVal = new Paragraph(dogovorList.get(i).getCreatedAt().toString(), fontTxt);
                table1.addCell(paragraphDateVal);
            }

            //////////////////////////////////////
            Paragraph pEnter = new Paragraph("\n");

            Table table2 = new Table(2);
            table2.setWidths(new int[]{450,450});
            table2.setPadding(5);
            table2.setBorder(Rectangle.NO_BORDER);

            Cell cellJami = new Cell();
            cellJami.setBorder(Rectangle.NO_BORDER);
            cellJami.setVerticalAlignment(com.lowagie.text.alignment.VerticalAlignment.CENTER);
            cellJami.setHorizontalAlignment(com.lowagie.text.alignment.HorizontalAlignment.LEFT);
//            cell1.setWidth("10%");
            Paragraph paragraphJami = new Paragraph("Ja'mi : ", fontTema);
            paragraphJami.setAlignment(Element.ALIGN_LEFT);
            cellJami.add(paragraphJami);
            table2.addCell(cellJami);

            Cell cellJamiVal = new Cell();
            cellJamiVal.setBorder(Rectangle.NO_BORDER);
            cellJamiVal.setVerticalAlignment(com.lowagie.text.alignment.VerticalAlignment.CENTER);
            cellJamiVal.setHorizontalAlignment(com.lowagie.text.alignment.HorizontalAlignment.RIGHT);
//            cell1.setWidth("10%");
            Paragraph paragraphJamiVal = new Paragraph(String.valueOf(jami), fontTema);
            paragraphJamiVal.setAlignment(Element.ALIGN_RIGHT);
            cellJamiVal.add(paragraphJamiVal);
            table2.addCell(cellJamiVal);

            document.add(table1);
            document.add(table2);

            ////////////////////////

            document.close();
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
