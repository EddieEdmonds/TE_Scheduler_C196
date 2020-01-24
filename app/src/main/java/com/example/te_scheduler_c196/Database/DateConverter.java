package com.example.te_scheduler_c196.Database;

import java.util.Date;

import androidx.room.TypeConverter;

// example converter for java.util.Date
public class DateConverter {
    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }
}