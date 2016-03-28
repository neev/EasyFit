package com.example.android.easyfitness;


import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.easyfitness.data.EasyFitnessContract;
import com.example.android.easyfitness.data.WorkoutOptions;
import com.example.android.easyfitness.data.WorkoutRecord;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.Date;
import java.util.Vector;

public class FetchRecordsFromFirebase extends AsyncTask<String, Void, Void> {

    public final String Log_tag = FetchRecordsFromFirebase.class.getSimpleName();
    Vector<ContentValues> workoutOptions_values = new Vector <ContentValues>();
    Vector<ContentValues> workoutRecords_values = new Vector <ContentValues>();
    Vector<ContentValues> userProfile_contentValues = new Vector <ContentValues>();

    private final Context mContext;

    public FetchRecordsFromFirebase(Context context) {
        mContext = context;
        // Initialize Firebase with the application context
        Firebase.setAndroidContext(context);

    }

    private boolean DEBUG = true;





    @Override
    protected Void doInBackground(String... params) {
        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        final String authId = params[0];


        if(authId!=null) {

            //To get the USER PROFILE DETAILS FROM Firebase server

           /* final Firebase ref_userProfile = new Firebase(mContext.getResources().getString(R.string
                    .firebase_url)
                    +"/users/"+authId);
            //To read workout options from firebase
            // Attach an listener to read the data at our posts reference
            ref_userProfile.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    for (DataSnapshot userProfileSnapshot : snapshot.getChildren()) {

                        UserDetails userProfileObject = userProfileSnapshot.getValue(UserDetails.class);

                        System.out.println(userProfileObject.toString());


                        ContentValues userProfile_values = new ContentValues();
                        userProfile_values.put(EasyFitnessContract.UserDetailEntry.COLUMN_USER_NAME,
                                userProfileObject.getFullName());
                        userProfile_values.put(EasyFitnessContract.UserDetailEntry.COLUMN_USER_EMAIL,
                                userProfileObject.getEmail());
                        userProfile_values.put(EasyFitnessContract.UserDetailEntry.COLUMN_USER_AGE,
                                userProfileObject.getAge());
                        userProfile_values.put(EasyFitnessContract.UserDetailEntry.COLUMN_USER_WEIGHT,
                                userProfileObject.getWeight());
                        userProfile_values.put(EasyFitnessContract.UserDetailEntry.COLUMN_USER_GOALWEIGHT,
                                userProfileObject.getGoalWeight());
                        userProfile_contentValues.add(userProfile_values);
                        System.out.println("values size : " + workoutOptions_values.size());
                    }


                    System.out.println("*****inserting values*****");
                    ContentValues[] cvArrayUserProfile = new ContentValues[userProfile_contentValues
                            .size()];
                    workoutOptions_values.toArray(cvArrayUserProfile);
                    int inserted_into_DB = mContext.getContentResolver().bulkInsert(
                            EasyFitnessContract.UserDetailEntry.CONTENT_URI, cvArrayUserProfile);
                    Log.v(Log_tag, "WORKOUT OPTIONS Succesfully Inserted : " + String.valueOf
                            (userProfile_contentValues.size()) + "******* " + inserted_into_DB);


                    System.out.println("***** Out of for loop*****");
                }


                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }

            });

*/


            //***************************************
            final Firebase ref = new Firebase(mContext.getResources().getString(R.string
                    .firebase_url)
                    +"/workout");
            //To read workout options from firebase
            // Attach an listener to read the data at our posts reference
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    System.out.println("There are " + snapshot.getChildrenCount() + " workout options");

                    for (DataSnapshot workoutSnapshot : snapshot.getChildren()) {
                        //BlogPost post = postSnapshot.getValue(BlogPost.class);
                        WorkoutOptions workoutObject = workoutSnapshot.getValue(WorkoutOptions.class);
                        System.out.println(workoutObject.getId() + " - " + workoutObject
                                .getWorkout());


                        ContentValues workout_values = new ContentValues();
                        workout_values.put(EasyFitnessContract.WorkOutOptions.COLUMN_WORKOUT_ID, workoutObject.getId());
                        workout_values.put(EasyFitnessContract.WorkOutOptions.COLUMN_WORKOUT_DESCRIPTION, workoutObject.getWorkout());
                        workoutOptions_values.add(workout_values);
                        System.out.println("values size : " + workoutOptions_values.size());
                    }


                        System.out.println("*****inserting values*****");
                        ContentValues[] cvArray = new ContentValues[workoutOptions_values.size()];
                    workoutOptions_values.toArray(cvArray);
                        int inserted_into_DB = mContext.getContentResolver().bulkInsert(
                                EasyFitnessContract.WorkOutOptions.CONTENT_URI, cvArray);
                        Log.v(Log_tag, "WORKOUT OPTIONS Succesfully Inserted : " + String.valueOf
                                (workoutOptions_values.size()) + "******* " + inserted_into_DB);


                    System.out.println("***** Out of for loop*****");
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }

            });

//*************************************************************
            final Firebase ref_workout_records = new Firebase(mContext.getResources().getString(R.string
                    .firebase_url)
                    +"/recordedWorkoutList/"+authId);

            ref_workout_records.addListenerForSingleValueEvent
                    (new

    ValueEventListener() {

        @Override
        public void onDataChange (DataSnapshot workout_records){

            ref_workout_records.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot querySnapshot) {
                    for (DataSnapshot mWorkoutRecordsYear : querySnapshot.getChildren()) {
                        //BlogPost post = postSnapshot.getValue(BlogPost.class);
                        for (DataSnapshot mWorkoutRecordsMonth : mWorkoutRecordsYear
                                .getChildren()) {
                            for (DataSnapshot mWorkoutRecordsDate : mWorkoutRecordsMonth
                                    .getChildren()) {
                                for (DataSnapshot mWorkoutRecordsDetail :
                                        mWorkoutRecordsDate
                                                .getChildren()) {

                                    WorkoutRecord workoutRecord = mWorkoutRecordsDetail
                                            .getValue(WorkoutRecord.class);
                                    String year = mWorkoutRecordsYear.getKey().toString();
                                    String month = mWorkoutRecordsMonth.getKey()
                                            .toString();
                                    String date = mWorkoutRecordsDate.getKey().toString();

                                    String pushID = mWorkoutRecordsDetail
                                            .getKey();

                                    System.out.println(year + "-" + month + "-" + date);
                                    System.out.println(workoutRecord.getWorkoutDesc() + " - " +
                                            workoutRecord
                                                    .getWorkoutDuration());

                                    Date record_workout_fulldate = new Date((Integer.parseInt
                                            (year)-1900), (Utilities.getMonthinNumber(month)-1),
                                            Integer.parseInt(date));
                                    ContentValues userRecoredeworkoutValues = new ContentValues();
                                    ContentValues record_values = new
                                            ContentValues();
                                    record_values.put
                                            (EasyFitnessContract.UserWorkOutRecord.COLUMN_USERDEATIL_AUTHENTIFICATION_ID,
                                                    authId);
                                    record_values.put(EasyFitnessContract.UserWorkOutRecord.COLUMN_WORKOUT_DESCRIPTION,
                                            workoutRecord.getWorkoutDesc());
                                    record_values.put(EasyFitnessContract.UserWorkOutRecord
                                            .COLUMN_WORKOUT_DURATION, workoutRecord.getWorkoutDuration());
                                    record_values.put(EasyFitnessContract.UserWorkOutRecord
                                                    .COLUMN_WORKOUT_RECORDED_DATE_YEAR,
                                            year);
                                    record_values.put(EasyFitnessContract.UserWorkOutRecord
                                                    .COLUMN_WORKOUT_RECORDED_DATE_MONTH,
                                            Utilities.getMonthinNumber
                                                    (month));
                                    record_values.put(EasyFitnessContract.UserWorkOutRecord
                                                    .COLUMN_WORKOUT_RECORDED_DATE_DATE,
                                            date);
                                    record_values.put(EasyFitnessContract.UserWorkOutRecord
                                                    .COLUMN_PUSH_ID,
                                            pushID);
                                    record_values.put(EasyFitnessContract.UserWorkOutRecord
                                            .COLUMN_FULL_DATE, String.valueOf(record_workout_fulldate));
                                    workoutRecords_values.add(record_values);
                                    System.out.println("values size : " + workoutRecords_values.size());
                                }
                            }
                        }


                        System.out.println("*****inserting values*****");
                        ContentValues[] cvArrayWR = new
                                ContentValues[workoutRecords_values.size()];
                        workoutRecords_values.toArray(cvArrayWR);
                        int records_inserted_intoDB = mContext
                                .getContentResolver().bulkInsert(
                                        EasyFitnessContract.UserWorkOutRecord.CONTENT_URI, cvArrayWR);
                        Log.v(Log_tag, "WORKOUT RECORDS Succesfully " +
                                "Inserted : " + String.valueOf
                                (workoutRecords_values.size()) + "******* " + records_inserted_intoDB);


                    }
                }

                public void onCancelled(FirebaseError error) {
                }
            });
        }

        @Override
        public void onCancelled (FirebaseError error){
        }
    });
        }
        System.out.println("*****ASYNC TASK ENDED*****");


        return null;
    }

   /* @Override
    protected void onPostExecute(List<MyTrack> listOfTracks) {


        if (listOfTracks.size() != 0) {
            msearchViewOpenPageLayout.setVisibility(View.INVISIBLE);
            msearchViewOpenPageImage.setVisibility(View.INVISIBLE);
            mTextView.setVisibility(TextView.INVISIBLE);

            mlistview.setVisibility(View.VISIBLE);
            resultList.clear();
            for (MyTrack track : listOfTracks) {
                resultList.add(track);
            }
            // New data is back from the server.  Hooray!

        } else if (listOfTracks.size() == 0) {
            msearchViewOpenPageLayout.setVisibility(View.VISIBLE);
            msearchViewOpenPageImage.setVisibility(View.VISIBLE);
            mTextView.setVisibility(TextView.VISIBLE);
            mlistview.setVisibility(View.INVISIBLE);
            mTextView.setText("No results found for this artist ");
        }
        mlistadapter.notifyDataSetChanged();
        mlistview.setEmptyView(emptyView);
        mlistview.setAdapter(mlistadapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MyTrack mtrack = resultList.get(position);
                String spotifyId = mtrack.getSpotifyArtistID();
                String name = mtrack.getArtist_names();
                ArtistName parcelArtistObj = new ArtistName(name, spotifyId);

                ((Callback) getActivity()).onItemSelected(parcelArtistObj);
                    *//*Intent intent = new Intent(getActivity(), ToptracksActivity.class).putExtra(Intent.EXTRA_TEXT,
                            parcelArtistObj);
                    startActivity(intent);*//*
            }
        });


    }*/

}
