package org.gamemessenger.Architectur.adapter_android.actionMode;

import android.app.Activity;
import androidx.appcompat.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import org.gamemessenger.Architectur.Factory.RowsAdapter;
import org.gamemessenger.Architectur.Factory.SingleActivity;
import org.gamemessenger.R;

public class Toolbar_ActionMode_Callback implements ActionMode.Callback {

    private Activity activity;
    private RowsAdapter recyclerView_adapter;


    public Toolbar_ActionMode_Callback(Activity activity, RowsAdapter recyclerView_adapter) {
        this.activity = activity;
        this.recyclerView_adapter = recyclerView_adapter;
    }
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.findItem(R.id.menu_item_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete:
                SingleActivity messageActivity = (SingleActivity) activity;
                if(messageActivity != null) {
                    messageActivity.deleteRows();
                }
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        recyclerView_adapter.removeSelection();
        SingleActivity messageActivity = (SingleActivity) activity;
        if(messageActivity != null) {
            messageActivity.setNullToActionMode();
        }

    }
}
