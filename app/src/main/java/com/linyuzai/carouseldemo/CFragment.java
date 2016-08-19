package com.linyuzai.carouseldemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class CFragment extends Fragment {

    private Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.layout_fragment, null);
        TextView textView = (TextView) view.findViewById(R.id.text);
        if (textView == null)
            Log.e("CFragment", "text view:null");
        else
            textView.setText(bundle.getString("String", "null"));
        return view;
    }
}
