package com.playdata.pdfolio.project.domain.entity;

import com.playdata.pdfolio.project.exception.InValidUrlTypeException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor
public class Url {

    private static final String URI_PATTERN = "[A-Za-z0-9+&@#/%?=~_|!:,.;-]*[-A-Za-z0-9+&@#/%=~_|]";

    @Column
    @Lob
    private String url;

    private Url(String url) {
        this.url = url;
    }

    public static Url of(final String url) {
        if (isEmptyOrNull(url)) {
            return null;
        }
        validateUrlPattern(url);
        return new Url(url);
    }

    // URL 형식 검증 로직
    private static void validateUrlPattern(final String url) {
        Pattern pattern = Pattern.compile(URI_PATTERN);
        if (!pattern.matcher(url).matches()) {
            throw new InValidUrlTypeException();
        }
    }

    private static boolean isEmptyOrNull(String url) {
        return url == null || url.trim().isEmpty();
    }
}
