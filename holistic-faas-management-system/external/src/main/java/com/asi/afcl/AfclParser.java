package com.asi.afcl;

import at.uibk.dps.afcl.Workflow;
import at.uibk.dps.afcl.utils.Utils;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import java.io.IOException;

public class AfclParser {


    public static void main(String[] args) {

        String base = "src/main/resources/afcl_workflows/monteCarlo-main/";
        String src = "AFCL/monteCarlo.yaml";
        String jsonSchema = "AFCL/input_monteCarlo.json";

        try {

            Workflow workflow = Utils.readYAML(
                    base + src,
                    base + jsonSchema
            );

            System.out.println(workflow);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        }

    }


}
