package uz.nukuslab.dogovor.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.nukuslab.dogovor.payload.ApiResponse;
import uz.nukuslab.dogovor.payload.PhoneDto;
import uz.nukuslab.dogovor.service.PhoneService;


@RestController
@RequestMapping("/phone")
public class PhoneController {

    private final
    PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAllPhones() {
        ApiResponse apiResponse = phoneService.getAllPhone();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/{id}")
    public HttpEntity<?> getPhone(@PathVariable Long id) {
        ApiResponse apiResponse = phoneService.getPhone(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping("/addPhone")
    public HttpEntity<?> addPhone(@RequestBody PhoneDto phoneDto) {
        ApiResponse apiResponse = phoneService.addPhone(phoneDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @PutMapping("/editPhone/{id}")
    public HttpEntity<?> editPhone(@PathVariable Long id, @RequestBody PhoneDto phoneDto) {
        ApiResponse apiResponse = phoneService.editPhone(id, phoneDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/deletePhone/{id}")
    public HttpEntity<?> deletePhone(@PathVariable Long id) {
        ApiResponse apiResponse = phoneService.deletePhone(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


}
