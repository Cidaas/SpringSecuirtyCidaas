package de.cidaas.spring.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.servlet.ServletRequest;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@ComponentScan(basePackages = {"de.cidaas"})
public class APIController {

	@RequestMapping(method = RequestMethod.GET, value = "/myprofile")

	public ResponseEntity<Map<String, String>> myprofile() {
		Map<String, String> profileObj = new HashMap<String, String>();
		profileObj.put("firstName", "john");
		profileObj.put("lastName", "wiliams");
		profileObj.put("role", "USER");
		return new ResponseEntity<Map<String, String>>(profileObj, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/v1/api/myprofile1")

	public ResponseEntity<Map<String, String>> myprofile1() {
		Map<String, String> profileObj = new HashMap<String, String>();
		profileObj.put("firstName", "wiliams");
		profileObj.put("lastName", "john");
		profileObj.put("role", "USER");
		return new ResponseEntity<Map<String, String>>(profileObj, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/v1/api/myprofile2")

	public ResponseEntity<Map<String, String>> myprofile2() {
		Map<String, String> profileObj = new HashMap<String, String>();
		profileObj.put("firstName", "wiliams");
		profileObj.put("lastName", "john");
		profileObj.put("role", "USER");
		return new ResponseEntity<Map<String, String>>(profileObj, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/api/employeelist")

	public ResponseEntity<List<?>> employeeList(ServletRequest request) {
		List<Object> empList = new ArrayList<Object>();
		Map<String, String> profileObj1 = new HashMap<String, String>();
		profileObj1.put("firstName", "john");
		profileObj1.put("lastName", "wiliams");
		profileObj1.put("role", "USER");
		empList.add(profileObj1);
		Map<String, String> profileObj2 = new HashMap<String, String>();
		profileObj2.put("firstName", "David");
		profileObj2.put("lastName", "Johnson");
		profileObj2.put("role", "USER");
		empList.add(profileObj2);
		return new ResponseEntity<List<?>>(empList, HttpStatus.OK);
	}

	// checks if a caller has one of the scopes defined in scopes.

	@RequestMapping(method = RequestMethod.GET, value = "/api/holidaylist")

	public ResponseEntity<List<?>> holidayList(ServletRequest request) {
		List<Object> holidayList = new ArrayList<Object>();
		Map<String, String> holidayObj1 = new HashMap<String, String>();
		holidayObj1.put("Date", "1-1-2019");
		holidayObj1.put("reason", "New year");
		holidayList.add(holidayObj1);
		Map<String, String> holidayObj2 = new HashMap<String, String>();
		holidayObj2.put("Date", "25-12-2019");
		holidayObj2.put("reason", "Christmas Day");
		holidayList.add(holidayObj2);
		return new ResponseEntity<List<?>>(holidayList, HttpStatus.OK);
	}

	//@PermitAll
	@RequestMapping(method = RequestMethod.GET, value = "/public/calendar")
	@PermitAll
	public ResponseEntity<List<?>> localHolidayList(ServletRequest request) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");

		List<Object> holidayList = new ArrayList<Object>();
		Map<String, String> holidayObj1 = new HashMap<String, String>();
		holidayObj1.put("Date", "1-1-2019");
		holidayObj1.put("reason", "New year");
		holidayList.add(holidayObj1);
		Map<String, String> holidayObj2 = new HashMap<String, String>();
		holidayObj2.put("Date", "25-3-2019");
		holidayObj2.put("reason", "Christmas Day");
		holidayList.add(holidayObj2);
		return new ResponseEntity<List<?>>(holidayList, HttpStatus.OK);
	}

	// DenyAll -> Deactivates the rest service.
	
	@RequestMapping(method = RequestMethod.GET, value = "/leavetype")
	@DenyAll
	public ResponseEntity<List<?>> leaveType() {
		List<String> leaveTypeList = new ArrayList<String>();
		leaveTypeList.add("Vacation Leave");
		leaveTypeList.add("Sick Leave");
		return new ResponseEntity<List<?>>(leaveTypeList, HttpStatus.FORBIDDEN);
	}

}
