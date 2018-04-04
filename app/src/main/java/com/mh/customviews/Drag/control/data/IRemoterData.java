package com.mh.customviews.Drag.control.data;


/**
 * Created by munin on 2018/3/31.
 */

public interface IRemoterData {
   RemoterData.Point getType();

    int getTextResId();


    int getPicResId();

    IRemoterTag getTag();

}
