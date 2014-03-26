package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CostumArrayAdapter extends ArrayAdapter<Task> {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

	public CostumArrayAdapter(Context context, int textViewResourceId,
			List<Task> list) {
		super(context, textViewResourceId, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.row, null);
		}

		TextView task = (TextView)v.findViewById(R.id.txtTodoTitle);
		task.setText(getItem(position).taskStr);
		TextView dateText = (TextView)v.findViewById(R.id.txtTodoDueDate);
		Date date = getItem(position).date;
		if (date != null) {
			dateText.setText(sdf.format(date));
			if (date.compareTo(new Date()) < 0) {
				task.setTextColor(Color.RED);
				dateText.setTextColor(Color.RED);
			} else {
				task.setTextColor(Color.BLACK);
				dateText.setTextColor(Color.BLACK);
			}
		} else {
			dateText.setText("No due date");
			task.setTextColor(Color.BLACK);
			dateText.setTextColor(Color.BLACK);
		}

		return v;
	}

}
