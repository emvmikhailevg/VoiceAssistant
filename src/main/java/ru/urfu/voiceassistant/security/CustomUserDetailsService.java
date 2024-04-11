package ru.urfu.voiceassistant.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.urfu.voiceassistant.database.model.User;
import ru.urfu.voiceassistant.database.model.Role;
import ru.urfu.voiceassistant.database.repository.UserRepository;
import ru.urfu.voiceassistant.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис пользовательских данных для Spring Security.
 * Реализует интерфейс {@link UserDetailsService} для загрузки пользовательских данных из базы данных.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * Конструктор с параметром {@link UserRepository} для внедрения зависимости.
     *
     * @param userService Сервис для работы с пользователями.
     */
    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Загружает данные пользователя по электронной почте для аутентификации в Spring Security.
     *
     * @param email электронная почта пользователя.
     * @return объект {@link UserDetails}, представляющий пользователя.
     * @throws UsernameNotFoundException выбрасывается, если пользователь не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User uniqueUser = userService.findUserByEmail(email);

        if (uniqueUser != null) {
            return new org.springframework.security.core.userdetails.User(
                    uniqueUser.getEmail(),
                    uniqueUser.getPassword(),
                    mapRolesToAuthorities(uniqueUser.getRoles()));
        } else {
            throw new UsernameNotFoundException("Неверный email или имя пользователя");
        }
    }

    /**
     * Преобразует список ролей пользователя в коллекцию GrantedAuthority.
     *
     * @param roleEntities список ролей пользователя.
     * @return коллекция GrantedAuthority.
     */
    private Collection <? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roleEntities) {
        return roleEntities
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getLogin()))
                .collect(Collectors.toList());
    }
}
