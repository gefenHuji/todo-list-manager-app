package il.ac.huji.todolist;

import java.util.Date;

public class Task {
	public String taskStr;
	public Date date;
	
	public Task(String task, Date date) {
		this.taskStr = task;
		this.date = date;
	}
}
