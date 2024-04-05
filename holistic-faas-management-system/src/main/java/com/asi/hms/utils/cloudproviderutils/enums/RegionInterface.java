package com.asi.hms.utils.cloudproviderutils.enums;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public interface RegionInterface {

    class RegionSerializer extends StdSerializer<RegionInterface> {

        public RegionSerializer() {
            super(RegionInterface.class);
        }

        @Override
        public void serialize(RegionInterface regionInterface,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {

            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("value", regionInterface.name());
            jsonGenerator.writeStringField("regionCode", regionInterface.getRegionCode());
            jsonGenerator.writeStringField("regionName", regionInterface.getRegionName());

            jsonGenerator.writeEndObject();
        }

    }

    String name();

    String getRegionCode();

    String getRegionName();

    RegionGlobal getRegionGlobal();

}
