package ir.asta.samplebot.util.locale;

import ir.asta.samplebot.i18n.ResourceBundleCache;

import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class LocaleUtil {
    public static final String LOCALE_KEY = "locale_key";
    public static final String DIRECTION_KEY = "direction";
    public static final String BUNDLE_KEY = "msg";
    public static final String BUNDLE_NAME = "ApplicationResources";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String RIGHT_TEXTALIGN_KEY = "rightAlign";
    public static final String LINE_END_KEY = "lineEnd";
    public static final String LEFT_TEXTALIGN_KEY = "leftAlign";
    public static final String LINE_BEGIN_KEY = "lineBegin";
    public static final String IS_LOCALE_FARSI = "isLocaleFarsi";
    private static ThreadLocal<LocaleUtil.LocaleHolder> locale = new ThreadLocal();
    private static ResourceBundleCache resourceBundleCache = new ResourceBundleCache();

    public LocaleUtil() {
    }

    public static void setLocale(Locale newLocale) {
        if (newLocale != null) {
            getLocaleHolder().locale = newLocale;
        }

    }

    public static void setTimeZone(TimeZone newTimeZone) {
        if (newTimeZone != null) {
            getLocaleHolder().timeZone = newTimeZone;
        }

    }

    static LocaleUtil.LocaleHolder getLocaleHolder() {
        if (locale.get() == null) {
            locale.set(new LocaleUtil.LocaleHolder());
        }

        return (LocaleUtil.LocaleHolder)locale.get();
    }

    public static Locale getLocale() {
        if (getLocaleHolder().locale == null) {
            setLocale(new Locale("fa"));
        }

        return getLocaleHolder().locale;
    }

    public static TimeZone getTimeZone() {
        if (getLocaleHolder().timeZone == null) {
            setTimeZone(getDefaultTimeZone());
        }

        return getLocaleHolder().timeZone;
    }

    static TimeZone getDefaultTimeZone() {
        return TimeZone.getTimeZone("Asia/Tehran");
    }

    public static boolean isLocaleFarsi() {
        return getLocale() == null ? false : getLocale().getLanguage().toLowerCase().startsWith("fa");
    }

    public static ResourceBundle getBundle() {
        return getBundle(getBundleName());
    }

    public static ResourceBundle getBundle(String fileName) {
        return resourceBundleCache.getBundle(fileName, getLocale());
    }

    public static String getBundleName() {
        return "ApplicationResources";
    }

    public static String getText(String key) {
        String res = getTextWithDefault(key, (String)null);
        return res == null ? malformedKey(key) : res;
    }

    public static String getTextWithDefault(String key, String defaultValue) {
        try {
            String message = getBundle().getString(key);
            return message;
        } catch (MissingResourceException var4) {
            return defaultValue;
        }
    }

    private static String malformedKey(String key) {
        return "" + key + "";
    }

    public static String getText(String key, Object... arg) {
        try {
            if (arg == null || arg.length == 0) {
                return getText(key);
            }
        } catch (MissingResourceException var5) {
            return malformedKey(key);
        }

        String pattern = null;

        try {
            pattern = getText(key);
        } catch (MissingResourceException var4) {
            return malformedKey(key);
        }

        return (new MessageFormat(pattern, getLocale())).format(arg, new StringBuffer(), (FieldPosition)null).toString();
    }

    public static String getDirection() {
        return isLocaleFarsi() ? "rtl" : "ltr";
    }

    public static String getLeftTextAlign() {
        return isLocaleFarsi() ? "right" : "left";
    }

    public static String getRightTextAlign() {
        return isLocaleFarsi() ? "left" : "right";
    }

    public static String refinePersianChars(String paramValue) {
        if (paramValue == null) {
            return null;
        } else {
            StringBuilder filteredResult = new StringBuilder();

            for(int i = 0; i < paramValue.length(); ++i) {
                String character = paramValue.substring(i, i + 1);
                byte var5 = -1;
                switch(character.hashCode()) {
                    case 1609:
                        if (character.equals("ى")) {
                            var5 = 1;
                        }
                        break;
                    case 1632:
                        if (character.equals("٠")) {
                            var5 = 13;
                        }
                        break;
                    case 1633:
                        if (character.equals("١")) {
                            var5 = 14;
                        }
                        break;
                    case 1634:
                        if (character.equals("٢")) {
                            var5 = 15;
                        }
                        break;
                    case 1635:
                        if (character.equals("٣")) {
                            var5 = 16;
                        }
                        break;
                    case 1636:
                        if (character.equals("٤")) {
                            var5 = 17;
                        }
                        break;
                    case 1637:
                        if (character.equals("٥")) {
                            var5 = 18;
                        }
                        break;
                    case 1638:
                        if (character.equals("٦")) {
                            var5 = 19;
                        }
                        break;
                    case 1639:
                        if (character.equals("٧")) {
                            var5 = 20;
                        }
                        break;
                    case 1640:
                        if (character.equals("٨")) {
                            var5 = 21;
                        }
                        break;
                    case 1641:
                        if (character.equals("٩")) {
                            var5 = 22;
                        }
                        break;
                    case 1705:
                        if (character.equals("ک")) {
                            var5 = 2;
                        }
                        break;
                    case 1740:
                        if (character.equals("ی")) {
                            var5 = 0;
                        }
                        break;
                    case 1776:
                        if (character.equals("۰")) {
                            var5 = 3;
                        }
                        break;
                    case 1777:
                        if (character.equals("۱")) {
                            var5 = 4;
                        }
                        break;
                    case 1778:
                        if (character.equals("۲")) {
                            var5 = 5;
                        }
                        break;
                    case 1779:
                        if (character.equals("۳")) {
                            var5 = 6;
                        }
                        break;
                    case 1780:
                        if (character.equals("۴")) {
                            var5 = 7;
                        }
                        break;
                    case 1781:
                        if (character.equals("۵")) {
                            var5 = 8;
                        }
                        break;
                    case 1782:
                        if (character.equals("۶")) {
                            var5 = 9;
                        }
                        break;
                    case 1783:
                        if (character.equals("۷")) {
                            var5 = 10;
                        }
                        break;
                    case 1784:
                        if (character.equals("۸")) {
                            var5 = 11;
                        }
                        break;
                    case 1785:
                        if (character.equals("۹")) {
                            var5 = 12;
                        }
                }

                switch(var5) {
                    case 0:
                        filteredResult = filteredResult.append("ي");
                        break;
                    case 1:
                        filteredResult = filteredResult.append("ي");
                        break;
                    case 2:
                        filteredResult = filteredResult.append("ك");
                        break;
                    case 3:
                        filteredResult = filteredResult.append("0");
                        break;
                    case 4:
                        filteredResult = filteredResult.append("1");
                        break;
                    case 5:
                        filteredResult = filteredResult.append("2");
                        break;
                    case 6:
                        filteredResult = filteredResult.append("3");
                        break;
                    case 7:
                        filteredResult = filteredResult.append("4");
                        break;
                    case 8:
                        filteredResult = filteredResult.append("5");
                        break;
                    case 9:
                        filteredResult = filteredResult.append("6");
                        break;
                    case 10:
                        filteredResult = filteredResult.append("7");
                        break;
                    case 11:
                        filteredResult = filteredResult.append("8");
                        break;
                    case 12:
                        filteredResult = filteredResult.append("9");
                        break;
                    case 13:
                        filteredResult = filteredResult.append("0");
                        break;
                    case 14:
                        filteredResult = filteredResult.append("1");
                        break;
                    case 15:
                        filteredResult = filteredResult.append("2");
                        break;
                    case 16:
                        filteredResult = filteredResult.append("3");
                        break;
                    case 17:
                        filteredResult = filteredResult.append("4");
                        break;
                    case 18:
                        filteredResult = filteredResult.append("5");
                        break;
                    case 19:
                        filteredResult = filteredResult.append("6");
                        break;
                    case 20:
                        filteredResult = filteredResult.append("7");
                        break;
                    case 21:
                        filteredResult = filteredResult.append("8");
                        break;
                    case 22:
                        filteredResult = filteredResult.append("9");
                        break;
                    default:
                        filteredResult = filteredResult.append(character);
                }
            }

            return filteredResult.toString();
        }
    }

    public static void clearLocale() {
        locale.set(null);
    }

    static class LocaleHolder {
        TimeZone timeZone;
        Locale locale;

        LocaleHolder() {
        }
    }
}

