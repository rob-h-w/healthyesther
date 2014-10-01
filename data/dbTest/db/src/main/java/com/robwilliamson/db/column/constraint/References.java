package com.robwilliamson.db.column.constraint;

import com.robwilliamson.db.column.ColumnDefinition;

public class References extends Constraint {
    private final String mForeignTable;
    private final ColumnDefinition mForeignColumn;
    private final Reaction [] mReactions;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("REFERENCES ");
        str.append(mForeignTable);
        str.append(" ( ").append(mForeignColumn.name).append(" )");

        for (Reaction reaction : mReactions) {
            str.append(" ").append(reaction);
        }
        return null;
    }

    public enum Action {
        CASCADE
    }

    public static abstract class Reaction {
        public final Action action;
        public final String name;

        protected Reaction(final Action action, final String name) {
            this.action = action;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + " " + action;
        }
    }

    static class OnUpdate extends Reaction {
        public OnUpdate(final Action action) {
            super(action, "ON UPDATE");
        }
    }

    static class OnDelete extends Reaction {
        public OnDelete(final Action action) {
            super(action, "ON DELETE");
        }
    }

    static class Match extends Reaction {
        public Match(final Action action) {
            super(action, "MATCH");
        }
    }

    public static Reaction onUpdate(final Action action) { return new OnUpdate(action); }
    public static Reaction onDelete(final Action action) { return new OnDelete(action); }
    public static Reaction match(final Action action) { return new Match(action); }

    References(final String foreignTable,
               final ColumnDefinition foreignColumn,
               final Reaction... reactions) {
        mForeignTable = foreignTable;
        mForeignColumn = foreignColumn;
        mReactions = reactions;
    }
}
