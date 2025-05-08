package ru.webrise.subscriptionsmanager.logic.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.webrise.subscriptionsmanager.infrastructure.controller.SubscriptionsController;
import ru.webrise.subscriptionsmanager.infrastructure.data.Subscriptions;
import ru.webrise.subscriptionsmanager.infrastructure.data.Users;
import ru.webrise.subscriptionsmanager.infrastructure.data.repository.SubscriptionRepository;
import ru.webrise.subscriptionsmanager.infrastructure.data.repository.UserRepository;
import ru.webrise.subscriptionsmanager.infrastructure.exceptions.ApplicationException;
import ru.webrise.subscriptionsmanager.logic.SubscriptionsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final UserRepository userRepository;

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public List<SubscriptionsController.SubscriptionDto> getSubscriptions(UUID userId) {
        final Optional<Users> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            log.warn("There is no user record with id = {}", userId);
            throw new ApplicationException("Не найден пользователь с id = %s".formatted(userId),
                    HttpStatus.NOT_FOUND);
        }

        return subscriptionRepository.findSubscriptionsByUserId(userId).stream()
                .map(subscription -> SubscriptionsController.SubscriptionDto.builder()
                        .serviceName(subscription.getServiceName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SubscriptionsController.SubscriptionDto saveSubscription(UUID userId,
                                                                    SubscriptionsController.SubscriptionDto request) {
        final Optional<Users> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            log.warn("There is no user record with id = {}", userId);
            throw new ApplicationException("Не найден пользователь с id = %s".formatted(userId));
        }

        final Subscriptions subscriptions = Subscriptions.builder()
                .serviceName(request.serviceName())
                .status(Subscriptions.Status.ACTIVE)
                .users(userOpt.get())
                .build();

        final Subscriptions savedSubscription = subscriptionRepository.save(subscriptions);
        log.info("Created subscription record with id: {}", savedSubscription.getId());

        return SubscriptionsController.SubscriptionDto.builder()
                .subscriptionId(savedSubscription.getId().toString())
                .build();
    }

    @Override
    @Transactional
    public void deleteSubscription(UUID userId, UUID subId) {
        final Optional<Users> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            log.warn("There is no user record with id = {}", userId);
            throw new ApplicationException("Не найден пользователь с id = %s".formatted(userId));
        }

        final Optional<Subscriptions> targetSubscriptionOpt = subscriptionRepository
                .findSubscriptionsByUserId(userId).stream()
                .filter(subscription -> subId.equals(subscription.getId()))
                .findFirst();

        if (targetSubscriptionOpt.isEmpty()) {
            log.warn("There is no subscription with id = {}", subId);
            throw new ApplicationException("Не найдена подписка с id = %s".formatted(subId));
        }

        subscriptionRepository.deleteById(subId);
        log.info("Deleted subscription record with id: {}", userId);
    }

    @Override
    public List<SubscriptionsController.SubscriptionDto> getTopSubscriptions() {
        return subscriptionRepository.getTopSubscriptions().stream()
                .map(sub -> SubscriptionsController.SubscriptionDto.builder()
                        .serviceName(sub.getServiceName())
                        .build())
                .collect(Collectors.toList());
    }
}
