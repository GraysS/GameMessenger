package org.gamemessenger.Architectur.Factory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import org.gamemessenger.Architectur.adapter_android.actionMode.Toolbar_ActionMode_Callback;

import java.io.UnsupportedEncodingException;

public abstract class SingleActivity extends AppCompatActivity {

    private ActionMode mActionMode;

    public abstract void deleteRows();

    protected void onListItemSelect(int position,RowsAdapter adapter) {
        adapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = adapter.getSelectedCount() > 0;


        if (hasCheckedItems && mActionMode == null) {

            mActionMode = startSupportActionMode(new Toolbar_ActionMode_Callback(this,adapter));

        }  else if (!hasCheckedItems && mActionMode != null)

            mActionMode.finish();

        if (mActionMode != null) {
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(adapter
                    .getSelectedCount()) + " selected");
        }
    }

    public ActionMode getmActionMode() {
        return mActionMode;
    }


    public void setNullToActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }


}
