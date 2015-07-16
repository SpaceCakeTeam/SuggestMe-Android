package it.suggestme.tutorialpages;

import android.support.v4.view.ViewPager;
import android.view.View;

<<<<<<< HEAD:app/src/main/java/me/federicomaggi/suggestme/tutorialpages/DepthPageTransformer.java
/**
 * Created by federicomaggi on 08/06/15.
 * © 2015 Federico Maggi. All rights reserved
 */
=======
>>>>>>> a35368c8c80cbc0d1bd4cf812b8f7ef61f4342e7:app/src/main/java/it/suggestme/tutorialpages/DepthPageTransformer.java
public class DepthPageTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.75f;
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        view.setTranslationX(-1 * view.getWidth() * position);
        if (position < -1) {
            view.setAlpha(0);
        } else if (position <= 0) {
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        } else if (position <= 1) {
            view.setAlpha(1 - position);
            view.setTranslationX(pageWidth * -position);
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else {
            view.setAlpha(0);
        }
    }
}