package uz.nukuslab.dogovor.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.nukuslab.dogovor.payload.AddressDto;
import uz.nukuslab.dogovor.payload.ApiResponse;
import uz.nukuslab.dogovor.service.AddressService;


@RestController
@RequestMapping("/address")
public class AddressController {

    private final
    AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping("/add")
    public HttpEntity<?> addAddress(@RequestBody AddressDto addressDto){
        ApiResponse apiResponse = addressService.addAddress(addressDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @GetMapping
    public HttpEntity<?> getAddresses(){
        ApiResponse apiResponse = addressService.getAllAddresses();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/{id}")
    public HttpEntity<?> getAddress(@PathVariable Long id){
        ApiResponse apiResponse = addressService.getAddress(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @PutMapping("/editAddress/{id}")
    public HttpEntity<?> editAddress(@PathVariable Long id, @RequestBody AddressDto addressDto){
        ApiResponse apiResponse = addressService.editAddress(id, addressDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/deleteAddress/{id}")
    public HttpEntity<?> deleteAddress(@PathVariable Long id){
        ApiResponse apiResponse = addressService.deleteAddress(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }



}
