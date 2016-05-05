/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.semantic;

import com.robwilliamson.healthyesther.type.Column;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Relation {
    private static final Map<Column, Set<Relation>> sRelationsByBaseColumn = new HashMap<>();
    private static final Map<Column, Relation> sRelationsByRelatedColumn = new HashMap<>();

    @Nonnull
    private final Column mBaseColumn;
    @Nonnull
    private final Column mRelatedColumn;

    private Relation(@Nonnull Column baseColumn, @Nonnull Column relatedColumn) {
        mBaseColumn = baseColumn;
        mRelatedColumn = relatedColumn;

        if (sRelationsByBaseColumn.containsKey(baseColumn)) {
            sRelationsByBaseColumn.get(baseColumn).add(this);
        } else {
            Set<Relation> relationSet = new HashSet<>(1);
            relationSet.add(this);
            sRelationsByBaseColumn.put(baseColumn, relationSet);
        }

        sRelationsByRelatedColumn.put(relatedColumn, this);
    }

    @Nonnull
    public static Builder with() {
        return new Builder();
    }

    @Nonnull
    public static Set<Relation> getAllRelationsOf(@Nonnull Column baseColumn) {
        if (sRelationsByBaseColumn.containsKey(baseColumn)) {
            return sRelationsByBaseColumn.get(baseColumn);
        }

        return new HashSet<>();
    }

    @Nullable
    public static Relation getRelationOf(@Nonnull Column relatedColumn) {
        return sRelationsByRelatedColumn.get(relatedColumn);
    }

    @Nonnull
    public Column getBaseColumn() {
        return mBaseColumn;
    }

    @Nonnull
    public Column getRelatedColumn() {
        return mRelatedColumn;
    }

    public static class Builder {
        private Column mBaseColumn;
        private Column mRelatedColumn;

        Builder() {
        }

        @Nonnull
        public Builder baseColumn(@Nonnull Column baseColumn) {
            mBaseColumn = baseColumn;
            return this;
        }

        @Nonnull
        public Builder relatedColumn(@Nonnull Column relatedColumn) {
            mRelatedColumn = relatedColumn;
            return this;
        }

        @Nonnull
        public Builder with() {
            return this;
        }

        @Nonnull
        public Builder and() {
            return this;
        }

        @Nonnull
        public Relation get() {
            if (mBaseColumn == null) {
                throw new MissingBaseColumnException();
            }

            Relation relation = sRelationsByRelatedColumn.get(mRelatedColumn);

            if (relation == null) {
                if (mRelatedColumn == null) {
                    throw new MissingRelatedColumnException();
                }

                relation = new Relation(mBaseColumn, mRelatedColumn);
            }

            return relation;
        }

        public static class MissingBaseColumnException extends RuntimeException {
        }

        public static class MissingRelatedColumnException extends RuntimeException {
        }
    }
}
