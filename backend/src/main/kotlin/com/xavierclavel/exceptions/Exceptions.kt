package com.xavierclavel.exceptions

class UnauthorizedException(cause: UnauthorizedCause): Exception(cause.key)

class ForbiddenException(cause: ForbiddenCause): Exception(cause.key)

class BadRequestException(cause: BadRequestCause): Exception(cause.key)

class NotFoundException(cause: NotFoundCause): Exception(cause.key)

enum class UnauthorizedCause(val key: String) {
    SESSION_NOT_FOUND("session.not.found"),
    USER_NOT_VERIFIED("user.not.verified"),
    INVALID_PASSWORD("invalid.password"),
    INVALID_TOKEN("invalid.token"),
}

enum class ForbiddenCause(val key: String) {
    NOT_ALLOWED_TO_EDIT_RECIPE("not.allowed.to.edit.recipe"),
    NOT_ALLOWED_TO_EDIT_USER("not.allowed.to.edit.user"),
    NOT_ALLOWED_TO_SEE_RECIPE("not.allowed.to.see.recipe"),
    NOT_ALLOWED_TO_SEE_COOKBOOK("not.allowed.to.see.cookbook"),
    NOT_ALLOWED_TO_SEE_USER("not.allowed.to.see.user"),
    NOT_MEMBER_OF_COOKBOOK("not.member.of.cookbook"),
    ACCOUNT_NOT_PUBLIC("account.not.public"),
    MUST_BE_COOKBOOK_ADMINISTRATOR("must.be.cookbook.administrator"),
}

enum class NotFoundCause(val key: String) {
    USER_NOT_FOUND("user.not.found"),
    RECIPE_NOT_FOUND("recipe.not.found"),
    COOKBOOK_NOT_FOUND("cookbook.not.found"),
    INGREDIENT_NOT_FOUND("ingredient.not.found"),
    MAIL_NOT_FOUND("mail.not.found"),
    FOLLOW_NOT_FOUND("follow.not.found"),
}

enum class BadRequestCause (val key: String) {
    INVALID_REQUEST("invalid.request"),
    INVALID_IMAGE("invalid.image"),

    TOKEN_MISSING("token.missing"),
    MAIL_MISSING("mail.missing"),

    USER_ALREADY_FOLLOWED("user.already.followed"),
    USER_NOT_FOLLOWED("user.not.followed"),
    NO_FOLLOW_REQUEST("no.follow.request"),

    NOT_APPLICABLE_ON_SELF("not.applicable.on.self"),
    MAIL_ALREADY_USED("mail.already.used"),
    USERNAME_ALREADY_USED("username.already.used"),
    RECIPE_ALREADY_IN_COOKBOOK("recipe.already.in.cookbood"),
    RECIPE_NOT_IN_COOKBOOK("recipe.not.in.cookbook"),

}