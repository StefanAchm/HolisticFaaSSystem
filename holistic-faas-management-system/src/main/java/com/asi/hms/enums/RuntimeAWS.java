package com.asi.hms.enums;

import com.asi.hms.model.RuntimeGlobal;

/**
 * AWS Lambda Runtimes,
 * see <a href="https://docs.aws.amazon.com/lambda/latest/dg/lambda-runtimes.html">this</a> site for more information:
 */
public enum RuntimeAWS implements RuntimeInterface {

    // NodeJs
    NODE_JS_20("nodejs20.x"),
    NODE_JS_18("nodejs18.x"),
    @Deprecated NODE_JS_16("nodejs16.x"),

    // Python
    PYTHON_3_12("python3.12"),
    PYTHON_3_11("python3.11"),
    PYTHON_3_10("python3.10"),
    PYTHON_3_9("python3.9"),
    @Deprecated() PYTHON_3_8("python3.8"),

    // Java
    JAVA_21("java21"),
    JAVA_17("java17"),
    JAVA_11("java11"),
    JAVA_8("java8.al2"),

    // DotNet
    DOTNET_8("dotnet8"),
    @Deprecated DOTNET_7("dotnet7"),
    @Deprecated DOTNET_6("dotnet6"),

    // Ruby
    RUBY_3_2("ruby3.2"),

    // OS-only Runtimes
    PROVIDED_AL_2023("provided.al2023"),
    PROVIDED_AL_2("provided.al2");


    private final String runtimeCode;

    RuntimeAWS(String runtimeCode) {
        this.runtimeCode = runtimeCode;
    }

    public static RuntimeAWS fromCode(String runtimeString) {
        for (RuntimeAWS runtime : RuntimeAWS.values()) {
            if (runtime.runtimeCode.equals(runtimeString)) {
                return runtime;
            }
        }
        return null;
    }

    @Override
    public String getRuntimeCode() {
        return runtimeCode;
    }

    @Override
    public RuntimeGlobal getRuntimeGlobal() {

        // Create a new RuntimeGlobal object and set the language and version
        // E.g. for java11, set java as language and 11 as version

        String language = runtimeCode.split("\\d")[0];
        String version = runtimeCode.substring(language.length());

        // Remove all none alphanumeric characters from the version
        version = version.replaceAll("[^0-9]", "");

        RuntimeGlobal runtimeGlobal = new RuntimeGlobal();

        runtimeGlobal.setLanguage(language);
        runtimeGlobal.setVersion(version);


        return runtimeGlobal;

    }

}
