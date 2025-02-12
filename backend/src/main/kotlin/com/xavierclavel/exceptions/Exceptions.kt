package com.xavierclavel.exceptions

class UnauthorizedException(cause: UnauthorizedCause): Exception(cause.key)

class ForbiddenException(cause: ForbiddenCause): Exception(cause.key)

class BadRequestException(cause: BadRequestCause): Exception(cause.key)

class NotFoundException(cause: NotFoundCause): Exception(cause.key)

enum class UnauthorizedCause(val key: String) {
    SESSION_NOT_FOUND("session.not.found"),
    USER_NOT_VERIFIED("user.not.verified"),
    INVALID_PASSWORD("invalid.password"),
}

enum class ForbiddenCause(val key: String) {

}

enum class NotFoundCause(val key: String) {
    USER_NOT_FOUND("user.not.found"),
    RECIPE_NOT_FOUND("recipe.not.found"),
    COOKBOOK_NOT_FOUND("cookbook.not.found"),
    INGREDIENT_NOT_FOUND("ingredient.not.found"),
}

enum class BadRequestCause (val key: String) {
    TOKEN_MISSING("token.missing"),
    MAIL_MISSING("mail.missing"),
    USER_ALREADY_FOLLOWED("user.already.followed"),
    USER_NOT_FOLLOWED("user.not.followed"),
    NOT_APPLICABLE_ON_SELF("not.applicable.on.self"),
    MAIL_ALREADY_USED("mail.already.used"),
    USERNAME_ALREADY_USED("username.already.used"),
}