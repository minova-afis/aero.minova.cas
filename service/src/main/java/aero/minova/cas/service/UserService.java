package aero.minova.cas.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aero.minova.cas.service.model.User;

@Service
public class UserService extends BaseService<User> {

	@Autowired
	UserGroupService userGroupService;

	public List<User> findUsersInUserGroup(int userGroupKey) {

		String userGroupToken = userGroupService.findEntityById(userGroupKey).getKeyText();

		List<User> res = new ArrayList<>();

		for (User u : repository.findAllWithLastActionGreaterZero()) {
			if (u.getMemberships() != null && u.getMemberships().toLowerCase().contains(userGroupToken.toLowerCase())) {
				res.add(u);
			}
		}

		return res;
	}

	public void addUserToUserGroup(int userKey, int userGroupKey) {

		User user = findEntityById(userKey);

		String userGroupToken = "#" + userGroupService.findEntityById(userGroupKey).getKeyText();
		if (user.getMemberships() == null || !user.getMemberships().toLowerCase().contains(userGroupToken.toLowerCase())) {
			user.setMemberships(user.getMemberships() != null ? user.getMemberships() : "" + userGroupToken);
			save(user);
		}
	}

//TODO:Testen	
	public void removeUserFromUserGroup(int userKey, int userGroupKey) {
		User user = findEntityById(userKey);
		String userGroupToken = userGroupService.findEntityById(userGroupKey).getKeyText();

		List<String> memberships = Arrays.asList(user.getMemberships().split("#"));
		memberships.remove(userGroupToken);

		user.setMemberships("");
		for (String s : memberships) {
			user.setMemberships(user.getMemberships() + "#" + s);
		}

		save(user);
	}

}
