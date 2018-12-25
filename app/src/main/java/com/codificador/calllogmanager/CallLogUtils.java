package com.codificador.calllogmanager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;
import java.util.ArrayList;

public class CallLogUtils {

    private static CallLogUtils instance;
    private Context context;
    private ArrayList<CallLogInfo> mainList = null;
    private ArrayList<CallLogInfo> missedCallList = null;
    private ArrayList<CallLogInfo> outgoingCallList = null;
    private ArrayList<CallLogInfo> incomingCallList = null;

    private CallLogUtils(Context context){
        this.context = context;
    }

    public static CallLogUtils getInstance(Context context){
        if(instance == null)
            instance = new CallLogUtils(context);
        return instance;
    }

    private void loadData(){
        mainList = new ArrayList<>();
        missedCallList = new ArrayList<>();
        outgoingCallList = new ArrayList<>();
        incomingCallList = new ArrayList<>();

        String projection[] = {"_id", CallLog.Calls.NUMBER,CallLog.Calls.DATE,CallLog.Calls.DURATION,CallLog.Calls.TYPE,CallLog.Calls.CACHED_NAME};
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI,projection,null,null,CallLog.Calls.DEFAULT_SORT_ORDER);

        if(cursor == null){
            Log.d("CALLLOG","cursor is null");
            return;
        }

        if(cursor.getCount() == 0){
            Log.d("CALLLOG","cursor size is 0");
            return;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            CallLogInfo callLogInfo = new CallLogInfo();
            callLogInfo.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
            callLogInfo.setNumber(cursor.getString(cursor.getColumnIndex( CallLog.Calls.NUMBER )));
            callLogInfo.setCallType(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
            callLogInfo.setDate(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
            callLogInfo.setDuration(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)));
            mainList.add(callLogInfo);

            switch(Integer.parseInt(callLogInfo.getCallType()))
            {
                case CallLog.Calls.OUTGOING_TYPE:
                    outgoingCallList.add(callLogInfo);
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    incomingCallList.add(callLogInfo);
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    missedCallList.add(callLogInfo);
                    break;
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    public ArrayList<CallLogInfo> readCallLogs(){
        if(mainList == null)
            loadData();
        return mainList;
    }

    public ArrayList<CallLogInfo> getMissedCalls(){
        if(mainList == null)
            loadData();
        return missedCallList;
    }

    public ArrayList<CallLogInfo> getIncommingCalls(){
        if(mainList == null)
            loadData();
        return incomingCallList;
    }

    public ArrayList<CallLogInfo> getOutgoingCalls(){
        if(mainList == null)
            loadData();
        return outgoingCallList;
    }

    public long[] getAllCallState(String number){
        long output[] = new long[2];
        for(CallLogInfo callLogInfo : mainList){
            if(callLogInfo.getNumber().equals(number)){
                output[0]++;
                if(Integer.parseInt(callLogInfo.getCallType()) != CallLog.Calls.MISSED_TYPE)
                    output[1]+= callLogInfo.getDuration();
            }
        }
        return output;
    }

    public long[] getIncomingCallState(String number){
        long output[] = new long[2];
        for(CallLogInfo callLogInfo : incomingCallList){
            if(callLogInfo.getNumber().equals(number)){
                output[0]++;
                output[1]+= callLogInfo.getDuration();
            }
        }
        return output;
    }

    public long[] getOutgoingCallState(String number){
        long output[] = new long[2];
        for(CallLogInfo callLogInfo : outgoingCallList){
            if(callLogInfo.getNumber().equals(number)){
                output[0]++;
                output[1]+= callLogInfo.getDuration();
            }
        }
        return output;
    }

    public int getMissedCallState(String number){
        int output =0;
        for(CallLogInfo callLogInfo : missedCallList){
            if(callLogInfo.getNumber().equals(number)){
                output++;
            }
        }
        return output;
    }
}