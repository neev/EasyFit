package com.example.android.easyfitness;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

class TodoCursorAdapter extends CursorAdapter {
   public TodoCursorAdapter(Context context, Cursor cursor, int flags) {
       super(context, cursor, 0);
   }

   // The newView method is used to inflate a new view and return it,
   // you don't bind any data to the view at this point.
   @Override
   public View newView(Context context, Cursor cursor, ViewGroup parent) {
       return LayoutInflater.from(context).inflate(R.layout.activity_profile, parent, false);
   }

   // The bindView method is used to bind all data to a given view
   // such as setting the text on a TextView.
   @Override
   public void bindView(View view, Context context, Cursor cursor) {




       // Find fields to populate in inflated template
       TextView text_name = (TextView) view.findViewById(R.id.tvNumber4);
       TextView text_email = (TextView) view.findViewById(R.id.tvNumber3);
       TextView text_weight = (TextView) view.findViewById(R.id.tvNumber1);
       TextView text_goal = (TextView) view.findViewById(R.id.tvNumber2);
       ImageView profileImage = (ImageView) view.findViewById(R.id.imageView_profile);
       // Extract properties from cursor
       String name = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
       String email = cursor.getString(cursor.getColumnIndexOrThrow("user_email"));
       int weight = cursor.getInt(cursor.getColumnIndexOrThrow("user_weight"));
       int goal_weight = cursor.getInt(cursor.getColumnIndexOrThrow("user_goal_weight"));
       String selectedImage = cursor.getString(cursor.getColumnIndexOrThrow("image_data"));
       byte[] byteValue = selectedImage .getBytes();
       Bitmap imageProfilebitmap = Utilities.getImage(byteValue);
       profileImage.setImageBitmap(imageProfilebitmap);

      /* if(selectedImage != null) {
           ByteArrayOutputStream stream = new ByteArrayOutputStream();
           selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
           selectedImage.recycle();
           byte[] byteArray = stream.toByteArray();
           String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);*/

       // Populate fields with extracted properties
       text_name.setText(name);
       text_email.setText(email);
       text_weight.setText(String.valueOf(weight));
       text_goal.setText(String.valueOf(goal_weight));

   }
}
