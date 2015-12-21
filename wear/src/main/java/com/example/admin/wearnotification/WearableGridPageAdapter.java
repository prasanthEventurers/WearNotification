package com.example.admin.wearnotification;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Admin on 12/11/2015.
 */
public class WearableGridPageAdapter extends FragmentGridPagerAdapter {

    public Context mContext;
    public page[][] pages = {
            {        new page("title 1","description about the page 1"),
                    new page("title 1","desc 2"),
                    new page("title 1","desc 3")
            },
            {       new page("title 2","desc 1"),
                    new page("title 2","desc 2"),
                    new page("title 2","desc 3")
            },
            {       new page("title 3","description about the page 1"),
                    new page("title 3","desc 2"),
                    new page("title 3","desc 3")
            },};
    public int[] bgImg = {R.drawable.wear_background_two,R.drawable.pager_background_two,R.drawable.pager_background_three};

    public class page{
        String pageTitle;
        String pageDesc;
        int cardGravity = Gravity.BOTTOM;

        public page(String pageTitle,String pageDesc) {
            this.pageTitle = pageTitle;
            this.pageDesc = pageDesc;
        }
    }


    public WearableGridPageAdapter(Context applicationContext, FragmentManager fm) {
        super(fm);
        this.mContext = applicationContext;
    }

    @Override
    public Fragment getFragment(int i, int i1) {
        page page = pages[i][i1];
        String strTile = page.pageTitle;
        String strDesc = page.pageDesc;
        CardFragment cardFragment = CardFragment.create(strTile,strDesc);
        cardFragment.setCardGravity(page.cardGravity);
        cardFragment.setExpansionEnabled(true);
//      cardFragment.setExpansionDirection(cardFragment.EXPAND_DOWN);
        return cardFragment;
    }

    @Override
    public int getRowCount() {
        return 3;
    }

    @Override
    public int getColumnCount(int i) {
        return 3;
    }

//    @Override
//    public Drawable getBackgroundForRow(int row) {
//        return mContext.getDrawable(bgImg[row]);
//    }

    @Override
    public Drawable getBackgroundForPage(int row, int column) {
        return mContext.getDrawable(bgImg[column]);
    }
}
