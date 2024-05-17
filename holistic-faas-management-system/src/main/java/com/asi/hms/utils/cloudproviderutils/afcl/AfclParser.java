package com.asi.hms.utils.cloudproviderutils.afcl;

import at.uibk.dps.afcl.Function;
import at.uibk.dps.afcl.SubFC;
import at.uibk.dps.afcl.Workflow;
import at.uibk.dps.afcl.functions.*;
import at.uibk.dps.afcl.functions.objects.Case;
import at.uibk.dps.afcl.functions.objects.PropertyConstraint;
import at.uibk.dps.afcl.functions.objects.Section;
import at.uibk.dps.afcl.utils.Utils;
import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.model.api.*;
import com.asi.hms.utils.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AfclParser {

    private AfclParser() {
        throw new IllegalStateException("Utility class");
    }

    public static APIWorkflow getWorkflow(String filePath) {

        try {

            File file = new File(filePath);

            Workflow workflow = Utils.readYAMLNoValidation(
                    FileUtil.readFileToByteArray(file)
            );

            return getAPIWorkflow(workflow);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static APIWorkflow getWorkflow(MultipartFile file) {

        try {

            Workflow workflow = Utils.readYAMLNoValidation(file.getBytes());

            return getAPIWorkflow(workflow);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static void createWorkflowAtPath(File sourceFile,
                                            APIWorkflowDeployment workflowDeployment,
                                            String destinationFile) {

        try {

            Workflow workflow = Utils.readYAMLNoValidation(
                    FileUtil.readFileToByteArray(sourceFile)
            );

            List<AtomicFunction> functionsFromWorkflow = getFunctionsFromWorkflow(workflow);

            for(AtomicFunction af : functionsFromWorkflow) {

                APIFunctionFlat apiFunctionFlat = workflowDeployment.getFunctionDefinitions()
                        .stream()
                        .filter(fd -> fd.getFunction().getName().equals(af.getName()))
                        .findFirst()
                        .orElseThrow(() -> new HolisticFaaSException("Function not found"));

                if(af.getProperties() == null) {
                    af.setProperties(new ArrayList<>());
                }

                af.getProperties().add(
                        new PropertyConstraint("resource", apiFunctionFlat.getFunctionDeployment().getResource())
                );

            }

            // Afcl writes file to root directory, which is a bit annoying
            Utils.writeYamlNoValidation(workflow, destinationFile);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private static APIWorkflow getAPIWorkflow(Workflow workflow) {

        APIWorkflow apiWorkflow = new APIWorkflow();

        apiWorkflow.setName(workflow.getName());

        apiWorkflow.setFunctions(getAPIFunctions(workflow));

        return apiWorkflow;

    }

    private static List<APIFunction> getAPIFunctions(Workflow workflow) {

        List<APIFunction> apiFunctions = new ArrayList<>();

        for (AtomicFunction af : getFunctionsFromWorkflow(workflow)) {

            APIFunction apiFunction = new APIFunction();
            apiFunction.setName(af.getName());

            APIFunctionType apiFunctionType = new APIFunctionType();

            apiFunctionType.setName(af.getType());

            apiFunction.setFunctionType(apiFunctionType);

            apiFunctions.add(apiFunction);

        }

        return apiFunctions;

    }


    private static List<AtomicFunction> getFunctionsFromWorkflow(Workflow workflow) {

        List<Function> functions = new ArrayList<>();

        functions.addAll(getAllFunctions(workflow.getWorkflowBody()));

        if (workflow.getSubFCs() != null) {
            for (SubFC subFC : workflow.getSubFCs()) {
                functions.addAll(getAllFunctions(subFC.getSubFCBody()));
            }
        }

        List<AtomicFunction> atomicFunctions = new ArrayList<>();
        for (Function f : functions) {
            if (f instanceof AtomicFunction af &&
                    atomicFunctions.stream().noneMatch(atomicFunction -> atomicFunction.getName().equals(af.getName()))) {

                atomicFunctions.add(af);

            }
        }


        return atomicFunctions;

    }

    private static List<Function> getAllFunctions(List<Function> f) {

        List<Function> functions = new ArrayList<>();

        for (Function function : f) {
            functions.addAll(getAllFunctions(function));
        }

        return functions;

    }

    private static List<Function> getAllFunctions(Function f) {

        List<Function> functions = new ArrayList<>();

        if (f instanceof AtomicFunction af) {
            functions.add(af);
        } else if (f instanceof IfThenElse ifThenElse) {
            functions.addAll(getAllFunctions(ifThenElse.getElseBranch()));
            functions.addAll(getAllFunctions(ifThenElse.getThenBranch()));
        } else if (f instanceof LoopCompound loopCompound) {
            functions.addAll(getAllFunctions(loopCompound.getLoopBody()));
        } else if (f instanceof Parallel parallel) {

            for (Section section : parallel.getParallelBody()) {
                functions.addAll(getAllFunctions(section.getSection()));
            }

        } else if (f instanceof ParallelFor parallelFor) {
            // Nothing to do
        } else if (f instanceof Sequence sequence) {
            functions.addAll(getAllFunctions(sequence.getSequenceBody()));
        } else if (f instanceof SequentialFor sequentialFor) {
            // Nothing to do
        } else if (f instanceof SequentialWhile sequentialWhile) {
            // Nothing to do
        } else if (f instanceof Switch aSwitch) {

            functions.addAll(getAllFunctions(aSwitch.getDefaultBranch()));

            for (Case aCase : aSwitch.getCases()) {
                functions.addAll(getAllFunctions(aCase.getFunctions()));
            }

        }

        return functions;

    }

}
