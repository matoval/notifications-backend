package com.redhat.cloud.notifications.models.behaviorgroup;

import com.redhat.cloud.notifications.routers.models.behaviorgroup.CreateBehaviorGroupRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.UUID;

public class CreateBehaviorGroupRequestTest {

    private static Validator validator;

    /**
     * Sets up the validator.
     */
    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    /**
     * Tests that a valid request doesn't raise any constraint violations.
     */
    @Test
    void testValidRequest() {
        final var requestBundleId = new CreateBehaviorGroupRequest();
        requestBundleId.bundleId = UUID.randomUUID();
        requestBundleId.displayName = "valid display name";

        final var requestBundleName = new CreateBehaviorGroupRequest();
        requestBundleName.bundleName = "specified bundle name";
        requestBundleName.displayName = "valid display name";

        final CreateBehaviorGroupRequest[] validRequests = {requestBundleId, requestBundleName};

        for (final var request : validRequests) {
            final var constraintViolations = validator.validate(request);

            Assertions.assertEquals(0, constraintViolations.size(), "unexpected constraint violations received: " + constraintViolations);
        }
    }

    /**
     * Tests that a request with an invalid display name raises a constraint
     * violation.
     */
    @Test
    void testInvalidDisplayName() {
        final String[] invalidDisplayNames = {null, "", "     "};

        for (final var invalidName : invalidDisplayNames) {
            final var request = new CreateBehaviorGroupRequest();
            request.bundleId = UUID.randomUUID();
            request.displayName = invalidName;

            final var constraintViolations = validator.validate(request);

            Assertions.assertEquals(1, constraintViolations.size(), "unexpected number of constraint violations received. CVs: " + constraintViolations);

            for (final var cv : constraintViolations) {
                Assertions.assertEquals("displayName", cv.getPropertyPath().toString(), "unexpected field raised the constraint violation");
                Assertions.assertEquals("must not be blank", cv.getMessage(), "unexpected constraint violation returned");
            }
        }
    }

    /**
     * Tests that when the bundle ID or the name is missing, a constraint
     * violation is raised.
     */
    @Test
    void testMissingBundleIdAndNames() {
        final var request = new CreateBehaviorGroupRequest();
        request.displayName = "test display name";

        final var constraintViolations = validator.validate(request);

        Assertions.assertEquals(1, constraintViolations.size(), "unexpected number of constraint violations received. CVs: " + constraintViolations);

        for (final var cv : constraintViolations) {
            Assertions.assertEquals("bundleUuidOrBundleNameValid", cv.getPropertyPath().toString(), "unexpected field raised the constraint violation");
            Assertions.assertEquals("either the bundle name or the bundle UUID are required", cv.getMessage(), "unexpected constraint violation returned");
        }
    }

    /**
     * Tests that when the specified bundle names are invalid, a constraint
     * violation is raised.
     */
    @Test
    void testInvalidBundleNames() {
        final String[] invalidBundleNames = {null, "", "     "};

        for (final var invalidName : invalidBundleNames) {
            final var request = new CreateBehaviorGroupRequest();
            request.bundleName = invalidName;
            request.displayName = "test display name";

            final var constraintViolations = validator.validate(request);

            Assertions.assertEquals(1, constraintViolations.size(), "unexpected number of constraint violations received. CVs: " + constraintViolations);

            for (final var cv : constraintViolations) {
                Assertions.assertEquals("bundleUuidOrBundleNameValid", cv.getPropertyPath().toString(), "unexpected field raised the constraint violation");
                Assertions.assertEquals("either the bundle name or the bundle UUID are required", cv.getMessage(), "unexpected constraint violation returned");
            }
        }
    }
}
