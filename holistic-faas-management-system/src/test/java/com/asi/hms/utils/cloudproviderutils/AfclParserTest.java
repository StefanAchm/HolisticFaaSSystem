package com.asi.hms.utils.cloudproviderutils;

import at.uibk.dps.afcl.Workflow;
import at.uibk.dps.afcl.utils.Utils;
import com.asi.hms.model.api.APIWorkflow;
import com.asi.hms.utils.cloudproviderutils.afcl.AfclParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AfclParserTest {

    public static void main(String[] args) {

        String base = "src/main/resources/afcl_workflows/monteCarlo-main/";
        String src = "AFCL/monteCarlo.yaml";
        String jsonSchema = "AFCL/input_monteCarlo.json";

        try {

            Workflow workflow = Utils.readYAML(
                    base + src,
                    base + jsonSchema
            );


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void testGenom() {

        APIWorkflow workflow = AfclParser.getWorkflow("src/main/resources/afcl_workflows/genome/genome.yaml");

        Assertions.assertEquals(5, workflow.getFunctions().size());

    }

}
