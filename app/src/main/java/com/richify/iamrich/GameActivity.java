package com.richify.iamrich;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.richify.iamrich.billing.BillingManager;
import com.richify.iamrich.billing.BillingProvider;

/**
 * Created by thomaslin on 02/03/2018.
 *
 */

public class GameActivity extends DialogFragment implements BillingProvider {

    private static final String TAG = "GameActivity";



    @Override
    public BillingManager getBillingManager() {
        return null;
    }
}
