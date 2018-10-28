package smbat.com.newsfeed.utils;

public class UIUtils {

    private UIUtils() {
        throw new UnsupportedOperationException();
    }

    public static String getRGBColor(final int hashCode) {
        return Integer.toHexString(((hashCode >> 24) & 0xFF)) +
                Integer.toHexString(((hashCode >> 16) & 0xFF)) +
                Integer.toHexString(((hashCode >> 8) & 0xFF));
    }
}
