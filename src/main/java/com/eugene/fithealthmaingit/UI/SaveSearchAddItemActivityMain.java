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

package com.eugene.fithealthmaingit.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.CalSQLiteDatabase.DailyCalorieAdapter;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.CalSQLiteDatabase.DailyCalorieIntake;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.CalSQLiteDatabase.DatabaseHandler;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterAll;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterPrevention;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.FatSecretSearchAndGet.FatSecretGetMethod;
import com.eugene.fithealthmaingit.MainActivity;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.DateCompare;
import com.eugene.fithealthmaingit.Utilities.Globals;
import com.eugene.fithealthmaingit.Utilities.OrderFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveSearchAddItemActivityMain extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String mealType;
    String food_id;
    String food_brand;
    String meal_favorite;
    CollapsingToolbarLayout collapsingToolbar;
    DecimalFormat df = new DecimalFormat("0.0");
    boolean favorite = false;
    DecimalFormat dfW = new DecimalFormat("0");
    // Widgets
    private Toolbar mToolbar;
    private TextView mViewSevingSize;
    private TextView mServingg;
    private TextView mAmountPerServing;
    private TextView mServingSizeUpdated;
    private TextView mFoodCal;
    private TextView mCalUpdate;
    private TextView mViewFat1;
    private TextView mFattieUpdate;
    private TextView mViewSaturatedFat;
    private TextView mSaturatedFatUpdate;
    private TextView mViewCholesterol;
    private TextView mCholesterolUpdate;
    private TextView mViewSodium;
    private TextView mSodiumUpdate;
    private TextView mViewCarbs1;
    private TextView mCarbUpdate;
    private TextView mViewFiber;
    private TextView mFiberUpdate;
    private TextView mViewSugar;
    private TextView mSugarUpdate;
    private TextView mViewProtien1;
    private TextView mProUpdate;
    private TextView mViewVitA;
    private TextView mVitAUpdate;
    private TextView mViewVitC;
    private TextView mVitCUpdate;
    private TextView mViewCalcium;
    private TextView mCalciumUpdate;
    private TextView mViewIron;
    private TextView mIronUpdate;
    private ProgressBar mPbCal;
    private ProgressBar mPbFat;
    private ProgressBar mPbCarb;
    private ProgressBar mPbPro;

    private Spinner mSpnServings;
    private LinearLayout llVitA,
            llVitC,
            llCalcium,
            llIron,
            llCalories,
            llFat,
            llSat,
            llCholesterol,
            llSodium,
            llCarbs,
            llFiber,
            llSugars,
            llProtein;
    private View vVitA, vVitC, vCalcium, vIron, vFat,
            vSat,
            vCholesterol,
            vSodium,
            vCarbs,
            vFiber,
            vSugar,
            vPro;
    private LogAdapterPrevention mLogAdapterAll;
    // FatSecret method.get
    private FatSecretGetMethod mFatSecretGet;
    String arrayOrJson;
    // Inflate view
    private ArrayList<String> mItem;
    ArrayAdapter<String> servingAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_search_item_fragment_new);
        setAdapter(new Date());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mealType = extras.getString(Globals.MEAL_TYPE);
            food_id = extras.getString(Globals.MEAL_ID);
            food_brand = extras.getString(Globals.MEAL_BRAND);
            meal_favorite = extras.getString(Globals.MEAL_FAVORITE);
        }
        Log.e("FAV", meal_favorite);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.white));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (meal_favorite.equals("favorite")) {
            toolbar.inflateMenu(R.menu.menu_save_meal_fav);
            favorite = true;
        } else {
            toolbar.inflateMenu(R.menu.menu_save_meal);
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_save:
                        saveMeal();
                        break;
                    case R.id.action_fav:
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.menu_save_meal_fav);
                        favorite = true;
                        break;
                    case R.id.action_fav_save:
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.menu_save_meal);
                        favorite = false;
                        break;
                    default:
                        break;
                }


                return false;
            }
        });

        findViewsById();
        getFood(Integer.valueOf(food_id));
    }

    //Declare a int member variable and initialize to 0 (at the top of your class)

    private int mLastSpinnerPosition = 0;

    private void findViewsById() {
        mLogAdapterAll = new LogAdapterPrevention(SaveSearchAddItemActivityMain.this, 0, LogMeal.logsByDate(new Date()));
        mFatSecretGet = new FatSecretGetMethod(); // method.get
        mViewSevingSize = (TextView) findViewById(R.id.viewSevingSize);
        mViewSevingSize.setVisibility(View.GONE);
        mServingg = (TextView) findViewById(R.id.servingg);
        mServingg.setText("1");
        mAmountPerServing = (TextView) findViewById(R.id.amountPerServing);
        mServingSizeUpdated = (TextView) findViewById(R.id.servingSizeUpdated);
        mFoodCal = (TextView) findViewById(R.id.foodCal);
        mCalUpdate = (TextView) findViewById(R.id.calUpdate);
        mViewFat1 = (TextView) findViewById(R.id.viewFat1);
        mFattieUpdate = (TextView) findViewById(R.id.fattieUpdate);
        mViewSaturatedFat = (TextView) findViewById(R.id.viewSaturatedFat);
        mSaturatedFatUpdate = (TextView) findViewById(R.id.saturatedFatUpdate);
        mViewCholesterol = (TextView) findViewById(R.id.viewCholesterol);
        mCholesterolUpdate = (TextView) findViewById(R.id.cholesterolUpdate);
        mViewSodium = (TextView) findViewById(R.id.viewSodium);
        mSodiumUpdate = (TextView) findViewById(R.id.sodiumUpdate);
        mViewCarbs1 = (TextView) findViewById(R.id.viewCarbs1);
        mCarbUpdate = (TextView) findViewById(R.id.carbUpdate);
        mViewFiber = (TextView) findViewById(R.id.viewFiber);
        mFiberUpdate = (TextView) findViewById(R.id.fiberUpdate);
        mViewSugar = (TextView) findViewById(R.id.viewSugar);
        mSugarUpdate = (TextView) findViewById(R.id.sugarUpdate);
        mViewProtien1 = (TextView) findViewById(R.id.viewProtien1);
        mProUpdate = (TextView) findViewById(R.id.proUpdate);
        mViewVitA = (TextView) findViewById(R.id.viewVitA);
        mVitAUpdate = (TextView) findViewById(R.id.vitAUpdate);
        mViewVitC = (TextView) findViewById(R.id.viewVitC);
        mVitCUpdate = (TextView) findViewById(R.id.vitCUpdate);
        mViewCalcium = (TextView) findViewById(R.id.viewCalcium);
        mCalciumUpdate = (TextView) findViewById(R.id.calciumUpdate);
        mViewIron = (TextView) findViewById(R.id.viewIron);
        mIronUpdate = (TextView) findViewById(R.id.ironUpdate);
        mPbCal = (ProgressBar) findViewById(R.id.pbCal);
        mPbFat = (ProgressBar) findViewById(R.id.pbFat);
        mPbCarb = (ProgressBar) findViewById(R.id.pbCarb);
        mPbPro = (ProgressBar) findViewById(R.id.pbPro);
        mItem = new ArrayList<>();
        servingAdapter = new ArrayAdapter<>(SaveSearchAddItemActivityMain.this, android.R.layout.simple_list_item_1, mItem);
        mSpnServings = (Spinner) findViewById(R.id.spnServings);
        mSpnServings.setVisibility(View.GONE);
        mSpnServings.setAdapter(servingAdapter);
        mSpnServings.setSelection(0, false);
        mSpnServings.post(new Runnable() {
            public void run() {
                mSpnServings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                        if (mLastSpinnerPosition == i) { // Prevent spinner from being called on start and reselection of the same item
                            return;
                        }
                        mLastSpinnerPosition = i;
                        getFood(Integer.valueOf(food_id));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
        llVitA = (LinearLayout) findViewById(R.id.llVitA);
        llVitC = (LinearLayout) findViewById(R.id.llVitC);
        llCalcium = (LinearLayout) findViewById(R.id.llCalcium);
        llIron = (LinearLayout) findViewById(R.id.llIron);
        llCalories = (LinearLayout) findViewById(R.id.llCalories);
        llFat = (LinearLayout) findViewById(R.id.llFat);
        llSat = (LinearLayout) findViewById(R.id.llSat);
        llCholesterol = (LinearLayout) findViewById(R.id.llCholesterol);
        llSodium = (LinearLayout) findViewById(R.id.llSodium);
        llCarbs = (LinearLayout) findViewById(R.id.llCarbs);
        llFiber = (LinearLayout) findViewById(R.id.llFiber);
        llSugars = (LinearLayout) findViewById(R.id.llSugars);
        llProtein = (LinearLayout) findViewById(R.id.llProtein);
        vVitA = findViewById(R.id.vVitA);
        vVitC = findViewById(R.id.vVitC);
        vCalcium = findViewById(R.id.vCalcium);
        vIron = findViewById(R.id.vIron);
        vFat = findViewById(R.id.vFat);
        vSat = findViewById(R.id.vSat);
        vCholesterol = findViewById(R.id.vCholesterol);
        vSodium = findViewById(R.id.vSodium);
        vCarbs = findViewById(R.id.vCarbs);
        vFiber = findViewById(R.id.vFiber);
        vSugar = findViewById(R.id.vSugar);
        vPro = findViewById(R.id.vPro);

        updateItems();
    }

    boolean isCheck = false;

    double mCalorieProgress = 0;
    double mFatProgress = 0;
    double mCarbProgress = 0;
    double mProteinProgress = 0;

    private void progressBars() {
        // Nutrition Goals
        double mCalorieGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_TO_REACH_GOAL, ""));
        double mFatGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_FAT, ""));
        double mCarbGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_CARBOHYDRATES, ""));
        double mProteinGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_PROTEIN, ""));

        double mAllCaloriesConsumed = 0;
        double mAllFatConsumed = 0;
        double mAllCarbsConsumed = 0;
        double mAllProteinConsumed = 0;
        for (LogMeal logMeal : mLogAdapterAll.getLogs()) {
            mAllCaloriesConsumed += logMeal.getCalorieCount();
            mAllFatConsumed += logMeal.getFatCount();
            mAllCarbsConsumed += logMeal.getCarbCount();
            mAllProteinConsumed += logMeal.getProteinCount();
        }
        // mPbCal, mPbFat, mPbCarb, mPbPro
        mPbCal.setMax(Integer.valueOf(dfW.format(mCalorieGoal)));
        mPbCal.setProgress(Integer.valueOf(dfW.format(mAllCaloriesConsumed)) + Integer.valueOf(dfW.format(mCalorieProgress)));
        mPbCal.setSecondaryProgress(Integer.valueOf(dfW.format(mAllCaloriesConsumed)));

        mPbFat.setMax(Integer.valueOf(dfW.format(mFatGoal)));
        mPbFat.setProgress(Integer.valueOf(dfW.format(mAllFatConsumed)) + Integer.valueOf(dfW.format(mFatProgress)));
        mPbFat.setSecondaryProgress(Integer.valueOf(dfW.format(mAllFatConsumed)));

        mPbCarb.setMax(Integer.valueOf(dfW.format(mCarbGoal)));
        mPbCarb.setProgress(Integer.valueOf(dfW.format(mAllCarbsConsumed)) + Integer.valueOf(dfW.format(mCarbProgress)));
        mPbCarb.setSecondaryProgress(Integer.valueOf(dfW.format(mAllCarbsConsumed)));

        mPbPro.setMax(Integer.valueOf(dfW.format(mProteinGoal)));
        mPbPro.setProgress(Integer.valueOf(dfW.format(mAllProteinConsumed)) + Integer.valueOf(dfW.format(mProteinProgress)));
        mPbPro.setSecondaryProgress(Integer.valueOf(dfW.format(mAllProteinConsumed)));
    }

    private void updateItems() {

        mServingSizeUpdated.setText("1");

        LinearLayout changeServing = (LinearLayout) findViewById(R.id.changeServing);
        changeServing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SaveSearchAddItemActivityMain.this);

                alert.setTitle("Update Serving Size: ");
                alert.setMessage("Servings Consumed");

                final EditText input = new EditText(SaveSearchAddItemActivityMain.this);
                input.setText(mServingg.getText().toString());
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                input.selectAll();
                input.setGravity(Gravity.CENTER_HORIZONTAL);
                alert.setView(input, 64, 0, 64, 0);
                alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        double values = Double.valueOf(input.getText().toString());
                        mServingg.setText(df.format(values));
                        mServingSizeUpdated.setText(df.format(values));
                        mCalUpdate.setText(dfW.format(Double.valueOf(mCalories) * values));
                        mCalorieProgress = Double.valueOf(mCalories) * values;
                        mFattieUpdate.setText(df.format(Double.valueOf(mFat) * values));
                        mFatProgress = Double.valueOf(mFat) * values;
                        mSaturatedFatUpdate.setText(df.format(Double.valueOf(mSaturatedFat) * values));
                        mCholesterolUpdate.setText(df.format(Double.valueOf(mCholesterol) * values));
                        mSodiumUpdate.setText(df.format(Double.valueOf(mSodium) * values));
                        mCarbUpdate.setText(df.format(Double.valueOf(mCarbohydrates) * values));
                        mCarbProgress = Double.valueOf(mCarbohydrates) * values;
                        mFiberUpdate.setText(df.format(Double.valueOf(mFiber) * values));
                        mSugarUpdate.setText(df.format(Double.valueOf(mSugar) * values));
                        mProUpdate.setText(df.format(Double.valueOf(mProtein) * values));
                        mProteinProgress = Double.valueOf(mProtein) * values;
                        mVitAUpdate.setText(df.format(Double.valueOf(mVitA) * values));
                        mVitCUpdate.setText(df.format(Double.valueOf(mVitC) * values));
                        mCalciumUpdate.setText(df.format(Double.valueOf(mCalcium) * values));
                        mIronUpdate.setText(df.format(Double.valueOf(mIron) * values));
                        progressBars();
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(input.getWindowToken(), 0);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(input.getWindowToken(), 0);
                    }
                });
                alert.setCancelable(false);
                alert.show();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

    }

    private String mFoodName = null;
    private String mCalories = null;
    private String mCarbohydrates = null;
    private String mProtein = null;
    private String mFat = null;
    private String mSaturatedFat = null;
    private String mCholesterol = null;
    private String mSodium = null;
    private String mFiber = null;
    private String mSugar = null;
    private String mVitA = null;
    private String mVitC = null;
    private String mCalcium = null;
    private String mIron = null;
    private String mServingDescription = null;
    private String mNumberUnits = null;

    private void getFood(final long id) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... arg0) {
                JSONObject foodGet = mFatSecretGet.getFood(id);
                try {
                    if (foodGet != null) {
                        mFoodName = foodGet.getString("food_name");
                        JSONObject servings = foodGet.getJSONObject("servings");
                        Object intervention = servings.get("serving");
                        if (intervention instanceof JSONObject) {
                            arrayOrJson = "object";
                            JSONObject serving = servings.getJSONObject("serving");
                            if (serving.isNull("serving_description")) {
                                mServingDescription = null;
                            } else {
                                mServingDescription = serving.getString("serving_description");
                            }
                            if (serving.isNull("number_of_units")) {
                                mNumberUnits = null;
                            } else {
                                mNumberUnits = serving.getString("number_of_units");
                            }
                            if (serving.isNull("calories")) {
                                mCalories = null;
                            } else {
                                mCalories = serving.getString("calories");
                            }
                            if (serving.isNull("fat")) {
                                mFat = null;
                            } else {
                                mFat = serving.getString("fat");
                            }
                            if (serving.isNull("carbohydrate")) {
                                mCarbohydrates = null;
                            } else {
                                mCarbohydrates = serving.getString("carbohydrate");
                            }
                            if (serving.isNull("protein")) {
                                mProtein = null;
                            } else {
                                mProtein = serving.getString("protein");
                            }
                            if (serving.isNull("saturated_fat")) {
                                mSaturatedFat = null;
                            } else {
                                mSaturatedFat = serving.getString("saturated_fat");
                            }
                            if (serving.isNull("cholesterol")) {
                                mCholesterol = null;
                            } else {
                                mCholesterol = serving.getString("cholesterol");
                            }
                            if (serving.isNull("sodium")) {
                                mSodium = null;
                            } else {
                                mSodium = serving.getString("sodium");
                            }
                            if (serving.isNull("fiber")) {
                                mFiber = null;
                            } else {
                                mFiber = serving.getString("fiber");
                            }
                            if (serving.isNull("sugar")) {
                                mSugar = null;
                            } else {
                                mSugar = serving.getString("sugar");
                            }
                            if (serving.isNull("vitamin_a")) {
                                mVitA = null;
                            } else {
                                mVitA = serving.getString("vitamin_a");
                            }
                            if (serving.isNull("vitamin_c")) {
                                mVitC = null;
                            } else {
                                mVitC = serving.getString("vitamin_c");
                            }
                            if (serving.isNull("calcium")) {
                                mCalcium = null;
                            } else {
                                mCalcium = serving.getString("calcium");
                            }
                            if (serving.isNull("iron")) {
                                mIron = null;
                            } else {
                                mIron = serving.getString("iron");
                            }
                        } else if (intervention instanceof JSONArray) {
                            mItem.clear();
                            JSONArray serving = servings.getJSONArray("serving");
                            for (int i = 0; i < serving.length(); i++) {
                                JSONObject ser = serving.getJSONObject(i);
                                String DifferentServings = ser.getString("serving_description");
                                mItem.add(DifferentServings);
                            }
                            JSONObject newServing = serving.getJSONObject(mLastSpinnerPosition);
                            if (newServing.isNull("serving_description")) {
                                mServingDescription = null;
                            } else {
                                mServingDescription = newServing.getString("serving_description");
                            }
                            if (newServing.isNull("number_of_units")) {
                                mNumberUnits = null;
                            } else {
                                mNumberUnits = newServing.getString("number_of_units");
                            }
                            if (newServing.isNull("calories")) {
                                mCalories = null;
                            } else {
                                mCalories = newServing.getString("calories");
                            }
                            if (newServing.isNull("fat")) {
                                mFat = null;
                            } else {
                                mFat = newServing.getString("fat");
                            }
                            if (newServing.isNull("carbohydrate")) {
                                mCarbohydrates = null;
                            } else {
                                mCarbohydrates = newServing.getString("carbohydrate");
                            }
                            if (newServing.isNull("protein")) {
                                mProtein = null;
                            } else {
                                mProtein = newServing.getString("protein");
                            }
                            if (newServing.isNull("saturated_fat")) {
                                mSaturatedFat = null;
                            } else {
                                mSaturatedFat = newServing.getString("saturated_fat");
                            }
                            if (newServing.isNull("cholesterol")) {
                                mCholesterol = null;
                            } else {
                                mCholesterol = newServing.getString("cholesterol");
                            }
                            if (newServing.isNull("sodium")) {
                                mSodium = null;
                            } else {
                                mSodium = newServing.getString("sodium");
                            }
                            if (newServing.isNull("fiber")) {
                                mFiber = null;
                            } else {
                                mFiber = newServing.getString("fiber");
                            }
                            if (newServing.isNull("sugar")) {
                                mSugar = null;
                            } else {
                                mSugar = newServing.getString("sugar");
                            }
                            if (newServing.isNull("vitamin_a")) {
                                mVitA = null;
                            } else {
                                mVitA = newServing.getString("vitamin_a");
                            }
                            if (newServing.isNull("vitamin_c")) {
                                mVitC = null;
                            } else {
                                mVitC = newServing.getString("vitamin_c");
                            }
                            if (newServing.isNull("calcium")) {
                                mCalcium = null;
                            } else {
                                mCalcium = newServing.getString("calcium");
                            }
                            if (newServing.isNull("iron")) {
                                mIron = null;
                            } else {
                                mIron = newServing.getString("iron");
                            }
                        }
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    return "Error";
                }
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result.equals("Error")) {

                } else {
                    servingAdapter.notifyDataSetChanged();
                    setItems();
                }
            }
        }.execute();
    }

    private void setItems() {
        collapsingToolbar.setTitle(mFoodName);
        mAmountPerServing.setText(mNumberUnits);
        mFoodCal.setText(mCalories);
        mViewFat1.setText(mFat);
        mViewSaturatedFat.setText(mSaturatedFat);
        mViewCholesterol.setText(mCholesterol);
        mViewSodium.setText(mSodium);
        mViewCarbs1.setText(mCarbohydrates);
        mViewFiber.setText(mFiber);
        mViewSugar.setText(mSugar);
        mViewProtien1.setText(mProtein);
        mViewVitA.setText(mVitA);
        mViewVitC.setText(mVitC);
        mViewCalcium.setText(mCalcium);
        mViewIron.setText(mIron);

        // Updated Values
        mCalUpdate.setText(mCalories);
        mCalorieProgress = Double.valueOf(mCalories);
        mFattieUpdate.setText(mFat);
        mFatProgress = Double.valueOf(mFat);
        mSaturatedFatUpdate.setText(mSaturatedFat);
        mCholesterolUpdate.setText(mCholesterol);
        mSodiumUpdate.setText(mSodium);
        mCarbUpdate.setText(mCarbohydrates);
        mCarbProgress = Double.valueOf(mCarbohydrates);
        mFiberUpdate.setText(mFiber);
        mSugarUpdate.setText(mSugar);
        mProUpdate.setText(mProtein);
        mProteinProgress = Double.valueOf(mProtein);
        mVitAUpdate.setText(mVitA);
        mVitCUpdate.setText(mVitC);
        mCalciumUpdate.setText(mCalcium);
        mIronUpdate.setText(mIron);
        mViewSevingSize.setText(mServingDescription);
        progressBars();
        /**
         * Check to see if any variables are null
         * If so set their view gone, and set their value to zero
         */
        if (mCalories == null) {
            llCalories.setVisibility(View.GONE);
            mCalories = "0";
        }
        if (mFat == null) {
            llFat.setVisibility(View.GONE);
            vFat.setVisibility(View.GONE);
            mFat = "0";
        }
        if (mSaturatedFat == null) {
            llSat.setVisibility(View.GONE);
            vSat.setVisibility(View.GONE);
            mSaturatedFat = "0";
        }
        if (mCholesterol == null) {
            mCholesterol = "0";
            llCholesterol.setVisibility(View.GONE);
            vCholesterol.setVisibility(View.GONE);
        }
        if (mSodium == null) {
            llSodium.setVisibility(View.GONE);
            vSodium.setVisibility(View.GONE);
            mSodium = "0";
        }
        if (mCarbohydrates == null) {
            llCarbs.setVisibility(View.GONE);
            vCarbs.setVisibility(View.GONE);
            mCarbohydrates = "0";
        }
        if (mFiber == null) {
            mFiber = "0";
            llFiber.setVisibility(View.GONE);
            vFiber.setVisibility(View.GONE);
        }

        if (mSugar == null) {
            llSugars.setVisibility(View.GONE);
            vSugar.setVisibility(View.GONE);
            mSugar = "0";
        }
        if (mProtein == null) {
            mProtein = "0";
            llProtein.setVisibility(View.GONE);
            vPro.setVisibility(View.GONE);
        }
        if (mVitA == null) {
            llVitA.setVisibility(View.GONE);
            vVitA.setVisibility(View.GONE);
            mVitA = "0";
        }
        if (mVitC == null) {
            llVitC.setVisibility(View.GONE);
            vVitC.setVisibility(View.GONE);
            mVitC = "0";
        }
        if (mCalcium == null) {
            llCalcium.setVisibility(View.GONE);
            vCalcium.setVisibility(View.GONE);
            mCalcium = "0";
        }
        if (mIron == null) {
            mIron = "0";
            llIron.setVisibility(View.GONE);
            vIron.setVisibility(View.GONE);
        }
        if (arrayOrJson != null) {
            mSpnServings.setVisibility(View.GONE);
            mViewSevingSize.setVisibility(View.VISIBLE);
        } else {
            mSpnServings.setVisibility(View.VISIBLE);
            mViewSevingSize.setVisibility(View.GONE);
        }
    }

    private void saveMeal() {

        LogMeal logMeals = new LogMeal();
        logMeals.setMealId(food_id);
        logMeals.setMealChoice(mealType);
        logMeals.setMealName(mFoodName);
        if (llCalories.getVisibility() == View.VISIBLE)
            logMeals.setCalorieCount(Double.valueOf(mCalUpdate.getText().toString()));
        if (llFat.getVisibility() == View.VISIBLE)
            logMeals.setFatCount(Double.valueOf(mFattieUpdate.getText().toString()));
        if (llSat.getVisibility() == View.VISIBLE)
            logMeals.setSaturatedFat(Double.valueOf(mSaturatedFatUpdate.getText().toString()));
        if (llCholesterol.getVisibility() == View.VISIBLE)
            logMeals.setCholesterol(Double.valueOf(mCholesterolUpdate.getText().toString()));
        if (llSodium.getVisibility() == View.VISIBLE)
            logMeals.setSodium(Double.valueOf(mSodiumUpdate.getText().toString()));
        if (llCarbs.getVisibility() == View.VISIBLE)
            logMeals.setCarbCount(Double.valueOf(mCarbUpdate.getText().toString()));
        if (llFiber.getVisibility() == View.VISIBLE)
            logMeals.setFiber(Double.valueOf(mFiberUpdate.getText().toString()));
        if (llSugars.getVisibility() == View.VISIBLE)
            logMeals.setSugars(Double.valueOf(mSugarUpdate.getText().toString()));
        if (llProtein.getVisibility() == View.VISIBLE)
            logMeals.setProteinCount(Double.valueOf(mProUpdate.getText().toString()));
        if (llVitC.getVisibility() == View.VISIBLE)
            logMeals.setVitA(Double.valueOf(mVitAUpdate.getText().toString()));
        if (llVitC.getVisibility() == View.VISIBLE)
            logMeals.setVitC(Double.valueOf(mVitCUpdate.getText().toString()));
        if (llCalcium.getVisibility() == View.VISIBLE)
            logMeals.setCalcium(Double.valueOf(mCalciumUpdate.getText().toString()));
        if (llIron.getVisibility() == View.VISIBLE)
            logMeals.setIron(Double.valueOf(mIronUpdate.getText().toString()));

        if (favorite == true && !meal_favorite.equals("favorite")) {
            logMeals.setFavorite("favorite");
        } else {
            logMeals.setFavorite("not");
        }
        logMeals.setManualEntry("false");
        logMeals.setBrand(food_brand);
        logMeals.setServingSize(Double.valueOf(mServingg.getText().toString()));
        logMeals.setMealServing("Serving");
        logMeals.setDate(new Date());
        logMeals.setOrderFormat(OrderFormat.setMealFormat(mealType));
        logMeals.save();
        testing();
        Intent saveFinish = new Intent(this, MainActivity.class);
        saveFinish.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(saveFinish);
    }


    private DatabaseHandler db;
    List<DailyCalorieIntake> dailyCalorieIntakes;
    private DailyCalorieAdapter dailyCalorieAdapter;

    private void testing() {
        LogAdapterAll logAdapterAll = new LogAdapterAll(this, 0, LogMeal.logsByDate(new Date()));
        if (dailyCalorieIntakes.size() > 0) {
            double caloriesUpdate = 0;
            for (LogMeal logMeal1 : logAdapterAll.getLogs()) {
                caloriesUpdate += logMeal1.getCalorieCount();
            }
            DailyCalorieIntake c = dailyCalorieAdapter.getItem(0);
            c.setCalorieIntake(caloriesUpdate);
            db.updateCalories(c);
        } else {
            LogMeal logMeal = logAdapterAll.getItem(0);
            db.addContact(new DailyCalorieIntake("", logMeal.getCalorieCount(), DateCompare.dateToString(new Date())));
        }
    }


    private void setAdapter(Date newDate) {
        db = new DatabaseHandler(this);
        String date = DateCompare.dateToString(newDate); // Convert date to string
        dailyCalorieIntakes = db.getContactsByDate(date); // filter by string
        dailyCalorieAdapter = new DailyCalorieAdapter(this, 0, dailyCalorieIntakes);
        if (dailyCalorieIntakes.size() > 0) {
            DailyCalorieIntake c = dailyCalorieAdapter.getItem(0);
        }
    }
}
