package aero.minova.cas.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import aero.minova.cas.service.model.UserGroup;

@Service
public class UserGroupService extends BaseService<UserGroup> {

	/**
	 * Gibt eine Liste aller (nicht leeren) Tokens der übergebenen Gruppe zurück
	 * 
	 * @param userGroup
	 * @return
	 */
	public static List<String> getSecurityTokensOfGroup(UserGroup userGroup) {
		List<String> tokens = Arrays.asList(userGroup.getSecurityToken().split("#"));
		return tokens.stream().filter(s -> s != null && !s.isBlank()).toList();
	}

}
