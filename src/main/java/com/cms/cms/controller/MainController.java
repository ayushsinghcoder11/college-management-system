package com.cms.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cms.cms.dto.StudentDto;
import com.cms.cms.entity.StudentInfo;
import com.cms.cms.service.StudentService;
/*import org.springframework.web.bind.annotation.RestController;*/

/* @RestController */
@Controller
public class MainController {
	
	@Autowired
	private StudentService studentService;

	@GetMapping("/")
	public String showIndex(Model model){
		model.addAttribute("totalStudents", studentService.getAllStudents().size());
		return"index";
	}
	
	@GetMapping("/addstudent")
	public String addStudent(Model model){
		model.addAttribute("studentDto",new StudentDto());
		return"addstudent";
	}
	
	@PostMapping("/addstudent")
	public String saveStudent(@ModelAttribute StudentDto studentDto, RedirectAttributes rediAttributes){
		try {
			 studentService.saveStudent(studentDto);
			 rediAttributes.addFlashAttribute("msg", "Student added successfully.");
		} catch (Exception e) {
			rediAttributes.addFlashAttribute("msg", "Something went wrong."+e.getMessage());
		}
		return"redirect:/viewstudent";
	}
	
	@GetMapping("/viewstudent")
	public String showStudent(@RequestParam(value="keyword", required=false) String Keyword,Model model){
		 model.addAttribute("students", studentService.searchStudents(Keyword));
		 model.addAttribute("keyword", Keyword);
		return"viewstudent";
	}
	
	@GetMapping("/editstudent/{rollno}")
	public String editstudent(@PathVariable String rollno,Model model,RedirectAttributes redirectAttributes){
		StudentInfo student=studentService.getStudentByRollno(rollno);
		if(student ==null){
			redirectAttributes.addFlashAttribute("msg", "Student not found.");
			return"redirect:/viewstudent";
		}
		StudentDto stddto=new StudentDto();
		stddto.setRollno(student.getRollno());
		stddto.setName(student.getName());
		stddto.setCourse(student.getCourse());
		stddto.setEmail(student.getEmail());
		stddto.setMobile(student.getMobile());
		
		model.addAttribute("studentDto", stddto);
		model.addAttribute("oldPhoto", student.getPhoto());
		
		return "editstudent";	
	}
	
	@PostMapping("/editstudent")
	public String updateStudent(@ModelAttribute StudentDto studentDto, RedirectAttributes redirectAttributes){
		
		try {
			studentService.updateStudent(studentDto);
			redirectAttributes.addFlashAttribute("msg", "Student Updated Successfully.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("msg", "Something went wrong."+e.getMessage());
		}
		return "redirect:/viewstudent";
	}
	
	@GetMapping("/deletestudent/{rollno}")
	public String deleteStudent(@PathVariable String rollno, RedirectAttributes redirectAttributes){
		try {
			studentService.deleteStudent(rollno);
			redirectAttributes.addFlashAttribute("msg","Student deleted Successfully.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("msg", "Something went wrong."+e.getMessage());
		}
		return"redirect:/viewstudent";
	}
	
}
