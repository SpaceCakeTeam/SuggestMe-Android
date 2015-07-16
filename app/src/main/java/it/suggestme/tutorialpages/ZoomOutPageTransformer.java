package it.suggestme.tutorialpages;

import android.support.v4.view.ViewPager;
import android.view.View;

<<<<<<< HEAD:app/src/main/java/me/federicomaggi/suggestme/tutorialpages/ZoomOutPageTransformer.java
/**
 * Created by federicomaggi on 08/06/15.
 * © 2015 Federico Maggi. All rights reserved
 */
=======
>>>>>>> a35368c8c80cbc0d1bd4cf812b8f7ef61f4342e7:app/src/main/java/it/suggestme/tutorialpages/ZoomOutPageTransformer.java
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) {
            view.setAlpha(0);
        } else if (position <= 1) {
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            view.setAlpha(MIN_ALPHA+(scaleFactor - MIN_SCALE)/(1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else {
            view.setAlpha(0);
        }
    }
}