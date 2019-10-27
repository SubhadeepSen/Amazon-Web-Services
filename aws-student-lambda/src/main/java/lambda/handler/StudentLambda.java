package lambda.handler;

import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import lambda.model.Student;
import lambda.repo.StudentRepository;
import lambda.request.Request;

public class StudentLambda implements RequestHandler<Request, Student> {

	private final static Logger LOGGER = Logger.getLogger("Student Lambda");

	@Override
	public Student handleRequest(Request request, Context context) {
		Student std = null;
		LOGGER.info("Method: " + request.getHttpMethod());
		switch (request.getHttpMethod()) {
		case "GET":
			std = StudentRepository.getStudentById(request.getId());
		case "POST":
			std = StudentRepository.addStudent(request.getStudent());
			break;
		default:
			break;
		}
		return std;
	}

}
