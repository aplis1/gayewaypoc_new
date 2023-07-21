package com.second.secondmicroservice.controller;

import com.second.secondmicroservice.model.UploadRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/second")
public class SecondController {

    @GetMapping("/message")
    public String message(){
        return "Hello from Second Microservice";
    }

   /* @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file",required = false) MultipartFile file, @RequestParam(value = "id",required = false) String id) throws Exception {

        return ResponseEntity.ok(file.getOriginalFilename() + " uploaded" +" id: "+ id);

    }*/
   @PostMapping("/upload")
   public ResponseEntity<?> uploadFile(@ModelAttribute UploadRequest request) throws Exception {

       return ResponseEntity.ok(request.getFile().getOriginalFilename() + " uploaded" +" id: "+ request.getId());

   }
}
