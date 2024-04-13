package com.asi.hms.enums;

import com.asi.hms.model.RuntimeGlobal;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public interface RuntimeInterface {

    class RuntimeSerializer extends StdSerializer<RuntimeInterface> {

        public RuntimeSerializer() {
            super(RuntimeInterface.class);
        }

        @Override
        public void serialize(RuntimeInterface runtimeInterface,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {

            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("value", runtimeInterface.name());
            jsonGenerator.writeStringField("runtimeCode", runtimeInterface.getRuntimeCode());

            jsonGenerator.writeEndObject();
        }

    }

    String name();

    String getRuntimeCode();

    RuntimeGlobal getRuntimeGlobal();

}
