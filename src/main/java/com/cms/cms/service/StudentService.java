package com.cms.cms.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cms.cms.dto.StudentDto;
import com.cms.cms.entity.StudentInfo;
import com.cms.cms.repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;
	
	public void saveStudent(StudentDto stddto) throws Exception{
		
		if(studentRepository.existsByEmail(stddto.getEmail())){
			throw new Exception("Email already exists.");
		}
		
		StudentInfo si=new StudentInfo();

		si.setRollno(stddto.getRollno());
		si.setName(stddto.getName());
		si.setCourse(stddto.getCourse());
		si.setEmail(stddto.getEmail());
		si.setMobile(stddto.getMobile());
		
		MultipartFile file=stddto.getPhoto();
		
		if(file !=null && !file.isEmpty()){
			 
			String fileName=file.getOriginalFilename();
			
			Path uploadPath=Paths.get("uploads");
			
			Files.createDirectories(uploadPath);
			
			Files.copy(
					file.getInputStream(), 
					uploadPath.resolve(fileName),
					StandardCopyOption.REPLACE_EXISTING
					);
			si.setPhoto(fileName);
			
		}	
		studentRepository.save(si);
		
	}
	
	public List<StudentInfo> getAllStudents(){
		return studentRepository.findAll();
	}
	
	public StudentInfo getStudentByRollno(String rollno){
		return studentRepository.findById(rollno).orElse(null);
	}
	
	public void deleteStudent(String rollno){
		studentRepository.deleteById(rollno);
	}
	
	public void updateStudent(StudentDto stddto) throws Exception{
		StudentInfo oldStudent=studentRepository.findById(stddto.getRollno()).orElse(null);
		
		if(oldStudent !=null) {
			
			StudentInfo existingStudent= studentRepository.findByEmail(stddto.getEmail());
			
			if(existingStudent !=null && !existingStudent.getEmail().equals(stddto.getEmail())) {
				throw new Exception("Email already exists.");
			}			
			
			oldStudent.setRollno(stddto.getRollno());
			oldStudent.setName(stddto.getName());
			oldStudent.setCourse(stddto.getCourse());
			oldStudent.setEmail(stddto.getEmail());
			oldStudent.setMobile(stddto.getMobile());
			
			MultipartFile file=stddto.getPhoto();
			
			if(file !=null && !file.isEmpty()){
				String fileName=  System.currentTimeMillis()+"_"+file.getOriginalFilename();
				
				Path uploadPath=Paths.get("uploads");
				
				Files.createDirectories(uploadPath);
				
				Files.copy(
						file.getInputStream(),
						uploadPath.resolve(fileName),
						StandardCopyOption.REPLACE_EXISTING
						);
				
				oldStudent.setPhoto(fileName);
			}
				studentRepository.save(oldStudent);
		}
	}
	
	public List<StudentInfo> searchStudents(String keyword){
		
		if(keyword==null || keyword.trim().isEmpty()){
			return studentRepository.findAll();
		}
		
		return studentRepository.searchStudent(keyword);
	}
	
}
