package com.robthecornallgmail.memarket.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.robthecornallgmail.memarket.Activities.MainActivity;
import com.robthecornallgmail.memarket.R;
import com.robthecornallgmail.memarket.Util.MemeObject;
import com.robthecornallgmail.memarket.Util.OrderRow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rob on 12/09/17.
 */

public class OrdersListFragment  extends Fragment {
    String TAG = "OrdersListFragment";
    List<OrderRow> mOrderRows;
    OrdersListAdapter mAdapter;
    OnOrdersListFragmentInteractionListener mListener;
    TextView mTitle;
    ImageView mIcon;

    public OrdersListFragment() {
        mOrderRows = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new OrdersListAdapter(mOrderRows, mListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_list, container, false);
//        view.setBackgroundColor(getResources().getColor(R.color.colorMyGrey)); /* covers up drop down arrow */
        mTitle = (TextView) view.findViewById(R.id.orders_text_title);
        mIcon = (ImageView) view.findViewById(R.id.orders_meme_icon);
        RecyclerView rview = (RecyclerView) view.findViewById(R.id.orders_list_rv);
        rview.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rview.setAdapter(mAdapter);

//        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
//        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
//        p.y = 50;
//        getDialog().getWindow().setAttributes(p);

        return view;
    }

    public void updateList(HashMap<Integer, OrderRow> orderIDtoRow, Integer selectedMemeID, MemeObject memeObject, String sell) {
        mOrderRows.clear();
        mTitle.setText(sell + " orders for " + memeObject.mName);
        String iconName = "icon_" + memeObject.mName.replaceAll(" ", "_").toLowerCase();

        int iconId = getContext().getResources().getIdentifier(iconName, "drawable", MainActivity.PACKAGE_NAME);
        try {
            Picasso.with(getContext()).load(iconId).centerCrop().fit().into(mIcon);
        } catch (Exception e ) {
            Log.v(TAG,e.toString());
        }

        Log.v(TAG, "updateListCalled");
        for(Map.Entry<Integer, OrderRow> order: orderIDtoRow.entrySet()) {
            order.getValue().mOrderID = order.getKey();
            if(order.getValue().mMemeID.equals(selectedMemeID)) {
                mOrderRows.add(order.getValue());
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    public void clearList() {
        mOrderRows.clear();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOrdersListFragmentInteractionListener) {
            mListener = (OnOrdersListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOrdersListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnOrdersListFragmentInteractionListener {
        void onOrdersListFragmentInteraction(OrderRow orderRow);
    }
}