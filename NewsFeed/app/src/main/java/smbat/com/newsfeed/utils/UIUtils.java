package smbat.com.newsfeed.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import smbat.com.newsfeed.R;

public class UIUtils {

    private static final String HEX_COLOR_FORMATTER = "#%X";

    private UIUtils() {
        throw new UnsupportedOperationException();
    }

    public static Drawable getRandomColoredDrawable(final String sectionName,
                                                    final Context context) {
        final Drawable drawable =
                ContextCompat.getDrawable(context, R.drawable.catgory_rounded_drawable);
        try {
            final float[] hsv = new float[3];
            int color = Color.parseColor(String.format(HEX_COLOR_FORMATTER, sectionName.hashCode()));
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.75f; // make color darker
            color = Color.HSVToColor(hsv);
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
            }
            return drawable;
        } catch (RuntimeException e) {
            Log.d("UnknownColorException",
                    "Skipping setting background color to news category...");
        }
        return drawable;
    }
}
