package com.robwilliamson.healthyesther.db.includes;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public abstract class Table {
    private final List<Upgrader> mUpgraders = new ArrayList<>();

    public abstract
    @Nonnull
    String getName();

    public abstract void create(Transaction transaction);

    public abstract void drop(Transaction transaction);

    public void upgrade(Transaction transaction, int from, int to) {
        for (Upgrader upgrader : mUpgraders) {
            upgrader.upgrade(transaction, from, to);
        }
    }

    public void addUpgrader(@Nonnull Upgrader upgrader) {
        mUpgraders.add(upgrader);
    }

    public interface Upgrader {
        void upgrade(Transaction transaction, int from, int to);
    }
}
