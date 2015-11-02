package com.github.ffpojo.decorator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.beans.Employee;
import com.github.ffpojo.beans.Manager;
import com.github.ffpojo.beans.Project;
import com.google.common.truth.Truth;

public class CollectionDecoratorTest {
	
	FFPojoHelper ffpojo;
	
	@Before
	public void init(){
		ffpojo =  FFPojoHelper.getInstance();
	}

	@Test
	public void testToStringList() {
		final Employee funcionario =  createEmployee();
		final String line = ffpojo.parseToText(funcionario);
		final Employee expected = ffpojo.createFromText(Employee.class, line);
		Truth.assert_().that(line.length()).isEqualTo(550);
		Truth.assert_().that(expected.getProjects().size()).isEqualTo(20);
	}
	
	@Test
	public void testFromStringList() {
		final String line = createStringToTestFromStringCollection(); 
		final Employee expected = createEmployee();
		final Employee actual = ffpojo.createFromText(Employee.class, line);
		Truth.assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	public void testToStringSet(){
		final Manager manager = createManager(); 
		final String line = ffpojo.parseToText(manager);
		Truth.assertThat(line.length()).isEqualTo(11030);
		Truth.assertThat(manager.getEmployees().size()).isEqualTo(2);
	}
	
	@Test
	public void testToFromStringSet(){
		final String line = createStringToTestManager();
		final Manager expected = createManager();
		final Manager actual = ffpojo.createFromText(Manager.class, line);
		Truth.assertThat(line.length()).isEqualTo(11030);
		Truth.assertThat(actual).isEqualTo(expected);
	}

	private Manager createManager() {
		final Manager manager =  new Manager();
		manager.setName("Gilberto");
		manager.setLasName("Holms");
		manager.getEmployees().add(createEmployee());
		manager.getEmployees().add(createEmployee("Benjamin"));
		return manager;
	}

	private Employee createEmployee(){
		return createEmployee("William");
	}
	private Employee createEmployee(String name){
		Employee employee =  new Employee();
		employee.setName(name);
		employee.setLastName("Miranda de Jesus");
		employee.setProjects(criarListProjetos());
		return employee;
	}
	
	private String createStringToTestManager(){
		return ffpojo.parseToText(createManager());
	}
	private String createStringToTestFromStringCollection(){
		return ffpojo.parseToText(createEmployee());
	}
	
	private List<Project> criarListProjetos() {
		List<Project> projetos =  new ArrayList<Project>();
		Calendar cal =  Calendar.getInstance();
		for(int i=1; i <=20; i++) {
			int monthBefore = (int) Math.round(Math.random()*100);
			cal.add(Calendar.MONTH, -monthBefore);
			Project projeto =  new Project();
			projeto.setDescription("Project " + i);
			int monthAfter = (int) Math.round(Math.random()*100);
			projeto.setStartDate(cal.getTime());
			cal.add(Calendar.MONTH, monthAfter);
			projeto.setEndDate(cal.getTime());
			projetos.add(projeto);
		}
		return projetos;
	}

//	@Test
//	public void testFromString() {
//		fail("Not yet implemented");
//	}


	
	
}
