package com.service.business.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.database.bean.User;
import com.database.dao.SotivityDao;
import com.database.dao.UserDao;
import com.security.util.SecurityUtil;
import com.server.util.ConfigurationHelper;
import com.service.business.UserService;

@Named
public class UserServiceImpl implements Serializable, UserService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private UserDao userDao;
	@Inject
	private SotivityDao sotivityDao;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public boolean createUser(String firstname, String lastname,
			String nickname, String email, String password) {
		try {
			if (firstname == null || firstname.isEmpty() || lastname == null
					|| lastname.isEmpty() || nickname == null
					|| nickname.isEmpty() || email == null || email.isEmpty()
					|| password == null || password.isEmpty())
				return false;
			logger.trace("Entered Create User");
			User user = new User();
			byte[] bSalt = SecurityUtil.createSalt();
			String salt = SecurityUtil.hashToString(bSalt);
			byte[] bDigest = SecurityUtil.getHash(password, bSalt);
			String digest = SecurityUtil.hashToString(bDigest);
			user.setFirstname(firstname);
			user.setLastname(lastname);
			user.setEmail(email);
			user.setNickname(nickname);
			user.setPassword(digest);
			user.setSalt(salt);
			userDao.create(user);
			return true;
		} catch (UnsupportedEncodingException ex) {
			logger.error("Encoding is not supportet: " + ex.getMessage());
			return false;
		} catch (NoSuchAlgorithmException ex) {
			logger.error("Algorithm is not supportet: " + ex.getMessage());
			return false;
		}
		// TODO CATCH USER/EMAIL ALREADY EXISTS
	}

	public void updateUser(User user) {
		sotivityDao.update(user);
	}

	public User login(String nickname, String password) {
		User user = userDao.getUserByNickname(nickname);
		String digest, salt;
		boolean userExists;
		// first if for prevention of side channel attack
		if (user == null) {
			digest = "000000000000000000000000000=";
			salt = "00000000000=";
			userExists = false;
		} else {
			digest = user.getPassword();
			salt = user.getSalt();
			userExists = true;
		}

		try {
			return (SecurityUtil.authenticate(digest, password, salt) && userExists) ? user
					: null;
		} catch (UnsupportedEncodingException ex) {
			logger.error("Encodeing is not supportet: " + ex.getMessage());
			return null;
		} catch (NoSuchAlgorithmException ex) {
			logger.error("Algorithm is not supportet: " + ex.getMessage());
			return null;
		}
	}

	public boolean isNicknameUsed(String nickname) {
		if (userDao.getUserByNickname(nickname) != null)
			return true;
		else
			return false;

	}

	@Override
	public boolean changeProfilePicture(User user, UploadedFile file) {
		if (user == null || file == null)
			return false;

		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			inputStream = file.getInputstream();
			File newFile = new File(
					ConfigurationHelper.getValue("files.static.path"),
					file.getFileName());
			outputStream = new FileOutputStream(newFile);

			IOUtils.copy(inputStream, outputStream);
			logger.info(newFile.getAbsolutePath());
			//sotivity.setPicture(newFile.getName());

			user.setPicture(newFile.getName());
			this.updateUser(user);
			
		} catch (IOException e) {
			logger.error("Couldn't save soti image", e);
		} finally {
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error("Error closing OutputStream", e);
				}
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("Error closing InputStream", e);
				}
		}
		
		//OutputStream out = new FileOutputStream(outFile);
		
		return true;
	}
	
	public List<User> getAllUsers(){
		return userDao.getAll();
	}
	
	public boolean deleteUser(User user){
		userDao.delete(user);
		return true;
	}

}
