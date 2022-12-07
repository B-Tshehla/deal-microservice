package com.enfint.deal.controller;



import com.enfint.deal.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/deal/document")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/{applicationId}/send")
    public void sendDocuments(@PathVariable("applicationId") Long applicationId){
        documentService.sendingDocuments(applicationId);
    }

    @PostMapping("/{applicationId}/sign")
    public void signDocuments(@PathVariable("applicationId") Long applicationId){
        documentService.signingDocuments(applicationId);
    }

    @PostMapping("/{applicationId}/code")
    public void documentsCode(@PathVariable("applicationId") Long applicationId,@RequestBody Integer sesCode){
        log.info("sesCode {} ", sesCode);
        documentService.verifySesCode(applicationId,sesCode);
    }
}
