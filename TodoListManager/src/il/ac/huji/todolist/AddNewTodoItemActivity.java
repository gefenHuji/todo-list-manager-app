package il.ac.huji.todolist;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	private Button ok;
	private Button cancel;
	private DatePicker date;
	private EditText task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_todo_item);
		setTitle("Add New Item");

		ok = (Button) findViewById(R.id.btnOK);
		cancel = (Button) findViewById(R.id.btnCancel);
		task = (EditText) findViewById(R.id.edtNewItem);
		date = (DatePicker) findViewById(R.id.datePicker);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String newTask = task.getText().toString();
				Calendar calendar = Calendar.getInstance();
				calendar.set(date.getYear(), date.getMonth(), date.getDayOfMonth());
				Intent result = new Intent();
				result.putExtra("title", newTask);
				result.putExtra("dueDate", calendar.getTime());
				setResult(RESULT_OK, result);
				finish();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);        
				finish();
			}
		});
	}

}
