import org.hibernate.annotations.DialectOverride.OverridesAnnotation;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.timperio.crm.dto.UserDto;
import com.example.timperio.crm.entity.User;
import com.example.timperio.crm.mapper.UserMapper;
import com.example.timperio.crm.repository.UserRepository;
import com.example.timperio.crm.reusables.exceptions.UserAlreadyExistsException;
import com.example.timperio.crm.service.AdminUserService;

public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) throws UserAlreadyExistsException {
        User user = UserMapper.mapToUser(userDto);
        try {
            User existingUser = userRepository.findByUsername(user.getUsername());
            if (existingUser != null) {
                throw new UserAlreadyExistsException("User already exists");
            }
            User savedUser = userRepository.save(user);
            return UserMapper.mapToUserDto(savedUser);
        } catch (UserAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return the original input without userid if failed
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        try {
            User existingUser = userRepository.findByUserId(user.getUserId());
            if (existingUser == null) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        User updatedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDto deleteUser(int userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            return null;
        }
        userRepository.delete(user);
        return UserMapper.mapToUserDto(user);
    }
}