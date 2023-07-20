package io.ecommerce.GoShop.controller.app;

import com.itextpdf.text.DocumentException;
import io.ecommerce.GoShop.misc.ReportGenerator;
import io.ecommerce.GoShop.model.Category;
import io.ecommerce.GoShop.model.Order;
import io.ecommerce.GoShop.model.Payment;
import io.ecommerce.GoShop.model.Status;
import io.ecommerce.GoShop.service.order.OrderService;
import io.grpc.netty.shaded.io.netty.handler.codec.DateFormatter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class dashboard {

    @Autowired
    OrderService orderService;

    @Autowired
    ReportGenerator reportGenerator;

    @GetMapping
    public String getDashboard(){
        return "app-admin/dashboard";
    }

    @GetMapping("/admin")
// @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin(Model model, HttpServletRequest request,
                        @RequestParam(name = "filter", required = false, defaultValue = "") String filter,
                        @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                        @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//    if (username.equals("anonymousUser")) {
//        saveAnonUserDetails.save(request.getSession().getId(), request.getRemoteAddr());
//    }

        String period;

        switch (filter) {
            case "week" -> {
                period = "week";
                // Get the starting date of the current week
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                startDate = calendar.getTime();
                // Get today's date
                endDate = new Date();
            }
            case "month" -> {
                period = "month";
                // Get the starting date of the current month
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDate = calendar.getTime();
                // Get today's date
                endDate = new Date();
            }
            case "day" -> {
                period = "day";
                // Get today's date
                LocalDate today = LocalDate.now();
                // Set the start date to 12:00:00 AM
                LocalDateTime startDateTime = today.atStartOfDay();
                // Set the end date to 11:59:59 PM
                LocalDateTime endDateTime = today.atTime(23, 59, 59);

                // Convert to Date objects
                ZoneId zone = ZoneId.systemDefault();
                startDate = Date.from(startDateTime.atZone(zone).toInstant());
                endDate = Date.from(endDateTime.atZone(zone).toInstant());
            }

            case "year" -> {
                period = "year";
                // Get the starting date of the current year
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                startDate = calendar.getTime();
                // Get today's date
                endDate = new Date();
            }
            default -> {
                // Default case: filter
                period = "Custom Date Range";
                System.out.println("order search");
                //for testing
                // Get the starting date of the current week
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                startDate = calendar.getTime();
                // Get today's date
                endDate = new Date();
            }
        }

        List<Order> orders = orderService.findOrdersByDate(startDate, endDate);

        model.addAttribute("numberOfOrders", orders.size());



        long revenue = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())  // Flatten the stream of order items
                .mapToLong(orderItem -> (long) orderItem.getVariant().getWholesalePrice() * orderItem.getQuantity())  // Calculate subtotal for each order item
                .sum();

        double orderTotalWithRetailPrice = orders.stream()
                .mapToDouble(Order::getTotal)
                .sum();




        model.addAttribute("revenue", revenue);

//        Float taxSum = orders.stream()
//                .map(OrderHistory::getTax)
//                .reduce(0F, Float::sum);
//
//        model.addAttribute("taxSum", taxSum);

        AtomicReference<Float> totalProfit = new AtomicReference<>(0F);
        orders.forEach(order -> {
            order.getOrderItems().forEach(item -> {
                totalProfit.updateAndGet(value -> value + item.getVariant().getOfferPrice() - item.getVariant().getWholesalePrice());
            });
        });

        model.addAttribute("totalProfit", totalProfit);

        Map<Category,Integer> catCount = new HashMap<>();
        orders.forEach(order ->{
            order.getOrderItems().forEach(item ->{
                Category category = item.getVariant().getProduct().getCategory();
                catCount.put(category, catCount.getOrDefault(category, 0) + 1);
            });
        });

        model.addAttribute("categoryCount", catCount);

        Map<Date, Float> revenueMap = new HashMap<>();

        orders.forEach(order -> {
            Date date = order.getCreatedDate();
            Calendar calendar = Calendar.getInstance();

            // Set the Date object to be modified
            calendar.setTime(date);

            // Set the desired time component
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 00);
            calendar.set(Calendar.MILLISECOND, 00);

            // Get the modified Date object
            Date modifiedDate = calendar.getTime();
            order.setCreatedDate(new Timestamp(modifiedDate.getTime()));

            revenueMap.put(order.getCreatedDate(), revenueMap.getOrDefault(order.getCreatedDate(), 0F)+order.getTotal());
        });

        model.addAttribute("revenueMap",revenueMap);

        Map<Status, Long> orderStatusCounts = orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));

        model.addAttribute("orderStatusCounts", orderStatusCounts);

        Map<Payment, Long> orderTypeCounts = orders.stream()
                .collect(Collectors.groupingBy(Order::getPayment, Collectors.counting()));

        model.addAttribute("orderTypeCounts", orderTypeCounts);

        long couponsUsed = orders.stream()
                .filter(order -> order.getCoupon() != null)
                .count();

        model.addAttribute("couponsUsed", couponsUsed);

        int totalItemCount = orders.stream()
                .mapToInt(order -> order.getOrderItems().size())
                .sum();

        model.addAttribute("totalItemCount", totalItemCount);

        List<Order> orderList =  orderService.findAll();



        //Recent 5 transactions
        model.addAttribute("lastFiveOrders", orderList
                .stream()
                .sorted(Comparator.comparing(Order::getCreatedDate)
                        .reversed())
                .limit(5)
                .collect(Collectors.toList()));

        model.addAttribute("range", "From " + DateFormatter.format(startDate) + " to " + DateFormatter.format(endDate));
        model.addAttribute("period", period);
        //    model.addAttribute("orders", orders);
        return "app-admin/dashboard";
    }

    @PostMapping("/generateReport")
    //  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> salesReportGenerator(@RequestBody Map<String, Object> requestData ) throws ParseException, IOException, DocumentException, DocumentException {
        String report = (String) requestData.get("report");
        String type = (String) requestData.get("type");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse((String) requestData.get("from"));
        Date toDate = dateFormat.parse((String) requestData.get("to"));

        String generatedFile="";
        switch (report){
            case "orders" ->{
                List<Order> orders = orderService.findOrdersByDate(fromDate, toDate);
                if(type.equals("csv")){
                    generatedFile = reportGenerator.generateOrderHistoryCsv(orders);
                }else {
                    generatedFile = reportGenerator.generateOrderHistoryPdf(orders, (String) requestData.get("from"), (String) requestData.get("to"));
                }
            }
        }
        File requestedFile = new File(generatedFile);
        ByteArrayResource resource = new ByteArrayResource(FileUtils.readFileToByteArray(requestedFile));
        HttpHeaders headers = new HttpHeaders();

        if(type.equals("csv")){
            headers.setContentType(MediaType.parseMediaType("text/csv"));
        }else{
            headers.setContentType(MediaType.APPLICATION_PDF);
        }
        headers.setContentDispositionFormData("attachment", generatedFile);
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);

    }
}
