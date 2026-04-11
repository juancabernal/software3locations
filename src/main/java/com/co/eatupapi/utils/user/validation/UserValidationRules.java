package com.co.eatupapi.utils.user.validation;

public final class UserValidationRules {

    public static final int NAME_MIN_LENGTH = 2;
    public static final int NAME_MAX_LENGTH = 100;
    public static final int DOCUMENT_NUMBER_MIN_LENGTH = 5;
    public static final int DOCUMENT_NUMBER_MAX_LENGTH = 30;
    public static final int PHONE_LENGTH = 10;
    public static final int EMAIL_MAX_LENGTH = 150;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 72;
    public static final int ADDRESS_MIN_LENGTH = 5;
    public static final int ADDRESS_MAX_LENGTH = 255;
    public static final int PAGE_MIN = 0;
    public static final int SIZE_MIN = 1;
    public static final int SIZE_MAX = 100;

    public static final String NAME_REGEX = "^\\s*[\\p{L}\\p{M}]+(?:[ '\\-][\\p{L}\\p{M}]+)*\\s*$";
    public static final String DOCUMENT_NUMBER_REGEX = "^\\s*[A-Za-z0-9][A-Za-z0-9\\-]{4,29}\\s*$";
    public static final String PHONE_REGEX = "^\\d{10}$";
    public static final String EMAIL_REGEX = "^\\s*[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\s*$";
    public static final String PASSWORD_REGEX = "^(?=\\S{8,72}$)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d\\s]).*$";
    public static final String ADDRESS_REGEX =
            "^\\s*[\\p{L}\\p{M}0-9#.,\\-°/]+(?:\\s+[\\p{L}\\p{M}0-9#.,\\-°/]+)*\\s*$";
    public static final String STATUS_REGEX = "(?i)^\\s*(ACTIVE|INACTIVE)\\s*$";

    private UserValidationRules() {
    }
}
