package com.azure.firechatapp.models

import java.util.Date

data class Message(val authorId: String, val message: String, val profileImageURL: String, val sendAt: Date)
