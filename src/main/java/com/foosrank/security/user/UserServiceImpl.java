package com.foosrank.security.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foosrank.security.role.Role;
import com.foosrank.security.role.RoleName;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Transactional
	@Override
	public void createUser(final UserDto userDto, final RoleName roleName) {
		final User user = mapUserDtoToUser(userDto);
		final Role role = new Role();
		role.setRolename(roleName);
		role.setUser(user);
		user.getRoles().add(role);
		user.setEnabled(true);

		userRepository.save(user);
	}

	private User mapUserDtoToUser(UserDto userDto) {
		User user = new User();
		user.setCreationTime(userDto.getCreationTime());
		user.setEnabled(userDto.isEnabled());
		user.setId(userDto.getId());
		user.setModificationTime(userDto.getModificationTime());
		user.setPassword(userDto.getPassword());
		user.setRoles(userDto.getRoles());
		user.setUsername(userDto.getUsername());
		return user;
	}

	private UserDto mapUserToUserDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setCreationTime(user.getCreationTime());
		userDto.setEnabled(user.isEnabled());
		userDto.setId(user.getId());
		userDto.setModificationTime(user.getModificationTime());
		userDto.setPassword(user.getPassword());
		userDto.setRoles(user.getRoles());
		userDto.setUsername(user.getUsername());
		return userDto;
	}

	@Transactional(readOnly = true)
	@Override
	public List<UserDto> getUsers() {
		final List<User> users = userRepository.findAll(new Sort("id"));
		final List<UserDto> usersDto = new ArrayList<UserDto>();
		users.forEach(user -> usersDto.add(mapUserToUserDto(user)));

		return usersDto;
	}

	@Transactional(readOnly = true)
	@Override
	public UserDto getUser(final Integer id) {
		return mapUserToUserDto(find(id));
	}

	@Transactional
	@Override
	public void updateUser(final UserDto user) {
		// TODO Auto-generated method stub

	}

	@Transactional
	@Override
	public void deleteUser(final Integer id) {
		userRepository.delete(id);
	}

	@Transactional(readOnly = true)
	private User find(final Integer id) {
		final User userOpt = userRepository.findOne(id);
		if (null == userOpt) {
			throw new UserNotFoundException(id);
		}
		return userOpt;
	}

}
