package il.ac.huji.todolist;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CostumArrayAdapter extends ArrayAdapter<String> {
	
	public CostumArrayAdapter(Context context, int textViewResourceId,
			List<String> list) {
		super(context, textViewResourceId, list);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.row, null);
		}
		
		TextView text = (TextView)v.findViewById(R.id.new_task);
		text.setText(getItem(position));
		if (position%2==0) {			
			text.setTextColor(Color.RED);
		} else {
			text.setTextColor(Color.BLUE);
		}
		
		return v;
	}

}
