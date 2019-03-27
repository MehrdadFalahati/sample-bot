import ir.asta.samplebot.i18n.AbstractApplicationResources;

public class ApplicationResources extends AbstractApplicationResources {
    public ApplicationResources() {
    }

    protected String[] getResourcePattern() {
        return new String[]{"classpath*:com/**/*-msg.properties", "classpath*:com/**/*-msg_en.properties", "classpath*:ir/**/*-msg.properties", "classpath*:ir/**/*-msg_en.properties", "classpath*:org/**/*-msg.properties", "classpath*:org/**/*-msg_en.properties", "classpath*:**/*-msg.properties", "classpath*:**/*-msg_en.properties"};
    }

    protected String[] getCustomizationResourcePattern() {
        return new String[]{"classpath*:customize-msg.properties", "classpath*:customize-msg_en.properties"};
    }
}