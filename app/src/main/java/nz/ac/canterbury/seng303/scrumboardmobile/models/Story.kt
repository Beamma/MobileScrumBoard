package nz.ac.canterbury.seng303.scrumboardmobile.models
enum class Complexity {
    UNSET,
    LOW,
    MEDIUM,
    HIGH
}

enum class Status {
    TO_DO,
    IN_PROGRESS,
    UNDER_REVIEW,
    DONE
}

enum class Priority {
    LOW,
    NORMAL,
    HIGH,
    CRITICAL
}

data class Task(
    val title: String,
    val description: String,
    val assignedTo: String,
    val workLogList: List<WorkLog>,
    val complexity: Complexity,
    val status: Status,
    val estimate: Int,
    val priority: Priority,
    val reviewer: String
)
data class WorkLog(
    val description: String,
    val time: Long,
    val workingHours: Int,
    val createdBy: String
)

data class Story(
    val id: Int,
    val title: String,
    val description: String,
    val timestamp: Long,
    val taskList: List<Task>,
    val status: Status
): Identifiable {
    override fun getIdentifier(): Int {
        return id
    }
}
