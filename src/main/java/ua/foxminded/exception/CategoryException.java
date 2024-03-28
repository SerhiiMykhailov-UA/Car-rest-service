package ua.foxminded.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CategoryException extends Exception {

	private static final long serialVersionUID = 1L;

	public CategoryException(String message) {
		super(message);
	}

}
