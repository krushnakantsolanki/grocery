package com.fameget.dreamgroceries.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.LruCache;

/**
 * Created by Anurag on 24-06-2015.
 * <p>
 * Caches the created typeface object in the lru cache so that it does not need to create same typeface objects everytime and can use the existing cached typeface of the same type.
 */
public class TypefaceCache {
    /**
     * An <code>LruCache</code> for previously loaded typefaces.
     */
    private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);

    /**
     * @param context
     * @param fontName - name of the font (with extension) that is in your assets folder.
     * @return
     */
    public static Typeface fetchTypeface(Context context, String fontName) {
        Typeface mTypeface = sTypefaceCache.get(fontName);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), fontName);
            //cache the loaded typeface
            sTypefaceCache.put(fontName, mTypeface);
        }
        return mTypeface;
    }
}
