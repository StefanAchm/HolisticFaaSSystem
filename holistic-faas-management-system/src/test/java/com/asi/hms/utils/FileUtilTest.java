package com.asi.hms.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {

    // afcl_workflows\genome\genomeAwsFunctions\genome_individual.py

    @Test
    void zipFile() throws IOException {

        Path inputPath = Paths.get("src/main/resources/afcl_workflows/genome/genomeAwsFunctions/genome_individual.py");
        Path outputPath = Paths.get("src/main/resources/afcl_workflows/genome/genomeAwsFunctions/genome_individual.zip");

        Files.createFile(outputPath);

        FileUtil.zipFiles(List.of(inputPath), outputPath);

    }


}