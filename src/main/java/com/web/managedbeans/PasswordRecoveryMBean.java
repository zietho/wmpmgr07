package com.web.managedbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class PasswordRecoveryMBean extends AbstractManagedBean {
	private String nickname;

	private boolean successMessage = false;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void submit() {
		logger.debug("Submit Password Recovery");
		this.successMessage = true;
	}

	public boolean isSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(boolean successMessage) {
		this.successMessage = successMessage;
	}
}
