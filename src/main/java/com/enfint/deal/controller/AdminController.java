package com.enfint.deal.controller;

import com.enfint.deal.dto.ApplicationModelDTO;
import com.enfint.deal.dto.ClientModelDTO;
import com.enfint.deal.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deal/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/application/{applicationId}/status")
    public void updateApplicationStatus(@PathVariable("applicationId") Long applicationId){
        log.info("ApplicationId {} ",applicationId);
        adminService.updateApplicationStatus(applicationId);
    }

    @GetMapping("/application/{applicationId}")
    public ApplicationModelDTO getApplicationById(@PathVariable("applicationId") Long applicationId){
        log.info("ApplicationId {} ",applicationId);
        return adminService.getApplicationById(applicationId);
    }

    @GetMapping("/client/{firstName}/{lastName}")
    public ClientModelDTO getClientByFistNameAndLastName(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName){
        log.info("FirstName => {} LastName => {}",firstName,lastName);
        return adminService.getClientByFistNameAndLastName(firstName,lastName);
    }

}
