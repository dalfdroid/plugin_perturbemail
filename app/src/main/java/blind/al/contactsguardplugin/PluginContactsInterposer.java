package blind.al.contactsguardplugin;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.os.IPluginContactsInterposer;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.util.Log;
import android.telephony.PhoneNumberUtils;
import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PluginContactsInterposer extends IPluginContactsInterposer.Stub {


    public PluginContactsInterposer() {

    }

    @Override
    public CursorWindow modifyData(String targetPkg, Uri url, String[] projection,
                                   Bundle queryArgs, CursorWindow inputWindow, String[] columns,
                                   int numRows)
            throws RemoteException {

        return PerturbEmail(targetPkg, url, projection, queryArgs, inputWindow, columns, numRows);
    }

    private CursorWindow PerturbEmail(String targetPkg, Uri url, String[] projection,
                                       Bundle queryArgs, CursorWindow inputWindow, String[] columns,
                                       int numRows) {

        CursorWindow outputWindow = new CursorWindow("perturbed");
        outputWindow.setNumColumns(columns.length);

        // Find email column id if it exist
        int emailColId = -1;
        if (url.equals(CommonDataKinds.Email.CONTENT_URI)){
            emailColId = Arrays.asList(columns).indexOf(ContactsContract.CommonDataKinds.Email.DATA);
        }

        for (int i = 0; i < inputWindow.getNumRows(); i++) {
            outputWindow.allocRow();
            copyCursorRow(inputWindow,outputWindow,columns,i,emailColId);
        }

        return  outputWindow;
    }

    private void copyCursorRow(CursorWindow inputWindow, CursorWindow outputWindow, String[] columns, int rowId, int perturbColId){
        for (int j = 0; j < columns.length; j++) {
            int type = inputWindow.getType(rowId, j);
            switch(type) {
                case Cursor.FIELD_TYPE_BLOB:
                    outputWindow.putBlob(inputWindow.getBlob(rowId, j), rowId, j);
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    outputWindow.putDouble(inputWindow.getDouble(rowId, j), rowId, j);
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    outputWindow.putLong(inputWindow.getLong(rowId, j), rowId, j);
                    break;
                case Cursor.FIELD_TYPE_NULL:
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    if (perturbColId==j) {
                        outputWindow.putString("****", rowId, j);
                    } else {
                        outputWindow.putString(inputWindow.getString(rowId, j), rowId, j);
                    }
                    break;
            }
        }
    }

}
