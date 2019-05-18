package com.miguelmartin.organizame.util

import android.arch.persistence.room.TypeConverter
import java.util.*

class ConversoresFechas {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}