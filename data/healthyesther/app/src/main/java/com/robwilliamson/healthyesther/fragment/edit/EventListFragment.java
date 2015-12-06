package com.robwilliamson.healthyesther.fragment.edit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.adapter.EventListAdapter;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.OrderBy;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.fragment.BaseFragment;
import com.robwilliamson.healthyesther.fragment.DbFragment;

import javax.annotation.Nonnull;

/**
 * A fragment representing a list of Events.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link EventListFragment.Watcher}
 * interface.
 */
public class EventListFragment extends DbFragment implements AbsListView.OnItemClickListener {

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private EventListAdapter mAdapter;
    private Watcher mWatcher;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mWatcher = (Watcher) context;
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        mWatcher.onEventSelected(mAdapter.getItem(position));
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

        mWatcher.getExecutor().perform(new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                final EventTable.Row[] rows = DatabaseAccessor.EVENT_TABLE.select(
                        database,
                        WhereContains.all(),
                        OrderBy.column().named(EventTable.WHEN).desc());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        mAdapter.addAll(rows);
                    }
                });
            }
        });
    }

    protected AbsListView getListView(View parent) {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(parent == null ? getView() : parent), android.R.id.list, AbsListView.class);
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
    public interface Watcher extends ExecutorProvider, BaseFragment.Watcher {
        void onEventSelected(EventTable.Row row);
    }
}
