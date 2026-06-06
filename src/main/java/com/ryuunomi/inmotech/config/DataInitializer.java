package com.ryuunomi.inmotech.config;

import com.ryuunomi.inmotech.entities.Plan;
import com.ryuunomi.inmotech.repositories.PlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PlanRepository planRepository;

    public DataInitializer(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public void run(String... args) {
        if (planRepository.count() == 0) {
            Plan gratis = new Plan("Gratuito", 2, 4, BigDecimal.ZERO);
            gratis.setStripePriceId("price_gratis");
            planRepository.save(gratis);

            Plan premium = new Plan("Premium", Integer.MAX_VALUE, Integer.MAX_VALUE, new BigDecimal("9.99"));
            premium.setStripePriceId("price_premium_placeholder");
            planRepository.save(premium);
        }
    }
}
