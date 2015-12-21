package com.example.admin.wearnotification;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Admin on 12/10/2015.
 */
public class WearableItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener {
    CircledImageView listImg;
    TextView listTxt;

    float mFadedText;
    int mFadedCircleColor;
    int mChosenCircleColor;

    public WearableItemLayout(Context context) {
        super(context);
        mChosenCircleColor = getResources().getColor(R.color.blue);
        mFadedCircleColor = getResources().getColor(R.color.grey);
    }

    public WearableItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WearableItemLayout(Context context, AttributeSet attrs, int defStyleAttr, float mFadedText, int mFadedCircleColor, int mChosenCircleColor) {
        super(context, attrs, defStyleAttr);
        this.mFadedText = mFadedText;
        this.mFadedCircleColor = mFadedCircleColor;
        this.mChosenCircleColor = mChosenCircleColor;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        listImg = (CircledImageView)findViewById(R.id.list_image);
        listTxt = (TextView)findViewById(R.id.list_text);

    }

    @Override
    public void onCenterPosition(boolean b) {
        listTxt.setAlpha(1f);
//        ((GradientDrawable) listImg.getDrawable()).setColor(mChosenCircleColor);
//        listImg.setBackground(getResources().getDrawable(R.drawable.back_circle,null));
        listImg.setCircleRadius(37);
        listImg.setCircleColor(getResources().getColor(R.color.blue));
        listImg.setCircleRadiusPressed(32);
//        listImg.setCircleBorderColor(getResources().getColor(R.color.blue));
        listImg.animate().scaleX(1f).scaleY(1f).alpha(1);
        listTxt.animate().scaleX(1f).scaleY(1f).alpha(1);
        listTxt.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onNonCenterPosition(boolean b) {
        listTxt.setAlpha(0.5f);
//        ((GradientDrawable) listImg.getDrawable()).setColor(mFadedCircleColor);
//        listImg.setBackground(getResources().getDrawable(R.drawable.back_grey_circle,null));
        listImg.setCircleRadius(25);
        listImg.setCircleColor(getResources().getColor(R.color.grey));
        listTxt.setTextColor(getResources().getColor(R.color.grey));
//        listImg.setCircleBorderColor(getResources().getColor(R.color.black));
//        listImg.animate().scaleX(0.8f).scaleY(0.8f).alpha(0.6f);
//        listTxt.animate().scaleX(0.8f).scaleY(0.8f).alpha(0.6f);
    }


}
