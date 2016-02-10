package com.robwilliamson.healthyesther.db.integration;

import javax.annotation.Nonnull;

public interface Upgrade {
    void upgradeFrom(@Nonnull Transaction transaction);
}
