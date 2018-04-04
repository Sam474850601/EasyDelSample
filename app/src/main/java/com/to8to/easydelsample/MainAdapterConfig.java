package com.to8to.easydelsample;

import com.to8to.easydel_annotation.Adapter;

/**
 * Created by same.li on 2018/4/4.
 */

@Adapter(
        holders = {
                "com.to8to.easydelsample.ContentHolder",
                "com.to8to.easydelsample.ContentHolder2",
                "com.to8to.easydelsample.ContentHolder3"
        },
        className = "MainAdapter",
        extend = "android.support.v7.widget.RecyclerView.Adapter"
)

public class MainAdapterConfig {


}
