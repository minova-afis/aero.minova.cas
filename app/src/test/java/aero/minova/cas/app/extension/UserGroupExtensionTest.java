package aero.minova.cas.app.extension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.google.gson.Gson;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.api.domain.ValueDeserializer;
import aero.minova.cas.api.domain.ValueSerializer;
import aero.minova.cas.service.repository.UserGroupRepository;

@SpringBootTest(classes = CoreApplicationSystemApplication.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class UserGroupExtensionTest {

	Gson gson = new Gson().newBuilder()//
			.registerTypeAdapter(Value.class, new ValueSerializer()) //
			.registerTypeAdapter(Value.class, new ValueDeserializer()) //
			.create();

	@Autowired
	UserGroupExtension userGroupExtension;

	@Autowired
	UserGroupRepository userGroupRepository;

	@Test
	void testFunctions() throws Exception {
		Table insert = readTableFromExampleJson("xpcasInsertUserGroup");
		Table resInsert = userGroupExtension.insert(insert).getBody().getOutputParameters();
		Value keyLong = resInsert.getValue("KeyLong", 0);
		assertEquals(1, userGroupRepository.findByKeyText("Group").size());
		assertNotNull(keyLong);

		Table update = readTableFromExampleJson("xpcasUpdateUserGroup");
		update.setValue(keyLong, "KeyLong", 0);
		userGroupExtension.update(update);
		assertEquals(1, userGroupRepository.findByKeyText("Group").size());
		assertEquals("Des", userGroupRepository.findByKeyText("Group").get(0).getDescription());

		Table read = readTableFromExampleJson("xpcasReadUserGroup");
		read.setValue(keyLong, "KeyLong", 0);
		Table resRead = userGroupExtension.read(read).getBody().getOutputParameters();
		assertEquals("Des", resRead.getValue("Description", 0).getStringValue());
		assertEquals("Token", resRead.getValue("SecurityToken", 0).getStringValue());

		Table delete = readTableFromExampleJson("xpcasDeleteUserGroup");
		delete.setValue(keyLong, "KeyLong", 0);
		userGroupExtension.delete(delete);
		assertEquals(0, userGroupRepository.findByKeyText("Group").size());
	}

	@SuppressWarnings("resource")
	public Table readTableFromExampleJson(String fileName) {

		return gson.fromJson(new Scanner(getClass()//
				.getClassLoader()//
				.getResourceAsStream(fileName + ".json"), "UTF-8")//
						.useDelimiter("\\A")//
						.next(),
				Table.class);
	}

}
