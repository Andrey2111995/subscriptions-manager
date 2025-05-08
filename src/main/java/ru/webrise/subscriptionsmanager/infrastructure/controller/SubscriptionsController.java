package ru.webrise.subscriptionsmanager.infrastructure.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.webrise.subscriptionsmanager.infrastructure.validator.UuidNotBlank;
import ru.webrise.subscriptionsmanager.logic.SubscriptionsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/${spring.application.name}/v1")
@RequiredArgsConstructor
@Validated
public class SubscriptionsController {

    private final SubscriptionsService subscriptionsService;

    @GetMapping("/users/{id}/subscriptions")
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions(@UuidNotBlank @PathVariable("id") String userId) {
        return ResponseEntity.ok(subscriptionsService.getSubscriptions(UUID.fromString(userId)));
    }

    @PostMapping("/users/{id}/subscriptions")
    public ResponseEntity<SubscriptionDto> saveSubscription(@UuidNotBlank @PathVariable("id") String userId,
                                                            @Valid @RequestBody SubscriptionDto request) {
        return ResponseEntity.ok(subscriptionsService.saveSubscription(UUID.fromString(userId), request));
    }

    @DeleteMapping("/users/{id}/subscriptions/{sub_id}")
    public ResponseEntity<?> deleteSubscription(@UuidNotBlank @PathVariable("id") String userId,
                                                @UuidNotBlank @PathVariable("sub_id") String subId) {
        subscriptionsService.deleteSubscription(UUID.fromString(userId), UUID.fromString(subId));

        return ResponseEntity.ok().build();
    }

    @GetMapping("subscriptions/top")
    public ResponseEntity<List<SubscriptionDto>> getTopSubscriptions() {
        return ResponseEntity.ok(subscriptionsService.getTopSubscriptions());
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SubscriptionDto(
            @JsonProperty("subscription_id")
            String subscriptionId,
            @NotBlank(message = "Не передано название сервиса подписки")
            @JsonProperty("service_name")
            String serviceName) {
    }
}
