package uz.nukuslab.dogovor.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.nukuslab.dogovor.payload.ApiResponse;
import uz.nukuslab.dogovor.payload.CompanyDto;
import uz.nukuslab.dogovor.service.CompanyService;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final
    CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAllCompany(){
        ApiResponse apiResponse = companyService.getAllCompany();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/{id}")
    public HttpEntity<?> getCompany(@PathVariable Long id){
        ApiResponse apiResponse = companyService.getCompany(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @PutMapping("/editCompany/{id}")
    public HttpEntity<?> editCompany(@PathVariable Long id, @RequestBody CompanyDto companyDto){
        ApiResponse apiResponse = companyService.editCompany(id, companyDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/deleteCompany/{id}")
    public HttpEntity<?> deleteCompany(@PathVariable Long id){
        ApiResponse apiResponse = companyService.deleteCompany(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping("/addCompany")
    public HttpEntity<?> addCompany(@RequestBody CompanyDto companyDto){
        ApiResponse apiResponse = companyService.addCompany(companyDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }



}
