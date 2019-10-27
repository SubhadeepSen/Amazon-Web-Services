package lambda.repo;

import java.util.ArrayList;
import java.util.List;

import lambda.model.Student;

public class StudentRepository {

	private static List<Student> students = new ArrayList<>();

	private StudentRepository() {
	}

	public static Student getStudentById(String id) {
		Student student = students.stream().filter(std -> std.getId().equals(id)).findFirst().orElse(null);
		return student;
	}

	public static Student addStudent(Student student) {
		if (null != student) {
			student.setId(String.valueOf(students.size() + 1));
			students.add(student);
		}
		return student;
	}
}
