package com.service.business;

import java.util.List;

import org.primefaces.model.UploadedFile;

import com.database.bean.User;

public interface UserService {

	public abstract boolean createUser(String firstname, String lastname,
			String nickname, String email, String password);

	public abstract void updateUser(User user);

	public abstract User login(String nickname, String password);

	public abstract boolean changeProfilePicture(User user, UploadedFile file);
	
	public abstract List<User> getAllUsers();
	
	public abstract boolean deleteUser(User user);
	
	public abstract boolean isNicknameUsed(String nickname);

}