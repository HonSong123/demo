// package com.example.demo.Controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.multipart.MultipartFile;

// import com.example.demo.Service.StorageService;

// @Controller
// @RequestMapping("/upload-test")
// public class TestUploadfile {
//     @Autowired
//     private StorageService storageService;

//     @GetMapping
//     public String uploadDemo() {
//         return "upload-test";
//     }

//     @PostMapping
//     public String save(@RequestParam("file") MultipartFile file) {
//         // TODO: process POST request
//         this.storageService.strore(file);
//         return "upload-test";
//     }

// }
