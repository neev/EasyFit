package com.neeraja.android.easyfit;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.ArrayList;
import java.util.Map;

import hirondelle.date4j.DateTime;

public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {

	public CaldroidSampleCustomAdapter(Context context, int month, int year,
			Map<String, Object> caldroidData,
			Map<String, Object> extraData) {
		super(context, month, year, caldroidData, extraData);



	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View cellView = convertView;

		// For reuse
		if (convertView == null) {
			cellView = inflater.inflate(R.layout.custom_cell_pinkdot, null);
		}

		int topPadding = cellView.getPaddingTop();
		int leftPadding = cellView.getPaddingLeft();
		int bottomPadding = cellView.getPaddingBottom();
		int rightPadding = cellView.getPaddingRight();

		TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
		ImageView image_pinkdot = (ImageView) cellView.findViewById(R.id.image_pinkdot);
		//TextView tv2 = (TextView) cellView.findViewById(R.id.cal_dur_text);

		tv1.setTextColor(Color.BLACK);

		// Get your data here
		ArrayList yourCustomData1 = (ArrayList) extraData.get("month_KEY1");
		ArrayList yourCustomData2 = (ArrayList) extraData.get("date_KEY2");

System.out.println("KEy1 :" + yourCustomData1 + "Key 2 :" + yourCustomData2);

		// Get dateTime of this cell
		DateTime dateTime = this.datetimeList.get(position);
		for(int m=0;m<yourCustomData1.size();m++){
			if(yourCustomData1.get(m)== dateTime.getMonth()){
				if (yourCustomData2.get(m) == dateTime.getDay()) {
					image_pinkdot.setVisibility(View.VISIBLE);
				}
			}
		}




		Resources resources = context.getResources();




		// Set color of the dates in previous / next month
		if (dateTime.getMonth() != month) {
			tv1.setTextColor(resources
					.getColor(com.caldroid.R.color.caldroid_darker_gray));
		}

		boolean shouldResetDiabledView = false;
		boolean shouldResetSelectedView = false;

		// Customize for disabled dates and date outside min/max dates
		if ((minDateTime != null && dateTime.lt(minDateTime))
				|| (maxDateTime != null && dateTime.gt(maxDateTime))
				|| (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

			tv1.setTextColor(CaldroidFragment.disabledTextColor);

			if (CaldroidFragment.disabledBackgroundDrawable == -1) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
			} else {
				cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
			}

			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
			}

		} else {
			shouldResetDiabledView = true;
		}

		// Customize for selected dates
		if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
			cellView.setBackgroundColor(resources
					.getColor(com.caldroid.R.color.caldroid_sky_blue));

			tv1.setTextColor(Color.BLACK);

		} else {
			shouldResetSelectedView = true;
		}

		if (shouldResetDiabledView && shouldResetSelectedView) {
			// Customize for today
			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
			} else {
				cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
			}
		}

		tv1.setText("" + dateTime.getDay());

		//image_pinkdot.setImageResource(R.drawable.pinkdot);
		//tv2.setText("30 min");

		// Somehow after setBackgroundResource, the padding collapse.
		// This is to recover the padding
		cellView.setPadding(leftPadding, topPadding, rightPadding,
				bottomPadding);

		// Set custom color if required
		setCustomResources(dateTime, cellView, tv1);

		return cellView;
	}


}
