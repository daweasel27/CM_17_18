package com.example.jp.projectofinal;

import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by TiagoHenriques on 17/10/2017.
 */

public class MovieContract {

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the contents of the weather table */
    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_DIRECTOR = "director";

        public static final String COLUMN_STARS = "stars";

        public static final String COLUMN_YEAR = "year";

        public static final String COLUMN_LENGTH = "length";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_GENRE = "genre";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_STORY_LINE = "storyline";

        public static final String COLUMN_THUMB = "thumb";

        public static final String COLUMN_POSTER = "poster";
    }
}