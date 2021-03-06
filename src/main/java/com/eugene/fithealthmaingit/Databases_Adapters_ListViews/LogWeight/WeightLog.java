/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight;

import android.content.Context;

import com.eugene.fithealthmaingit.AppActivity;
import com.orm.androrm.DateField;
import com.orm.androrm.DoubleField;
import com.orm.androrm.Model;
import com.orm.androrm.QuerySet;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * This class is the database for storing the users entry information. The user weight logs
 * is a database object from the androrm package
 */
public class WeightLog extends Model {
    protected DoubleField currentWeight;
    protected DoubleField weightLoss;
    protected DateField date;

    public WeightLog() {
        super(true);
        currentWeight = new DoubleField();
        date = new DateField();
    }

    public double getCurrentWeight() {
        return currentWeight.get();
    }

    public void setCurrentWeight(double count) {
        currentWeight.set(count);
    }

    public double getWeightLoss() {
        return weightLoss.get();
    }

    public void setWeightLoss(double count) {
        weightLoss.set(count);
    }

    public void setDate(Date d) {
        date.set(d);
    }

    public Date getDate() {
        return date.get();
    }

    public boolean save() {
        int min = 65;
        int max = 2000000;
        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;
        return this.save(context(), i1);
    }

    public boolean edit() {
        return this.save(context());
    }

    public boolean delete() {
        return this.delete(context());
    }

    public static List<WeightLog> all() {
        return WeightLog.objects().all().orderBy("date").toList();
    }

    private static String formatDateForQuery(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String dayString = String.valueOf(day);
        String monthString = String.valueOf(month);
        if (day < 10) {
            dayString = "0" + dayString;
        }
        if (month < 10) {
            monthString = "0" + monthString;
        }
        return year + "-" + monthString + "-" + dayString;
    }

    public static QuerySet<WeightLog> objects() {
        return WeightLog.objects(context(), WeightLog.class);
    }

    private static Context context() {
        return AppActivity.context();
    }
}
