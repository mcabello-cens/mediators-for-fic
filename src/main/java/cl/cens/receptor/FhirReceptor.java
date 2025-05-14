package cl.cens.receptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FhirReceptor {

    private static final Logger logger = LoggerFactory.getLogger(FhirReceptor.class);
    private static final FhirContext fhirContext = FhirContext.forR4();

    public static boolean validateOperationOutcome(OperationOutcome outcome) {
        if (outcome == null || !outcome.hasIssue()) {
            logger.info("No OperationOutcome issues found.");
            return true;
        }

        for (OperationOutcome.OperationOutcomeIssueComponent issue : outcome.getIssue()) {
            logger.warn("OperationOutcome issue found: {}", issue.getDiagnostics());
            if (issue.getSeverity() == OperationOutcome.IssueSeverity.ERROR) {
                return false;
            }
        }

        return true;
    }

    public static String helloWorld() {
        logger.info("Hola Mundo desde FhirMessageEvaluator.");
        return "Hola Mundo desde FhirMessageEvaluator class";
    }

    public static OperationOutcome sendBundle(String fhirServerUrl, String bearerToken, Bundle bundle) throws Exception {
        String url = fhirServerUrl.endsWith("/") ? fhirServerUrl + "$process-message" : fhirServerUrl + "/$process-message";
        logger.info("Sending bundle to FHIR server: {}", url);

        IParser parser = fhirContext.newJsonParser();
        String jsonPayload = parser.encodeResourceToString(bundle);

        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/fhir+json");
        post.setHeader("Authorization", "Bearer " + bearerToken);
        post.setEntity(new StringEntity(jsonPayload, "UTF-8"));

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post)) {

            String responseBody = EntityUtils.toString(response.getEntity());
            logger.info("FHIR server responded with: {}", responseBody);

            Resource resource = (Resource) parser.parseResource(responseBody);
            if (resource instanceof OperationOutcome) {
                logger.info("Parsed OperationOutcome successfully.");
                return (OperationOutcome) resource;
            } else {
                logger.error("Unexpected response type: {}", resource.getClass().getSimpleName());
                throw new IllegalStateException("Unexpected FHIR response type: " + resource.getClass().getSimpleName());
            }

        } catch (Exception e) {
            logger.error("Failed to send FHIR bundle: {}", e.getMessage(), e);
            throw e;
        }
    }
}
