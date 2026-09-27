package com.ryuunomi.inmotech.repositories;

import com.ryuunomi.inmotech.entities.StripeWebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StripeWebhookEventRepository extends JpaRepository<StripeWebhookEvent, Long> {

    boolean existsByEventId(String eventId);
}
