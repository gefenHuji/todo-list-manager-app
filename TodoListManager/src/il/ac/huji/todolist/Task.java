package il.ac.huji.todolist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
	
	private long id;
	private String title;
	private Date date;

	public Task() {
	}
	
	public Task(String title, Date date) {
		this.title = title;
		this.date = date;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}
	
	public String getDateAsString() {
		return sdf.format(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setDateAsString(String date) {
		try {
			this.date = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
