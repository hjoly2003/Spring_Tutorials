package com.marcobehler.stripepayments.web.form;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * [N]:mvc-validation - Bean validation. 
 */
public record FeatureReqForm (
    
    @NonNull
    @Min(4)
    Integer amount,

    @NonNull
    @Size(min = 5, max=200)
    String featureRequest,

    @Email
    String email
) {}
