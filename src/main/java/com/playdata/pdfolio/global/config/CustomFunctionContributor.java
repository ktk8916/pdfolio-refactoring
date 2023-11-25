package com.playdata.pdfolio.global.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.springframework.context.annotation.Configuration;

import static org.hibernate.type.StandardBasicTypes.DOUBLE;

@Configuration
public class CustomFunctionContributor implements FunctionContributor {

    private static final String MATCH_AGAINST = "match_against";
    private static final String MATCH_AGAINST_PATTERN = "match (?1, ?2) against (?3 in natural language mode)";

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry()
                .registerPattern(
                        MATCH_AGAINST,
                        MATCH_AGAINST_PATTERN,
                        functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(DOUBLE)
                );
    }
}
