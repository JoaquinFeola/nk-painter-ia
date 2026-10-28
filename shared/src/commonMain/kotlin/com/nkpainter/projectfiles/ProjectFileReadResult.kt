package com.nkpainter.projectfiles

sealed interface ProjectFileReadResult {
    data class Read(val content: ProjectFileContent) : ProjectFileReadResult

    /** Written by a format this app does not know yet: the app has to be updated. */
    data class NewerFormat(val formatVersion: Int) : ProjectFileReadResult

    /** Damaged, or not a project file of this app. */
    data class Invalid(val reason: String) : ProjectFileReadResult
}
