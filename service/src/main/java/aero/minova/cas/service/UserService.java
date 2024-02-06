package aero.minova.cas.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

		String newToken = userGroupService.findEntityById(userGroupKey).getKeyText();

		List<String> memberships = getMemberships(user);

		if (!memberships.contains(newToken)) {
			memberships.add(newToken);
			user.setMemberships(getMembershipString(memberships));
			save(user);
		}
	}

	public void removeUserFromUserGroup(int userKey, int userGroupKey) {
		User user = findEntityById(userKey);
		String tokenToRemove = userGroupService.findEntityById(userGroupKey).getKeyText();

		List<String> memberships = Arrays.asList(user.getMemberships().split("#")).stream().filter(token -> !token.equalsIgnoreCase(tokenToRemove)).toList();
		user.setMemberships(getMembershipString(memberships));
		save(user);
	}

	private List<String> getMemberships(User user) {
		if (user.getMemberships() == null || user.getMemberships().isBlank()) {
			return new ArrayList<>();
		}

		return Arrays.asList(user.getMemberships().split("#")).stream().map(String::strip).filter(s -> !s.isBlank())
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private String getMembershipString(List<String> memberships) {
		StringBuilder res = new StringBuilder();

		for (String s : memberships) {
			if (s != null && !s.strip().isBlank()) {
				res.append("#" + s.strip());
			}
		}

		return res.toString();
	}

}
