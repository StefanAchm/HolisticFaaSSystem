package com.asi.hms.utils.cloudproviderutils.migrate;

import com.asi.hms.enums.RuntimeAWS;
import com.asi.hms.enums.RuntimeGCP;
import com.asi.hms.enums.RuntimeInterface;
import com.asi.hms.model.api.APIMigrationObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RuntimeMigrationHelperTest {

    private static Stream<Arguments> fromTo() {

        return Stream.of(

                // AWS -> GCP
                Arguments.of(RuntimeAWS.NODE_JS_20, RuntimeGCP.NODE_JS_20, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.NODE_JS_18, RuntimeGCP.NODE_JS_18, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.NODE_JS_16, RuntimeGCP.NODE_JS_16, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.PYTHON_3_12, RuntimeGCP.PYTHON_3_12, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.PYTHON_3_11, RuntimeGCP.PYTHON_3_11, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.PYTHON_3_10, RuntimeGCP.PYTHON_3_10, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.PYTHON_3_9, RuntimeGCP.PYTHON_3_9, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.PYTHON_3_8, RuntimeGCP.PYTHON_3_8, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.JAVA_21, RuntimeGCP.JAVA_21, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.JAVA_17, RuntimeGCP.JAVA_17, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.JAVA_11, RuntimeGCP.JAVA_11, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.JAVA_8, null, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.DOTNET_8, RuntimeGCP.DOTNET_CORE_8, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.DOTNET_7, null, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.DOTNET_6, RuntimeGCP.DOTNET_CORE_6, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.RUBY_3_2, RuntimeGCP.RUBY_3_2, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.PROVIDED_AL_2023, null, RuntimeGCP.class),
                Arguments.of(RuntimeAWS.PROVIDED_AL_2, null, RuntimeGCP.class),

                // GCP -> AWS
                Arguments.of(RuntimeGCP.NODE_JS_20, RuntimeAWS.NODE_JS_20, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.NODE_JS_18, RuntimeAWS.NODE_JS_18, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.NODE_JS_16, RuntimeAWS.NODE_JS_16, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.NODE_JS_14, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.NODE_JS_12, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.NODE_JS_10, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.PYTHON_3_12, RuntimeAWS.PYTHON_3_12, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.PYTHON_3_11, RuntimeAWS.PYTHON_3_11, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.PYTHON_3_10, RuntimeAWS.PYTHON_3_10, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.PYTHON_3_9, RuntimeAWS.PYTHON_3_9, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.PYTHON_3_8, RuntimeAWS.PYTHON_3_8, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.PYTHON_3_7, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.GO_1_22, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.GO_1_21, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.GO_1_20, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.GO_1_19, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.GO_1_18, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.GO_1_16, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.GO_1_13, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.GO_1_12, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.GO_1_11, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.JAVA_21, RuntimeAWS.JAVA_21, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.JAVA_17, RuntimeAWS.JAVA_17, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.JAVA_11, RuntimeAWS.JAVA_11, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.RUBY_3_2, RuntimeAWS.RUBY_3_2, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.RUBY_3_0, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.RUBY_2_7, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.RUBY_2_6, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.PHP_8_3, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.PHP_8_2, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.PHP_8_1, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.PHP_7_4, null, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.DOTNET_CORE_8, RuntimeAWS.DOTNET_8, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.DOTNET_CORE_6, RuntimeAWS.DOTNET_6, RuntimeAWS.class),
                Arguments.of(RuntimeGCP.DOTNET_CORE_3, null, RuntimeAWS.class)


        );

    }

    @ParameterizedTest
    @MethodSource("fromTo")
    void fromTo(RuntimeInterface source, RuntimeInterface target, Class<? extends RuntimeInterface> targetClass) {

        APIMigrationObject apiMigrationObject = RuntimeMigrationHelper.fromTo(source, targetClass);

        Assertions.assertEquals(target != null ? target.getRuntimeCode() : null, apiMigrationObject.getTarget());

    }

}