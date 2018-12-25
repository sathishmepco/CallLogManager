package com.codificador.calllogmanager;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.codificador.calllogmanager.databinding.ActivityStatisticsBinding;

public class StatisticsActivity extends AppCompatActivity {

    ActivityStatisticsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_statistics);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initValues();
    }

    private void initValues(){
        String number = getIntent().getStringExtra("number");
        String name = getIntent().getStringExtra("name");
        if(number == null){
            finish();
            return;
        }

        CallLogUtils callLogUtils = CallLogUtils.getInstance(getApplicationContext());
        long data[] = callLogUtils.getAllCallState(number);
        binding.textViewCallCountTotal.setText(data[0]+" calls");
        binding.textViewCallDurationsTotal.setText(Utils.formatSeconds(data[1]));

        data = callLogUtils.getIncomingCallState(number);
        binding.textViewCallCountIncoming.setText(data[0]+" calls");
        binding.textViewCallDurationsIncoming.setText(Utils.formatSeconds(data[1]));

        data = callLogUtils.getOutgoingCallState(number);
        binding.textViewCallCountOutgoing.setText(data[0]+" calls");
        binding.textViewCallDurationsOutgoing.setText(Utils.formatSeconds(data[1]));

        int count = callLogUtils.getMissedCallState(number);
        binding.textViewCallCountMissed.setText(count+" calls");
        binding.textViewCallDurationsMissed.setText(Utils.formatSeconds(0));

        binding.textViewNumber.setText(number);
        binding.textViewName.setText(TextUtils.isEmpty(name) ? number : name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
