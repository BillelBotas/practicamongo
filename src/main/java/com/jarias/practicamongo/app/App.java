package com.jarias.practicamongo.app;

import com.jarias.practicamongo.mvc.Controller;
import com.jarias.practicamongo.mvc.Model;
import com.jarias.practicamongo.mvc.View;

public class App {
	public static void main(String[] args) {
		View view = new View();
		Model model = new Model();
		Controller controller = new Controller(view, model);
	}
}
