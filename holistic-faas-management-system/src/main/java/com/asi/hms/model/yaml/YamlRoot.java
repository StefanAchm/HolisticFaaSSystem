package com.asi.hms.model.yaml;

import com.asi.hms.enums.Provider;
import com.asi.hms.model.api.APIFunctionFlat;
import com.asi.hms.model.api.APIFunctionDeployment;
import com.asi.hms.model.api.APIFunctionImplementation;
import com.asi.hms.model.api.APIFunctionType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlRoot {

    private String date;

    private List<YamlFunctionType> functions;

    public YamlRoot() {
        this.functions = new ArrayList<>();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<YamlFunctionType> getFunctions() {
        return functions;
    }

    public void setFunctions(List<YamlFunctionType> functions) {
        this.functions = functions;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        map.put("functions", functions);
        return map;
    }

    public static YamlRoot fromApiFunction(List<APIFunctionFlat> functions) {

        YamlRoot yamlRoot = new YamlRoot();
        yamlRoot.setDate(LocalDate.now().toString());

        for (APIFunctionFlat function : functions) {

            APIFunctionType functionType = function.getFunctionType();
            APIFunctionImplementation functionImplementation = function.getFunctionImplementation();
            APIFunctionDeployment functionDeployment = function.getFunctionDeployment();


            YamlFunctionType yamlFunctionType = yamlRoot.functions
                    .stream()
                    .filter(t -> t.getName().equals(functionType.getName()))
                    .findFirst()
                    .orElseGet(() -> {
                        YamlFunctionType newFunctionType = new YamlFunctionType();
                        newFunctionType.setName(functionType.getName());
                        yamlRoot.functions.add(newFunctionType);
                        return newFunctionType;
                    });

            YamlFunctionImplementation yamlFunctionImplementation = yamlFunctionType.getImplementations()
                    .stream()
                    .filter(i -> i.getPath().equals(functionImplementation.getFilePath()))
                    .findFirst()
                    .orElseGet(() -> {
                        YamlFunctionImplementation newFunctionImplementation = new YamlFunctionImplementation();
                        newFunctionImplementation.setPath(functionImplementation.getFilePath());
                        yamlFunctionType.getImplementations().add(newFunctionImplementation);
                        return newFunctionImplementation;
                    });

            YamlFunctionDeployment yamlFunctionDeployment = YamlFunctionDeployment.fromApiFunctionDeployment(functionDeployment);
            yamlFunctionImplementation.getDeployments().add(yamlFunctionDeployment);

        }

        return yamlRoot;

    }

    public static List<APIFunctionType> toApiFunctionTypes(YamlRoot yamlRoot) {


        List<APIFunctionType> functionTypes = new ArrayList<>();

        for(YamlFunctionType yamlFunctionType : yamlRoot.functions) {

            APIFunctionType functionType = new APIFunctionType();
            functionType.setName(yamlFunctionType.getName());
            functionTypes.add(functionType);

            for(YamlFunctionImplementation yamlFunctionImplementation : yamlFunctionType.getImplementations()) {

                APIFunctionImplementation functionImplementation = new APIFunctionImplementation();
                functionImplementation.setFilePath(yamlFunctionImplementation.getPath());
                functionType.getFunctionImplementations().add(functionImplementation);

                for(YamlFunctionDeployment yamlFunctionDeployment : yamlFunctionImplementation.getDeployments()) {

                    APIFunctionDeployment functionDeployment = new APIFunctionDeployment();
                    functionDeployment.setHandler(yamlFunctionDeployment.getHandler());
                    functionDeployment.setMemory(yamlFunctionDeployment.getMemory());
                    functionDeployment.setProvider(Provider.valueOf(yamlFunctionDeployment.getProvider()));
                    functionDeployment.setRegion(yamlFunctionDeployment.getRegion());
                    functionDeployment.setRuntime(yamlFunctionDeployment.getRuntime());
                    functionDeployment.setTimeoutSecs(yamlFunctionDeployment.getTimeoutSecs());
                    functionImplementation.getFunctionDeployments().add(functionDeployment);

                }

            }

        }

        return functionTypes;

    }

    public static List<APIFunctionFlat> toApiFunction(YamlRoot yamlRoot) {

        List<APIFunctionFlat> functions = new ArrayList<>();

        for(YamlFunctionType yamlFunctionType : yamlRoot.functions) {

            for(YamlFunctionImplementation yamlFunctionImplementation : yamlFunctionType.getImplementations()) {

                for(YamlFunctionDeployment yamlFunctionDeployment : yamlFunctionImplementation.getDeployments()) {

                    APIFunctionFlat function = new APIFunctionFlat();

                    APIFunctionType functionType = new APIFunctionType();
                    functionType.setName(yamlFunctionType.getName());
                    function.setFunctionType(functionType);

                    APIFunctionImplementation functionImplementation = new APIFunctionImplementation();
                    functionImplementation.setFilePath(yamlFunctionImplementation.getPath());
                    function.setFunctionImplementation(functionImplementation);

                    APIFunctionDeployment functionDeployment = new APIFunctionDeployment();
                    functionDeployment.setHandler(yamlFunctionDeployment.getHandler());
                    functionDeployment.setMemory(yamlFunctionDeployment.getMemory());
                    functionDeployment.setProvider(Provider.valueOf(yamlFunctionDeployment.getProvider()));
                    functionDeployment.setRegion(yamlFunctionDeployment.getRegion());
                    functionDeployment.setRuntime(yamlFunctionDeployment.getRuntime());
                    functionDeployment.setTimeoutSecs(yamlFunctionDeployment.getTimeoutSecs());

                    function.setFunctionDeployment(functionDeployment);

                    functions.add(function);

                }

            }

        }

        return functions;

    }

}
