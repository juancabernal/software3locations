package com.co.eatupapi.controllers.commercial.provider;

import com.co.eatupapi.dto.commercial.provider.ProviderDTO;
import com.co.eatupapi.dto.commercial.provider.ProviderStatusUpdateDTO;
import com.co.eatupapi.services.commercial.provider.ProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/commercial/api/v1/providers")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @PostMapping
    public Mono<ResponseEntity<ProviderDTO>> createProvider(@RequestBody ProviderDTO request) {
        return providerService.createProvider(request)
                .map(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved));
    }

    @GetMapping("/{providerId}")
    public Mono<ResponseEntity<ProviderDTO>> getProviderById(@PathVariable String providerId) {
        return providerService.getProviderById(providerId)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Flux<ProviderDTO> getProviders(@RequestParam(required = false) String status) {
        return providerService.getProviders(status);
    }

    @PutMapping("/{providerId}")
    public Mono<ResponseEntity<ProviderDTO>> updateProvider(@PathVariable String providerId,
                                                            @RequestBody ProviderDTO request) {
        return providerService.updateProvider(providerId, request)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{providerId}/status")
    public Mono<ResponseEntity<ProviderDTO>> updateStatus(@PathVariable String providerId,
                                                          @RequestBody ProviderStatusUpdateDTO request) {
        return providerService.updateStatus(providerId, request.getStatus())
                .map(ResponseEntity::ok);
    }
}
