package com.example.numberguessinggame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context myContext;
    ArrayList<String> myNumbers;
    ArrayList<Boolean> numberGuessed;
    int hintPosition;
    boolean clearNames;
    LayoutInflater inflater;

    public  CustomAdapter(Context context, ArrayList<String> numbers, ArrayList<Boolean> numberGuessed
    , int hintPosition, boolean clearNumbers)
    {
        myContext = context;
        myNumbers = numbers;
        this.numberGuessed = numberGuessed;
        this.hintPosition = hintPosition;
        this.clearNames = clearNumbers;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return myNumbers.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.activity_my_gridview, null);
        TextView tvNumber = (TextView) view.findViewById(R.id.tvNumber);
        tvNumber.setText(myNumbers.get(position));
        if(clearNames==true)
        {
            tvNumber.setBackgroundResource(R.color.dark_primary_color);
            tvNumber.setText("");
        }
        else if(hintPosition != -1 && hintPosition == position)
        {
            tvNumber.setBackgroundResource(R.color.dark_primary_color);
        }
        else if(numberGuessed.get(position) == true)
        {
            tvNumber.setBackgroundResource(R.color.dark_primary_color);
            tvNumber.setText("");
        }

        return view;
    }
}
