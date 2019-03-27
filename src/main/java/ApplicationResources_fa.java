public class ApplicationResources_fa extends ApplicationResources {
    public ApplicationResources_fa() {
    }

    protected String[] getResourcePattern() {
        return new String[]{"classpath*:com/**/*-msg_fa.properties", "classpath*:ir/**/*-msg_fa.properties", "classpath*:org/**/*-msg_fa.properties", "classpath*:**/*-msg_fa.properties"};
    }

    protected String[] getCustomizationResourcePattern() {
        return new String[]{"classpath*:customize-msg_fa.properties"};
    }
}
