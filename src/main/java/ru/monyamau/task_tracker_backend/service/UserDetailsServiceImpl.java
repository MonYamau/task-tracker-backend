package ru.monyamau.task_tracker_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.monyamau.task_tracker_backend.entity.User;
import ru.monyamau.task_tracker_backend.repository.UserRepository;
import ru.monyamau.task_tracker_backend.security.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Не удалось найти пользователя по текущему имени"));
        return new UserDetailsImpl(user);
    }
}
