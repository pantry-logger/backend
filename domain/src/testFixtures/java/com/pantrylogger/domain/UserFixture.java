package com.pantrylogger.domain;

import com.pantrylogger.domain.user.Email;
import com.pantrylogger.domain.user.ExternalAuthId;
import com.pantrylogger.domain.user.User;
import com.pantrylogger.domain.user.Username;

public class UserFixture {

    private static final User USER = new User(
            new ExternalAuthId("test_external_id"),
            new Email("test@test.com"),
            new Username("tester"),
            "Test",
            "Tester"
    );

    private static final User ANOTHER_USER = new User(
            new ExternalAuthId("test_other_external_id"),
            new Email("test@other.com"),
            new Username("other_test"),
            "Other",
            "Tester"
    );

    private static final Email MISSING_EMAIL = new Email("missing@email.com");
    private static final Username MISSING_USERNAME = new Username("missing");

    private static final User MISSING_USER = new User(
            new ExternalAuthId("missing_external_id"),
            MISSING_EMAIL,
            MISSING_USERNAME,
            "Missing",
            "Missed"
    );

    private UserFixture() {
    }

    public static User basicTestUser() {
        return USER;
    }

    public static User anotherBasicTestUser() {
        return ANOTHER_USER;
    }

    public static User missingTestUser() {
        return MISSING_USER;
    }

    public static Email missingEmail() {
        return MISSING_EMAIL;
    }

    public static Username missingUsername() {
        return MISSING_USERNAME;
    }

}
