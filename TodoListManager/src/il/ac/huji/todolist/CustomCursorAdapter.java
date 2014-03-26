package il.ac.huji.todolist;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CustomCursorAdapter extends SimpleCursorAdapter {

	private Context context;

	public CustomCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to, 0);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.row, null);
		}

		TextView titleView = (TextView)v.findViewById(R.id.txtTodoTitle);
		TextView dateView = (TextView)v.findViewById(R.id.txtTodoDueDate);
		String dateString = dateView.getText().toString();
		Task task = new Task();
		task.setDateAsString(dateString);
		Date date = task.getDate();

		if (date != null) {
			if (date.compareTo(new Date()) < 0) {
				titleView.setTextColor(Color.RED);
				dateView.setTextColor(Color.RED);
			} else {
				titleView.setTextColor(Color.BLACK);
				dateView.setTextColor(Color.BLACK);
			}
		} else {
			dateView.setText("No due date");
			titleView.setTextColor(Color.BLACK);
			dateView.setTextColor(Color.BLACK);
		}
		return v;
	}
}
