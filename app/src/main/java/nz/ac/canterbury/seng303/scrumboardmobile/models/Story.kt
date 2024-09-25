package nz.ac.canterbury.seng303.scrumboardmobile.models

data class Task(
    val title: String,
    val description: String,
    val assignedTo: String,
    val workLogList: List<WorkLog>
)
data class WorkLog(
    val description: String,
    val time: Long,
    val workingHours: Int,
    val createdBy: Int
)

data class Story(
    val id: Int,
    val title: String,
    val description: String,
    val timestamp: Long,
    val taskList: List<Task>
): Identifiable {
    override fun getIdentifier(): Int {
        return id
    }
}
