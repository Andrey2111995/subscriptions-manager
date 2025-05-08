package ru.webrise.subscriptionsmanager.infrastructure.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.webrise.subscriptionsmanager.infrastructure.data.Subscriptions;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscriptions, UUID> {

    List<Subscriptions> findSubscriptionsByUserId(UUID userId);

    @Query(
            value = """
                    SELECT serviceName
                    FROM (SELECT service_name serviceName,
                                 rank() OVER (ORDER BY COUNT(*) DESC) rank
                          FROM subscriptions
                          GROUP BY service_name) as s
                    WHERE rank = 1
                        """,
            nativeQuery = true)
    List<SubscriptionTop> getTopSubscriptions();

    interface SubscriptionTop {

        String getServiceName();
    }
}
