package nl.vslcatena.vslcatena.models

enum class Role(val value: String, val level: Long) {
    ANONYMOUS("none", 0),
    USER("user", 1),
    MODERATOR("moderator", 2),
    ADMIN("admin", 3),
    SUPER_ADMIN("super_admin", 4);

    fun hasClearance(role: Role): Boolean {
        return this.level >= role.level
    }

    companion object {
        fun getRoleFromLevel(level: Long): Role {
            return Role.values().find { it.level == level } ?: ANONYMOUS
        }
    }
}