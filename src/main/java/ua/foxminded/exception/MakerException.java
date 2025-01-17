package ua.foxminded.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class MakerException extends Exception {

	private static final long serialVersionUID = 1L;

	public MakerException(String message) {
		super(message);
	}

}
