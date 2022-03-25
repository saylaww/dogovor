package uz.nukuslab.dogovor.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.nukuslab.dogovor.payload.ApiResponse;
import uz.nukuslab.dogovor.payload.PropDto;
import uz.nukuslab.dogovor.service.PropService;


@RestController
@RequestMapping("/prop")
public class PropController {

    private final
    PropService propService;

    public PropController(PropService propService) {
        this.propService = propService;
    }

    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAllProps() {
        ApiResponse apiResponse = propService.getAllProps();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/{id}")
    public HttpEntity<?> getProp(@PathVariable Long id) {
        ApiResponse apiResponse = propService.getProp(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @PutMapping("/editProp/{id}")
    public HttpEntity<?> editProp(@PathVariable Long id, @RequestBody PropDto propDto) {
        ApiResponse apiResponse = propService.editProp(id, propDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping("/addProp")
    public HttpEntity<?> addProp(@RequestBody PropDto propDto) {
        ApiResponse apiResponse = propService.addProp(propDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/deleteProp/{id}")
    public HttpEntity<?> deleteProp(@PathVariable Long id) {
        ApiResponse apiResponse = propService.deleteProp(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


}
