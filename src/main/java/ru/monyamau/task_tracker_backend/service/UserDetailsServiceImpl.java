package ru.monyamau.task_tracker_backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.monyamau.task_tracker_backend.entity.User;
import ru.monyamau.task_tracker_backend.repository.UserRepository;
import ru.monyamau.task_tracker_backend.security.UserPrincipal;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Не удалось найти пользователя по текущей почте"));
        return new UserPrincipal(user.getId(), user.getEmail(), user.getPassword());
    }
}
