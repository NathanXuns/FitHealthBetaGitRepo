package com.eugene.fithealthmaingit.FitBit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eugene.fithealthmaingit.R;

import java.util.ArrayList;

public class FitBitActivityAdapter extends ArrayAdapter<FitBitActivityResult> {

    private final Context mContext;
    private final ArrayList<FitBitActivityResult> mItem;

    public FitBitActivityAdapter(Context context, ArrayList<FitBitActivityResult> itemsArrayList) {
        super(context, R.layout.list_search_row, itemsArrayList);
        this.mContext = context;
        this.mItem = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_search_row, parent, false);
        TextView mFoodName = (TextView) v.findViewById(R.id.food_name);
        TextView mBrand = (TextView) v.findViewById(R.id.food_brand);
        mFoodName.setText(mItem.get(position).getName());
        mBrand.setText(mItem.get(position).getCalories());
        return v;
    }
}
