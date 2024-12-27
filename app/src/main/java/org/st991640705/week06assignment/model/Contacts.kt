package org.st991640705.week06assignment.model

/**
 * This file defines the data model for contacts, including properties such as name, phone number,
 * image URI and workSchedule, allowing for structured data representation
 */
data class Contacts(
    val id: String? = null,
    val name: String? = null,
    val phoneNumber: String? = null,
    val workSchedule: String? = null,
    val imgUri: String? = null
)
