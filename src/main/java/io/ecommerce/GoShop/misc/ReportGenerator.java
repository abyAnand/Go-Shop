package io.ecommerce.GoShop.misc;

import io.ecommerce.GoShop.model.Order;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.model.Variant;
import io.ecommerce.GoShop.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

@Service
public class ReportGenerator {
    @Autowired
    OrderService orderHistoryService;


    public String generateOrderHistoryPdf(List<Order> orderHistories, String from, String to) throws DocumentException {
        String fileName = UUID.randomUUID().toString();

        // Define the directory to save the PDF file
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/uploads/reports/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Generate the file path
        String filePath = uploadDir + fileName + ".pdf";

        Document document = new Document(PageSize.A1);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Create the title
            Paragraph title = new Paragraph("Order History Report");
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            Paragraph period = new Paragraph("From "+from+" to "+to);
            period.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(period);

            document.add(new Paragraph("\n"));

            // Create the table
            PdfPTable table = new PdfPTable(6); // Adjust the number of columns as per your requirement

            // Add table headers
            table.addCell("ID");
            table.addCell("Date");
            table.addCell("Total");
            table.addCell("Size");
            table.addCell("Status");
            table.addCell("Type");

            // Add table rows
            for (Order orderHistory : orderHistories) {
                table.addCell(orderHistory.getId().toString());
                table.addCell(orderHistory.getCreatedDate().toString());
                table.addCell(String.valueOf(orderHistory.getTotal()));
                table.addCell(String.valueOf(orderHistory.getSize()));
                table.addCell(String.valueOf(orderHistory.getStatus()));
                table.addCell(String.valueOf(orderHistory.getPayment()));
            }

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return filePath;
    }



    public String generateOrderHistoryCsv(List<Order> orderHistoryList) {
        // Generate a unique file name for the CSV file
        String fileName = UUID.randomUUID().toString();

        // Define the directory to save the CSV file
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/uploads/reports/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Generate the full file path
        String filePath = uploadDir + fileName + ".csv";

        try (PrintWriter writer = new PrintWriter(filePath)) {
            // Write the CSV header
            writer.println("Order ID,Date,Total,Size, Status ,Order Type");

            // Iterate over the list of OrderHistory objects and write each record to the CSV file
            for (Order order : orderHistoryList) {
                writer.printf("%s,%s,%.2f,%d,%s,%s",
                        order.getId().toString(),
                        order.getCreatedDate().toString(),
                        order.getTotal(),
                        order.getSize(),
                        order.getStatus().toString(),
                        order.getPayment().toString());
            }

            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filePath;
    }


    public String generateStockReportPdf(List<Variant> variants) {
        String fileName = UUID.randomUUID().toString();

        // Define the directory to save the PDF file
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/uploads/reports/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Generate the file path
        String filePath = uploadDir + fileName + ".pdf";

        Document document = new Document(PageSize.A1);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Create the title
            Paragraph title = new Paragraph("Stock Report");
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);


            document.add(new Paragraph("\n"));

            // Create the table
            PdfPTable table = new PdfPTable(8); // Adjust the number of columns as per your requirement

            // Add table headers
            table.addCell("Variant ID");
            table.addCell("Variant Name");
            table.addCell("Product ID");
            table.addCell("Product Name");
            table.addCell("Price");
            table.addCell("Selling Price");
            table.addCell("Wholesale Price");
            table.addCell("Stock");

            // Add table rows
            for (Variant variant : variants) {
                table.addCell(variant.getId().toString());
                table.addCell(variant.getVariantName());
                table.addCell(variant.getProduct().getId().toString());
                table.addCell(String.valueOf(variant.getProduct().getProductName()));
                table.addCell(String.valueOf(variant.getPrice()));
                table.addCell(String.valueOf(variant.getOfferPrice()));
                table.addCell(String.valueOf(variant.getWholesalePrice()));
                table.addCell(String.valueOf(variant.getStock()));
            }

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        System.out.println("Stock report generated for "+variants.size()+" variants");
        return filePath;
    }

    public String generateUserReportPdf(List<User> users) {

        String fileName = UUID.randomUUID().toString();

        // Define the directory to save the PDF file
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/uploads/reports/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Generate the file path
        String filePath = uploadDir + fileName + ".pdf";

        Document document = new Document(PageSize.A1);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Create the title
            Paragraph title = new Paragraph("User Report - "+ users.size()+" users");
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);


            document.add(new Paragraph("\n"));

            // Create the table
            PdfPTable table = new PdfPTable(10); // Adjust the number of columns as per your requirement

            // Add table headers
            table.addCell("#");
            table.addCell("User ID");
            table.addCell("username");
            table.addCell("Name");
            table.addCell("Email");
            table.addCell("Phone");
            table.addCell("Enabled");
            table.addCell("Verified");
            table.addCell("Wallet");
            table.addCell("Orders");

            // Add table rows
            int i = 1;
            for (User user : users) {
                table.addCell(String.valueOf(i));
                table.addCell(user.getId().toString());
                table.addCell(user.getUsername());
                table.addCell(user.getFirstName() + " " + user.getLastName());
                table.addCell(user.getEmail());
                table.addCell(Long.toString(user.getPhoneNumber()));
                table.addCell(String.valueOf(user.isEnabled()));
                table.addCell(String.valueOf(user.isEnabled()));
                String balance = user.getWallet() == null ? "0" :Double.toString(user.getWallet().getBalance());
                table.addCell(balance);
                table.addCell(String.valueOf(user.getOrders().size()));
                i++;
            }
            document.add(table);

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        System.out.println("User report generated for "+users.size()+" users");
        return filePath;
    }

}