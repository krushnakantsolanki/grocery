package com.fameget.dreamgroceries

import android.app.Application


class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
      /*  ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/gtw.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .setFontMapper { font -> font }
                            .addCustomViewWithSetTypeface(CustomViewWithTypefaceSupport::class.java)
                            .addCustomStyle(TextField::class.java, R.attr.textFieldStyle)
                            .build()
                    )
                )
                .build()
        )
*/

       /* ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("MonReg.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .setFontMapper()
                            .addCustomViewWithSetTypeface(CustomViewWithTypefaceSupport.class)
                                .addCustomStyle(TextField.class, R.attr.textFieldStyle)
                            .build()
                    )
                )
                .build()
        )*/
    }
}