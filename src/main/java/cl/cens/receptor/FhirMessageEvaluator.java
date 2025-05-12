package cl.cens.receptor;

import org.hl7.fhir.r4.model.OperationOutcome;

public class FhirMessageEvaluator {
    public static boolean validateOperationOutcome(OperationOutcome outcome) {
        if (outcome == null || !outcome.hasIssue()) {
            return true; // No hay problemas registrados
        }

        for (OperationOutcome.OperationOutcomeIssueComponent issue : outcome.getIssue()) {
            if (issue.getSeverity() == OperationOutcome.IssueSeverity.ERROR) {
                return false; // Se detectó un error
            }
        }

        return true; // No se encontraron errores
    }

    public static String helloWorld() {
        System.out.println("¡Hola Mundo!");
        return "Hola Mundo desde FhirMessageEvaluator class";
    }
}
