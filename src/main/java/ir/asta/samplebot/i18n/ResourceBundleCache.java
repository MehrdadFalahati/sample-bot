package ir.asta.samplebot.i18n;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.util.ClassUtils;

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ResourceBundleCache implements BeanClassLoaderAware {
    private ClassLoader bundleClassLoader;
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private final Map<String, Map<Locale, ResourceBundle>> cachedResourceBundles = new ConcurrentHashMap();

    public ResourceBundleCache() {
    }

    public ResourceBundle getBundle(String basename, Locale locale) {
        ResourceBundle res = this.loadFromCache(basename, locale);
        if (res != null) {
            return res;
        } else {
            Map var4 = this.cachedResourceBundles;
            synchronized(this.cachedResourceBundles) {
                res = this.loadFromCache(basename, locale);
                if (res != null) {
                    return res;
                } else {
                    ResourceBundle var10000;
                    try {
                        ResourceBundle bundle = this.doGetBundle(basename, locale);
                        Map<Locale, ResourceBundle> localeMap = (Map)this.cachedResourceBundles.get(basename);
                        if (localeMap == null) {
                            localeMap = new ConcurrentHashMap();
                            this.cachedResourceBundles.put(basename, localeMap);
                        }

                        ((Map)localeMap).put(locale, bundle);
                        var10000 = bundle;
                    } catch (MissingResourceException var8) {
                        if (log.isWarnEnabled()) {
                            log.warn("ResourceBundle [" + basename + "] not found: " + var8.getMessage());
                        }

                        return null;
                    }

                    return var10000;
                }
            }
        }
    }

    ResourceBundle loadFromCache(String basename, Locale locale) {
        Map<Locale, ResourceBundle> localeMap = (Map)this.cachedResourceBundles.get(basename);
        if (localeMap != null) {
            ResourceBundle bundle = (ResourceBundle)localeMap.get(locale);
            if (bundle != null) {
                return bundle;
            }
        }

        return null;
    }

    protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
        return ResourceBundle.getBundle(basename, locale, this.getBundleClassLoader());
    }

    public void setBundleClassLoader(ClassLoader classLoader) {
        this.bundleClassLoader = classLoader;
    }

    protected ClassLoader getBundleClassLoader() {
        return this.bundleClassLoader != null ? this.bundleClassLoader : this.beanClassLoader;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader();
    }
}
