package com.robwilliamson.healthyesther.fragment.edit;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.adapter.EventListAdapter;
import com.robwilliamson.healthyesther.db.data.EventData;
import com.robwilliamson.healthyesther.db.definition.Modification;
import com.robwilliamson.healthyesther.db.use.QueuedQueryExecutor;

import java.util.Arrays;

/**
 * A fragment representing a list of Events.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link EventListFragment.Watcher}
 * interface.
 */
public abstract class EventListFragment extends EditFragment<EventListFragment.Watcher> implements AbsListView.OnItemClickListener {

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private EventListAdapter mAdapter;

    protected EventListFragment() {
        super(EventListFragment.Watcher.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new EventListAdapter(getActivity(), R.layout.view_list_item_event);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Set the adapter
        AbsListView listView = getListView(view);
        listView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_event;
    }

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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        callWatcher(new WatcherCaller<Watcher>() {
            @Override
            public void call(Watcher watcher) {
                watcher.onEventSelected(mAdapter.getItem(position));
            }
        });
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();

        callWatcher(new WatcherCaller<Watcher>() {
            @Override
            public void call(Watcher watcher) {
                watcher.enqueueQueries(Arrays.asList(getQueries()));
            }
        });
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = getListView().getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    protected AbsListView getListView() {
        return getListView(null);
    }

    protected AbsListView getListView(View parent) {
        return Utils.View.getTypeSafeView(parent == null ? getView() : parent, android.R.id.list, AbsListView.class);
    }

    protected EventListAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Watcher extends QueuedQueryExecutor {
        void onEventSelected(EventData eventData);

        void onQueryFailure(Throwable failure);
    }
}
