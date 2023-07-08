package io.ecommerce.GoShop.razorPay;

import com.razorpay.RazorpayException;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.user.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    UserService userService;

    @Value("${razorpay.keyId}")
    private String keyId;

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody String total, Model model, HttpServletRequest request) {
        User user = userService.findByUsername(getCurrentUsername()).orElse(null);

        float theTotal = Float.parseFloat(total);

        int total2 = (int) theTotal;
        try {
            // Call the Razorpay service to create an order
            String orderId = razorpayService.createOrder(total2, "INR");

            // Store the orderId and total in the session or model for further processing
            HttpSession session = request.getSession();
            session.setAttribute("orderId", orderId);
            session.setAttribute("total", total2);


            // Return a success response
            return ResponseEntity.ok(orderId);
        } catch (RazorpayException e) {
            // Handle exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during Razorpay integration");
        }
    }


    @PostMapping("/confirm")
    @ResponseBody
    public ResponseEntity<String> confirmPayment(@RequestBody String orderId, HttpSession session) {
        try {

            int total = Integer.parseInt(session.getAttribute("total").toString());
            System.out.println(total);
            // Assuming payment is successful, you can send a success response with orderId
            JSONObject responseJson = new JSONObject();
            responseJson.put("status", "success");
            responseJson.put("orderId", orderId);
            responseJson.put("amount", total*100);
            responseJson.put("key", keyId);

            return ResponseEntity.ok(responseJson.toString());
        } catch (Exception e) {
            // Handle exception and return error response
            JSONObject errorJson = new JSONObject();
            errorJson.put("status", "error");
            errorJson.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorJson.toString());
        }
    }
}