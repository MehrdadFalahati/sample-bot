package ir.asta.samplebot.i18n;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
public abstract class AbstractApplicationResources extends ResourceBundle {
    Map<String, Object> messages;

    public AbstractApplicationResources() {
        log.debug("Creating {} with pattern: {}", this.getClass().getName(), Arrays.toString(this.getResourcePattern()));
        this.messages = new HashMap();
        this.fillMessages(this.messages, this.getResources());
        this.fillMessages(this.messages, this.getCustomizationResources());
    }

    protected void fillMessages(Map<String, Object> messages, Set<Resource> resources) {
        Iterator var3 = resources.iterator();

        while(var3.hasNext()) {
            Resource resource = (Resource)var3.next();

            try {
                log.debug("loading bundle {}", resource.toString());
                Properties p = new Properties();
                p.load(resource.getInputStream());
                Iterator var6 = p.entrySet().iterator();

                while(var6.hasNext()) {
                    Entry<Object, Object> entry = (Entry)var6.next();
                    log.debug("addning property [{}:{}]", entry.getKey(), entry.getValue());
                    messages.put((String)entry.getKey(), entry.getValue());
                }
            } catch (IOException var8) {
                log.error(var8.getMessage(), var8);
            }
        }

    }

    protected Set<Resource> getResources() {
        return this.findResources(this.getResourcePattern());
    }

    protected Set<Resource> getCustomizationResources() {
        return this.findResources(this.getCustomizationResourcePattern());
    }

    protected Set<Resource> findResources(String[] resoucePatterns) {
        HashSet result = new HashSet();

        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            String[] var4 = resoucePatterns;
            int var5 = resoucePatterns.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String pattern = var4[var6];
                Resource[] resources = resolver.getResources(pattern);
                if (resources != null) {
                    Resource[] var9 = resources;
                    int var10 = resources.length;

                    for(int var11 = 0; var11 < var10; ++var11) {
                        Resource resource = var9[var11];
                        result.add(resource);
                    }
                }
            }
        } catch (IOException var13) {
            log.error(var13.getMessage(), var13);
        }

        return result;
    }

    protected abstract String[] getResourcePattern();

    protected abstract String[] getCustomizationResourcePattern();

    protected Object handleGetObject(String key) {
        Object object = this.messages.get(key);
        if (object == null && log.isDebugEnabled()) {
            log.debug("There is no message with key [{}]", key);
        }

        return object;
    }

    public Enumeration<String> getKeys() {
        return Collections.enumeration(this.messages.keySet());
    }
}
