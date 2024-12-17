package org.example.course_server.service;

import org.example.course_server.entity.User;
import org.example.course_server.entity.UserType;
import org.example.course_server.repository.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Инициализация
    }

    public User registerUser(User user) {
        // Хэшируем пароль перед сохранением
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        if (user.getUserType() == null) {
            user.setUserType(UserType.CUSTOMER);  // Устанавливаем тип по умолчанию
        }
        return userRepo.save(user);
    }
    public List<User> findAll(){return userRepo.findAll();}

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public boolean checkPassword(String rawPassword, User user) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public boolean existsByUsername(String username) {
        return userRepo.findByUsername(username).isPresent();
    }
}

