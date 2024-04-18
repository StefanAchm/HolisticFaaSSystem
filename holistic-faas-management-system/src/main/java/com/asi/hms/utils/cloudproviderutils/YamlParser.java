package com.asi.hms.utils.cloudproviderutils;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIFunction;
import com.asi.hms.model.yaml.YamlFunctionDeployment;
import com.asi.hms.model.yaml.YamlFunctionImplementation;
import com.asi.hms.model.yaml.YamlFunctionType;
import com.asi.hms.model.yaml.YamlRoot;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

public class YamlParser {

    private YamlParser() {
        throw new IllegalStateException("Utility class");
    }

    static class CustomRepresenter extends Representer {

        public CustomRepresenter(DumperOptions options) {

            super(options);

            this.representers.put(YamlRoot.class, data -> represent(((YamlRoot) data).toMap()));
            this.representers.put(YamlFunctionType.class, data -> represent(((YamlFunctionType) data).toMap()));
            this.representers.put(YamlFunctionImplementation.class, data -> represent(((YamlFunctionImplementation) data).toMap()));
            this.representers.put(YamlFunctionDeployment.class, data -> represent(((YamlFunctionDeployment) data).toMap()));

        }

    }


    public static String writeYaml(List<APIFunction> functions) {

        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
//        options.setExplicitStart(Tag.NULL); // prevent writing of root tag

        Yaml yaml = new Yaml(new CustomRepresenter(options));

        YamlRoot yamlRoot = YamlRoot.fromApiFunction(functions);

        return yaml.dump(yamlRoot);

    }

    public static List<APIFunction> readYaml(Path path) {

        Yaml yaml = new Yaml();

        YamlRoot yamlRoot;

        try {
            yamlRoot = yaml.loadAs(new FileInputStream(path.toFile()), YamlRoot.class);
        } catch (FileNotFoundException e) {
            throw new HolisticFaaSException(e);
        }

        return YamlRoot.toApiFunction(yamlRoot);

    }

    public static List<APIFunction> readYaml(String yamlString) {

        Yaml yaml = new Yaml();

        YamlRoot yamlRoot = yaml.loadAs(yamlString, YamlRoot.class);

        return YamlRoot.toApiFunction(yamlRoot);

    }

//    public static void writeYaml_old(List<APIFunctionType> functions) {
//
//        Yaml yaml = new Yaml();
//
//        YamlRoot yamlRoot = YamlRoot.fromApiFunctionTypes(functions);
//
//        String output = yaml.dump(yamlRoot);
//
//        System.out.println(output);
//
//    }
//
//    public static List<APIFunction> readYaml(String yamlString) {
//
//        Yaml yaml = new Yaml();
//
//        YamlRoot yamlRoot = yaml.loadAs(yamlString, YamlRoot.class);
//
//        return null;
//
//    }


}
