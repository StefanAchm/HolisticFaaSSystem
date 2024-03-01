package com.asi.hms.enums;

/**
 * GCP Lambda Runtimes,
 * see <a href="https://cloud.google.com/functions/docs/concepts/execution-environment?hl=de">this</a> for more information
 */
public enum RuntimeGCP implements RuntimeInterface {

    // NodeJS
    NODE_JS_20("nodejs20"),
    NODE_JS_18("nodejs18"),
    NODE_JS_16("nodejs16"),
    NODE_JS_14("nodejs14"),
    NODE_JS_12("nodejs12"),
    NODE_JS_10("nodejs10"),
    @Deprecated NODE_JS_8("nodejs8"),
    @Deprecated NODE_JS_6("nodejs6"),

    // Python
    PYTHON_3_12("python312"),
    PYTHON_3_11("python311"),
    PYTHON_3_10("python310"),
    PYTHON_3_9("python39"),
    PYTHON_3_8("python38"),
    PYTHON_3_7("python37"),

    // GO
    GO_1_22("go122"),
    GO_1_21("go121"),
    GO_1_20("go120"),
    GO_1_19("go119"),
    GO_1_18("go118"),
    GO_1_16("go116"),
    GO_1_13("go113"),
    GO_1_12("go112"),
    GO_1_11("go111"),

    // JAVA
    JAVA_21("java21"),
    JAVA_17("java17"),
    JAVA_11("java11"),

    // RUBY
    RUBY_3_2("ruby32"),
    RUBY_3_0("ruby30"),
    RUBY_2_7("ruby27"),
    RUBY_2_6("ruby26"),

    // PHP
    PHP_8_3("php83"),
    PHP_8_2("php82"),
    PHP_8_1("php81"),
    PHP_7_4("php74"),

    // DOTNET_CORE
    DOTNET_CORE_8("dotnet8"),
    DOTNET_CORE_6("dotnet6"),
    DOTNET_CORE_3("dotnet3");

    private final String runtimeString;

    RuntimeGCP(String runtimeString) {
        this.runtimeString = runtimeString;
    }


    @Override
    public String getRuntimeString() {
        return runtimeString;
    }

}
