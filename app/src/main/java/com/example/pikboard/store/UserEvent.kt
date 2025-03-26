package com.example.pikboard.store

sealed interface UserEvent {
    object SaveContact: UserEvent
    data class SetUsername(val firstName: String): UserEvent

    object ShowDialog: UserEvent
    object HideDialog: UserEvent
}
