package ru.example.taskweb.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.example.taskweb.dto.UserDto;
import ru.example.taskweb.entity.Role;
import ru.example.taskweb.entity.Task;
import ru.example.taskweb.entity.User;
import ru.example.taskweb.repository.RoleRepository;
import ru.example.taskweb.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                           UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        Role roleReadOnly = roleRepository.findByName("ROLE_READ");;
        Role roleUser = roleRepository.findByName("ROLE_USER");
        if (roleUser == null) {
            roleUser = checkRoleExist("ROLE_USER");
        }
        if (roleReadOnly == null) {
            roleReadOnly = checkRoleExist("ROLE_READ");
        }
        if (roleAdmin != null) {
            user.setRoles(Arrays.asList(roleReadOnly));
        }
        if (roleAdmin == null) {
            roleAdmin = checkRoleExist("ROLE_ADMIN");
            user.setRoles(Arrays.asList(roleAdmin));
        }
        userRepository.save(user);
    }

    @Override
    public void giveAdminRole(@RequestParam String email, ModelAndView mav, BindingResult bindingResult) {
        User user = userRepository.findByEmail(email);
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        if (user != null && roleAdmin != null) {
            user.setRoles(new ArrayList<>(Collections.singletonList(roleAdmin)));
        } else {
            bindingResult.rejectValue("error", "error", "Невозможно присвоить роль." +
                    " Пользователь или роль не найдены.");
            mav.setViewName("add-remainingTime-form");
            mav.addObject("error", "Ошибка при вычислении оставшегося времени.");
        }
        userRepository.save(user);
    }
    @Override
    public void giveUserRole(@RequestParam String email, ModelAndView mav, BindingResult bindingResult) {
        User user = userRepository.findByEmail(email);
        Role roleUser = roleRepository.findByName("ROLE_USER");
        if (user != null && roleUser != null) {
            user.setRoles(new ArrayList<>(Collections.singletonList(roleUser)));
        } else {
            bindingResult.rejectValue("error", "error", "Невозможно присвоить роль." +
                    " Пользователь или роль не найдены.");
            mav.setViewName("add-remainingTime-form");
            mav.addObject("error", "Ошибка при вычислении оставшегося времени.");
        }
        userRepository.save(user);
    }
    @Override
    public void giveReadRole(@RequestParam String email, ModelAndView mav, BindingResult bindingResult) {
        User user = userRepository.findByEmail(email);
        Role roleRead = roleRepository.findByName("ROLE_READ");
        if (user != null && roleRead != null) {
            user.setRoles(new ArrayList<>(Collections.singletonList(roleRead)));
        } else {
            bindingResult.rejectValue("error", "error", "Невозможно присвоить роль." +
                    " Пользователь или роль не найдены.");
            mav.setViewName("add-remainingTime-form");
            mav.addObject("error", "Ошибка при вычислении оставшегося времени.");
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUserByEmail(String userEmail, Model model) {
        User user = userRepository.findByEmail(userEmail);
        if (user != null) {
            user.getRoles().clear();
            user.getReleases().clear();
            user.getTasks().clear();
            try {
                userRepository.delete(user);
            } catch (Exception e) {
                model.addAttribute("error", "Ошибка при удалении пользователя: "
                        + e.getMessage());
            }
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByTask(Task task) {
        User user = userRepository.findByTasks(task);
        return user;
    }

    public List<Object> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private Object mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        String roles = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.joining(", "));

        userDto.setRole(roles);
        return userDto;
    }

    private Role checkRoleExist(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }
}
