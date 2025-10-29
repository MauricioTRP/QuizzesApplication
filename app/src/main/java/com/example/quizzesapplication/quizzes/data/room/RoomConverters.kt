package com.example.quizzesapplication.quizzes.data.room

import androidx.room.TypeConverter
import java.util.Date

/**
 * Type converters to allow Room to reference complex data types.
 */
class RoomConverters {
    /**
     * Converts Long timestamp to a Date object
     *
     * @param value the long timestamp from database
     * @return Date object, null if timestamp is null
     */
    @TypeConverter
    fun fromTimeStamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Converts a Date object to a long timestamp
     *
     * @param date the date to convert
     * @return long timestamp, null if date is null
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}