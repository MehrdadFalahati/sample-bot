package ir.asta.samplebot.i18n;

import ir.asta.samplebot.util.locale.LocaleUtil;
import org.springframework.context.support.AbstractMessageSource;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationResourcesMessageSource extends AbstractMessageSource {
    Map<Locale, ResourceBundle> bundleCache = new ConcurrentHashMap();

    public ApplicationResourcesMessageSource() {
    }

    protected MessageFormat resolveCode(String code, Locale locale) {
        ResourceBundle bundle = this.getBundle(locale);
        return new MessageFormat(bundle.getString(code), locale);
    }

    private ResourceBundle getBundle(Locale locale) {
        ResourceBundle b = (ResourceBundle)this.bundleCache.get(locale);
        if (b == null) {
            b = LocaleUtil.getBundle("ApplicationResources");
            this.bundleCache.put(locale, b);
        }

        return b;
    }
}