package com.robwilliamson.healthyesther.fragment.edit;

import com.robwilliamson.db.definition.Modification;
import com.robwilliamson.db.use.Query;

public class EditNoteFragment extends EditFragment<EditNoteFragment.Watcher> {

    public interface Watcher {
        void onFragmentUpdate(EditNoteFragment fragment);
        void onQueryFailed(EditNoteFragment fragment, Throwable error);
    }

    public EditNoteFragment() {}

    @Override
    public Modification getModification() {
        return null;
    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    protected void updateWatcher(Watcher watcher) {

    }

    @Override
    public Query[] getQueries() {
        return new Query[0];
    }
}
