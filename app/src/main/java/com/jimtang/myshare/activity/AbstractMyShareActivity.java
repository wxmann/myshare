package com.jimtang.myshare.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.jimtang.myshare.R;

/**
 * Created by tangz on 11/15/2015.
 */
abstract class AbstractMyShareActivity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_share_setting:
                kickOffNewShare();
                return true;
            case R.id.menu_saved_share_settings:
                kickOffSavedShares();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void kickOffNewShare() {
        Intent intent = new Intent(this, AddPeopleActivity.class);
        startActivity(intent);
    }

    private void kickOffSavedShares() {
    }
}
