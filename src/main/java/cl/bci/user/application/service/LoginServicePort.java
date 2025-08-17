package cl.bci.user.application.service;

import java.util.Optional;

import cl.bci.user.domain.User;

public interface LoginServicePort {
	Optional<String> login(User user);
}
