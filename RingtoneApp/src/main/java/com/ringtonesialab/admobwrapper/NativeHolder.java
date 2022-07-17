
package com.ringtonesialab.admobwrapper;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


class NativeHolder extends RecyclerView.ViewHolder {

    NativeHolder(ViewGroup adViewWrapper){
        super(adViewWrapper);
    }

    ViewGroup getAdViewWrapper() {
        return (ViewGroup)itemView;
    }

}
