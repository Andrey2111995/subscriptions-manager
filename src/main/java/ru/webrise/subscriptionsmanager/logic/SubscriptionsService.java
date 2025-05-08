package ru.webrise.subscriptionsmanager.logic;

import ru.webrise.subscriptionsmanager.infrastructure.controller.SubscriptionsController;

import java.util.List;
import java.util.UUID;

public interface SubscriptionsService {

    List<SubscriptionsController.SubscriptionDto> getSubscriptions(UUID userId);

    SubscriptionsController.SubscriptionDto saveSubscription(UUID userId,
                                                             SubscriptionsController.SubscriptionDto request);

    void deleteSubscription(UUID userId, UUID subId);

    List<SubscriptionsController.SubscriptionDto> getTopSubscriptions();
}
