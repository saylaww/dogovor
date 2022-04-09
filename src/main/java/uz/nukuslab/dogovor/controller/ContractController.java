package uz.nukuslab.dogovor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.nukuslab.dogovor.entity.Dogovor;
import uz.nukuslab.dogovor.entity.User;
import uz.nukuslab.dogovor.payload.ApiResponse;
import uz.nukuslab.dogovor.payload.DateDto;
import uz.nukuslab.dogovor.payload.DogovorDto;
import uz.nukuslab.dogovor.repository.DogovorRepository;
import uz.nukuslab.dogovor.service.DogovorService;


import java.io.IOException;
import java.text.ParseException;
import java.util.List;


@RestController
@RequestMapping("/dogovor")
public class ContractController {

    private final
    DogovorService contractService;
    private final
    DogovorRepository dogovorRepository;

    public ContractController(DogovorService contractService, DogovorRepository dogovorRepository) {
        this.contractService = contractService;
        this.dogovorRepository = dogovorRepository;
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping
    public HttpEntity<?> getAllContracts() {
        ApiResponse apiResponse = contractService.getAllContract();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/{id}")
    public HttpEntity<?> getContract(@PathVariable Long id) {
        ApiResponse apiResponse = contractService.getContract(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping("/addContract")
    public HttpEntity<?> addContract(@RequestBody DogovorDto contractDto) {
        ApiResponse apiResponse = contractService.addContract(contractDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @PutMapping("/editContract/{id}")
    public HttpEntity<?> editContract(@PathVariable Long id, @RequestBody DogovorDto contractDto) {
        ApiResponse apiResponse = contractService.editContract(id, contractDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/deleteContract/{id}")
    public HttpEntity<?> deleteContract(@PathVariable Long id) {
        ApiResponse apiResponse = contractService.deleteContract(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/byAdmin/{id}")
    public HttpEntity<?> ContractByAdmin(@PathVariable Integer id) {
        ApiResponse apiResponse = contractService.byAdmin(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/byDate")
    public HttpEntity<?> ByDate(@RequestBody DateDto dateDto) throws ParseException {
        ApiResponse apiResponse = contractService.byDate(dateDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/reportLastYear")
    public HttpEntity<?> reportLastYear() {
        ApiResponse apiResponse = contractService.reportLastYear();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/reportLastMonth")
    public HttpEntity<?> reportLastMonth() {
        ApiResponse apiResponse = contractService.reportLastMonth();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/reportLastDay")
    public HttpEntity<?> reportLastDay() {
        ApiResponse apiResponse = contractService.reportLastDay();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/exportWord")
    public HttpEntity<?> exportWord(@RequestBody List<Dogovor> dogovorList) throws IOException {
        ApiResponse apiResponse = contractService.exportWord(dogovorList);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/exportPdf")
    public HttpEntity<?> exportPdf(@RequestBody List<Dogovor> dogovorList) throws IOException {
        ApiResponse apiResponse = contractService.exportPdf(dogovorList);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/exportExcel")
    public HttpEntity<?> exportExcel(@RequestBody List<Dogovor> dogovorList) throws IOException {
        ApiResponse apiResponse = contractService.exportExcel(dogovorList);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/testWord")
    public HttpEntity<?> testWord() throws IOException {
        List<Dogovor> dogovorList = dogovorRepository.findAll();
        contractService.exportWord(dogovorList);
        return ResponseEntity.ok("OKKKKKKKKKKKKKKKK");
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/testPdf")
    public HttpEntity<?> testPdf() throws IOException {
        List<Dogovor> dogovorList = dogovorRepository.findAll();
        contractService.exportPdf(dogovorList);
        return ResponseEntity.ok("OKKKKKKKKKKKKKKKK");
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @GetMapping("/testExcel")
    public HttpEntity<?> testExcel() throws IOException {
        List<Dogovor> dogovorList = dogovorRepository.findAll();
        contractService.exportExcel(dogovorList);
        return ResponseEntity.ok("OKKKKKKKKKKKKKKKK");
    }


    @PreAuthorize(value = "hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @GetMapping("/myDog")
    public HttpEntity<?> myContracts() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Dogovor> byUserId = dogovorRepository.findByUserId(user.getId());
//        List<Dogovor> dogovorList = dogovorRepository.findAll();
//        contractService.exportExcel(dogovorList);
        return ResponseEntity.ok(byUserId);
    }



}
