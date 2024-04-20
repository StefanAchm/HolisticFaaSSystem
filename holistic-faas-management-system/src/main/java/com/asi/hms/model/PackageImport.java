package com.asi.hms.model;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.APIFunctionImplementation;
import com.asi.hms.model.api.APIFunctionType;
import com.asi.hms.utils.FileUtil;
import com.asi.hms.utils.cloudproviderutils.YamlParser;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class PackageImport {

    private File root;
    private File yamlFile;
    private List<APIFunctionType> apiFunctionTypes;

    public static PackageImport getPackageImport(Path rootPath) {

        PackageImport packageImport = new PackageImport();

        // Step 1: Check if the root path is a directory
        if(!rootPath.toFile().isDirectory()) {
            throw new HolisticFaaSException("Root path is not a directory");
        }

        packageImport.root = rootPath.toFile();

        // Step 2: Check if the root path contains a YAML file
        packageImport.yamlFile = FileUtil.getFileWithExtension(packageImport.root, List.of("yaml", "yml"));

        // Step 3: Parse the YAML file
        packageImport.apiFunctionTypes = YamlParser.readYamlAsTree(packageImport.yamlFile.toPath());

        // Step 4: Check if the functionimplementations of the yaml file are present in the root directory

        updateFilePaths(packageImport, packageImport.root);

        return packageImport;

    }

    public File getRoot() {
        return root;
    }

    public File getYamlFile() {
        return yamlFile;
    }

    public List<APIFunctionType> getApiFunctionTypes() {
        return apiFunctionTypes;
    }

    private static void updateFilePaths(PackageImport packageImport, File rootFile) {

        List<APIFunctionImplementation> apiFunctionImplementations = packageImport.apiFunctionTypes
                .stream()
                .flatMap(apiFunctionType -> apiFunctionType.getFunctionImplementations().stream())
                .toList();

        for(APIFunctionImplementation apiFunctionImplementation : apiFunctionImplementations) {

            File functionImplementationFile = new File(packageImport.root, apiFunctionImplementation.getFilePath());

            if(!functionImplementationFile.exists()) {
                throw new HolisticFaaSException("Function implementation file not found: " + functionImplementationFile);
            }

            String newFilePath = rootFile.getAbsolutePath() + File.separator + apiFunctionImplementation.getFilePath();

            apiFunctionImplementation.setFilePath(newFilePath);

        }

    }

}
