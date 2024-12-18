package com.example.demo.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.demo.Request.*;
import com.example.demo.Service.CustomerService;
import com.example.demo.Service.EmailService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DTO.GoogleUserDTO;
import com.example.demo.Entity.Account;
import com.example.demo.Entity.Cart;
import com.example.demo.Entity.Customer;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.CustomerRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerService customerService;


 

    public AuthController(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    // @GetMapping("/google/login-success")
    // public ModelAndView handleGoogleLogin(@AuthenticationPrincipal OAuth2User oauthUser, HttpSession session) {
    //     String email = oauthUser.getAttribute("email");
    //     String name = oauthUser.getAttribute("name");
    
    //     // Check if the Google account already exists
    //     Optional<Account> existingAccount = accountRepository.findByUsername(email);
    
    //     if (existingAccount.isPresent()) {
    //         Account account = existingAccount.get();
            
    //         // If the account is not linked to Google, return an error page
    //         if (!account.getIsgoogleaccount()) {
    //             return new ModelAndView("errorPage");  // Error page for non-Google linked account
    //         }
    
    //         // Check if the account is verified (optional, similar to your standard login)
    //         if (!account.getIsverified()) {
    //             return new ModelAndView("errorPage");  // Account not verified
    //         }
    
    //         // Set customer ID in session
    //         session.setAttribute("customerID", account.getCustomer().getCustomerID());
    
    //         // Retrieve the customer's cart based on their customer ID
    //         Customer customer = account.getCustomer();
    //         Cart cart = cartRepository.findByCustomer_CustomerID(customer.getCustomerID()).orElse(null);
    
    //         // Log customer information for debugging purposes
    //         System.out.println("Customer ID: " + account.getCustomer().getCustomerID());
    //         System.out.println("Cart: " + cart);
    
    //         // Return the user to the base page after successful login
    //         return new ModelAndView("redirect:/base.html");
    //     } else {
    //         // If the Google account does not exist, create a new account and customer
    
    //         // Create a new account
    //         Account newAccount = new Account();
    //         newAccount.setUsername(email);
    //         newAccount.setIsgoogleaccount(true);  // Mark the account as Google-linked
    //         newAccount.setRole(2);
    //         newAccount.setIsverified(true);  // Default role as customer (1 for customer)
    //         accountRepository.save(newAccount);
    
    //         // Create a new customer
    //         Customer newCustomer = new Customer();
    //         newCustomer.setEmail(email);
    //         newCustomer.setCustomername(name);
    //         newCustomer.setAccount(newAccount);
    //         customerRepository.save(newCustomer);

    //         System.out.println("Đang lưu tài khoản mới: " + newAccount.getUsername());
            

    
    //         // Set customer ID in session
    //         session.setAttribute("customerID", newCustomer.getCustomerID());
    
          
    
    //         // Return the user to the base page after creating the account
    //         return new ModelAndView("redirect:/base.html");
    //     }
    // }
    
    
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginResquest loginRequest) {
        // Kiểm tra thông tin đăng nhập
        Optional<Account> accountOpt = accountRepository.findByUsername(loginRequest.getUsername());


        // Kiểm tra mật khẩu
        if (!accountOpt.isPresent() || !PasswordUtil.checkPassword(loginRequest.getPassword(), accountOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tên đăng nhập hoặc mật khẩu.");
        }


        Account account = accountOpt.get();
        if (!account.getIsverified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tài khoản chưa được xác minh. Vui lòng kiểm tra email.");
        }

        // Lấy thông tin khách hàng dựa trên tài khoản
        Customer customer = customerRepository.findByAccount(account);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khách hàng không tồn tại.");
        }

        // Lấy giỏ hàng của khách hàng
        Cart cart = cartRepository.findByCustomer_CustomerID(customer.getCustomerID())
                                    .orElse(null);

        // Tạo LoginResponse để trả về thông tin người dùng và giỏ hàng
        LoginResponse loginResponse = new LoginResponse(customer.getCustomerID(), account.getUsername(), account.getRole(), cart);

        // Trả về LoginResponse
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registrationRequest) {
        try {
            // Check if username already exists
            if (accountRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                     .body(new RegisterResponse("Tên đăng nhập đã tồn tại."));
            }
    
            // Check if email already exists
            if (customerRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                     .body(new RegisterResponse("Email đã tồn tại."));
            }
    
            // Encode password
            String hashedPassword = PasswordUtil.hashPassword(registrationRequest.getPassword());
    
            // Create Account and Customer entities
            Account account = new Account();
            account.setUsername(registrationRequest.getUsername());
            account.setPassword(hashedPassword);
            account.setRole(2);
            account.setIsgoogleaccount(false);
            account.setIsverified(false); // Tài khoản chưa được xác minh
// Default to customer role
    
            // Save Account
            accountRepository.save(account);
    
            Customer customer = new Customer();
         
            customer.setEmail(registrationRequest.getEmail());
            customer.setAccount(account);

            // Save Customer
            customerRepository.save(customer);

            String verificationLink = "http://localhost:8080/api/auth/verify?email=" + registrationRequest.getUsername();

            // Gửi email xác minh
            emailService.sendVerificationEmail(registrationRequest.getEmail(), verificationLink);

            return ResponseEntity.ok(new RegisterResponse("Đăng ký thành công. Vui lòng kiểm tra email để xác minh tài khoản."));
    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new RegisterResponse("Đăng ký thất bại. Đã xảy ra lỗi máy chủ."));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("email") String email) {
        Optional<Account> accountOpt = accountRepository.findByUsername(email);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setIsverified(true); // Đánh dấu tài khoản đã xác minh
            accountRepository.save(account);

            // Trả về HTML với card thông báo
            String redirectHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>Xác minh tài khoản</title>
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    font-family: Arial, sans-serif;
                    background-color: #f4f4f9;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    height: 100vh;
                }
                .card {
                    background: #ffffff;
                    border-radius: 8px;
                    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                    width: 400px;
                    max-width: 90%;
                    text-align: center;
                    padding: 20px;
                }
                .card h1 {
                    color: #4CAF50;
                    font-size: 24px;
                    margin-bottom: 10px;
                }
                .card p {
                    color: #555;
                    font-size: 16px;
                    margin-bottom: 20px;
                }
                .progress-bar {
                    width: 100%;
                    background-color: #f3f3f3;
                    height: 10px;
                    border-radius: 5px;
                    overflow: hidden;
                }
                .progress-bar div {
                    width: 0%;
                    height: 100%;
                    background-color: #4CAF50;
                    transition: width 0.3s ease;
                }
            </style>
            <script>
                let progress = 0;
                const interval = setInterval(() => {
                    progress += 10;
                    document.getElementById('progress-bar-fill').style.width = progress + '%';
                    if (progress >= 100) {
                        clearInterval(interval);
                        window.location.href = '/login'; // Chuyển hướng về trang đăng nhập
                    }
                }, 300); // Tăng tiến độ mỗi 300ms
            </script>
        </head>
        <body>
            <div class="card">
                <h1>Xác minh thành công!</h1>
                <p>Tài khoản của bạn đã được xác minh. Bạn sẽ được chuyển đến trang đăng nhập trong giây lát.</p>
                <div class="progress-bar">
                    <div id="progress-bar-fill"></div>
                </div>
            </div>
        </body>
        </html>
        """;

            return ResponseEntity.ok().body(redirectHtml);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>Lỗi</title>
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    font-family: Arial, sans-serif;
                    background-color: #f4f4f9;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    height: 100vh;
                }
                .card {
                    background: #ffffff;
                    border-radius: 8px;
                    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                    width: 400px;
                    max-width: 90%;
                    text-align: center;
                    padding: 20px;
                }
                .card h1 {
                    color: #f44336;
                    font-size: 24px;
                    margin-bottom: 10px;
                }
                .card p {
                    color: #555;
                    font-size: 16px;
                }
            </style>
        </head>
        <body>
            <div class="card">
                <h1>Lỗi xác minh</h1>
                <p>Tài khoản không tồn tại hoặc đã được xác minh trước đó.</p>
            </div>
        </body>
        </html>
        """);
        }
    }


    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> payload) {
        try {
            int customerId = Integer.parseInt(payload.get("customerId"));
            String oldPassword = payload.get("oldPassword");
            String newPassword = payload.get("newPassword");
            String confirmPassword = payload.get("confirmPassword");

            if (!newPassword.equals(confirmPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mật khẩu mới và xác nhận mật khẩu không khớp.");
            }

            Customer customer = customerRepository.findById(Integer.valueOf(payload.get("customerId")))
                    .orElseThrow(() -> new RuntimeException("Khách hàng không tìm thấy"));

            // Lấy mật khẩu đã mã hóa từ cơ sở dữ liệu
            String encodedOldPassword = customer.getAccount().getPassword();

            // So sánh mật khẩu cũ người dùng nhập vào với mật khẩu đã mã hóa
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(oldPassword, encodedOldPassword)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mật khẩu cũ không chính xác.");
            }

            // Mã hóa mật khẩu mới
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            customer.getAccount().setPassword(encodedNewPassword);
            accountRepository.save(customer.getAccount());

            return ResponseEntity.ok("Mật khẩu đã được thay đổi.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi: " + e.getMessage());
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> payload) {
        Map<String, String> response = new HashMap<>();
        try {
            String email = payload.get("email");

            Optional<Customer> customerOpt = customerRepository.findByEmail(email);
            if (!customerOpt.isPresent()) {
                response.put("message", "Email không tồn tại trong hệ thống.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Customer customer = customerOpt.get();
            Account account = customer.getAccount();

            String newPassword = PasswordUtil.generateRandomPassword(8);
            String hashedPassword = new BCryptPasswordEncoder().encode(newPassword);
            account.setPassword(hashedPassword);
            accountRepository.save(account);

            // Gửi email mật khẩu mới
            emailService.sendPasswordResetEmail(email, newPassword);

            response.put("message", "Mật khẩu mới đã được gửi tới email của bạn.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Đã xảy ra lỗi trong quá trình xử lý: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }








}
