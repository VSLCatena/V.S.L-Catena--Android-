package nl.vslcatena.vslcatena.models

enum class Role(val value: String, val level: Int) {
    ANONYMOUS("none", 0),
    USER("user", 1),
    MODERATOR("moderator", 2),
    ADMIN("admin", 3),
    SUPER_ADMIN("super_admin", 5);

    fun hasClearance(role: Role): Boolean {
        return this.level >= role.level
    }

    companion object {
        fun getRoleFromString(tag: String): Role {
            return Role.values().find { it.value == tag } ?: ANONYMOUS
        }
    }
}