// package com.example.demo.RestController;

// import java.util.Map;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;

// @RestController
// public class GoogleLoginController {

//     private final ClientRegistrationRepository clientRegistrationRepository;

//     public GoogleLoginController(ClientRegistrationRepository clientRegistrationRepository) {
//         this.clientRegistrationRepository = clientRegistrationRepository;
//     }

//     @GetMapping("/login/google")
//     public String loginWithGoogle() {
//         // Trả về URL đăng nhập Google
//         return "redirect:" + clientRegistrationRepository.findByRegistrationId("google").getAuthorizationGrantType();
//     }
// }