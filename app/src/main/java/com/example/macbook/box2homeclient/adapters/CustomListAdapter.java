package com.example.macbook.box2homeclient.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.macbook.box2homeclient.R;
import com.example.macbook.box2homeclient.models.Order;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {
	private Activity _mContext;
	private LayoutInflater inflater;
	private List<Order> myOrderModelItems;
	private String activityName;
	FragmentTransaction fragmentTransaction;

	public CustomListAdapter(Activity activity, List<Order> myOrderModelItems,
                             String activityName, FragmentTransaction fragmentTransaction) {
		this._mContext = activity;
		this.myOrderModelItems = myOrderModelItems;
		this.activityName=activityName;
		this.fragmentTransaction =fragmentTransaction;
	}

	@Override
	public int getCount() {
		return myOrderModelItems.size();
	}

	@Override
	public Object getItem(int location) {
		return myOrderModelItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) _mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		TextView _date = (TextView) convertView.findViewById(R.id.txt_date);
		TextView _adresse_debut = (TextView) convertView.findViewById(R.id.txt_adresse_debut);
		TextView _adresse_fin = (TextView) convertView.findViewById(R.id.txt_adresse_fin);
		TextView _prix = (TextView) convertView.findViewById(R.id.txt_prix);

		if (!myOrderModelItems.isEmpty()) {
			// getting movie data for the row
			final Order m = myOrderModelItems.get(position);
			_date.setText(m.getDateCreation()+" "+m.getHeureCourse());
			_adresse_debut.setText(m.getDepart());
			_adresse_fin.setText(m.getArrivee());
			_prix.setText(m.getMontantMax()+" $");

		}

		return convertView;
	}
	public void clear() {
 		myOrderModelItems.clear();

	}

}