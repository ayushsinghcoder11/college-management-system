package com.cms.cms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cms.cms.entity.StudentInfo;

public interface StudentRepository extends JpaRepository<StudentInfo,String> {

	boolean existsByEmail(String email);
	
	StudentInfo findByEmail(String email);
	
	@Query("SELECT s FROM StudentInfo s WHERE " +
		       "s.rollno LIKE %:keyword% OR " +
		       "s.name LIKE %:keyword% OR " +
		       "s.email LIKE %:keyword% OR " +
		       "s.course LIKE %:keyword% OR " +
		       "s.mobile LIKE %:keyword%")
	List<StudentInfo> searchStudent(String keyword);
}
