package com.whiner.common.view;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.whiner.common.R;

import java.util.List;

public class TvSpinner implements DialogInterface.OnClickListener {

    private final AlertDialog alertDialog;
    private final String[] strings;
    private OnSelectItemListener onSelectItemListener;

    public TvSpinner(Context context, String title, List<String> list, int index) {
        strings = list.toArray(new String[0]);
        alertDialog = new AlertDialog.Builder(context, R.style.TvSpinner)
                .setTitle(title)
                .setSingleChoiceItems(strings, index, this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        if (onSelectItemListener != null) {
            onSelectItemListener.onSelectItem(strings[which], which);
        }
    }

    public void setOnSelectItemListener(OnSelectItemListener onSelectItemListener) {
        this.onSelectItemListener = onSelectItemListener;
    }

    public void show() {
        alertDialog.show();
    }

    public void hide() {
        alertDialog.hide();
    }

    public void destroy() {
        alertDialog.cancel();
        onSelectItemListener = null;
    }

    public interface OnSelectItemListener {
        void onSelectItem(String item, int index);
    }

}
