package com.co.eatupapi.controllers.payment.invoice;

import com.co.eatupapi.dto.payment.invoice.InvoiceDTO;
import com.co.eatupapi.dto.payment.invoice.InvoiceStatusUpdateDTO;
import com.co.eatupapi.services.payment.invoice.InvoiceService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO request) {
        InvoiceDTO saved = invoiceService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable String invoiceId) {
        InvoiceDTO invoice = invoiceService.getById(invoiceId);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getInvoices(
            @RequestParam(required = false) String status) {

        List<InvoiceDTO> invoices = invoiceService.getAll(status);
        return ResponseEntity.ok(invoices);
    }

    @PatchMapping("/{invoiceId}/status")
    public ResponseEntity<InvoiceDTO> updateStatus(
            @PathVariable String invoiceId,
            @RequestBody InvoiceStatusUpdateDTO request) {

        InvoiceDTO updated = invoiceService.updateStatus(invoiceId, request.getStatus());
        return ResponseEntity.ok(updated);
    }
}