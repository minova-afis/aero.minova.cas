package aero.minova.cas.app.extension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

	String insertTable = "{\n" + "            \"name\": \"xpcasInsertUserGroup\",\n" + "            \"columns\": [\n" + "                {\n"
			+ "                    \"name\": \"KeyLong\",\n" + "                    \"type\": \"INTEGER\",\n"
			+ "                    \"outputType\": \"OUTPUT\"\n" + "                },\n" + "                {\n"
			+ "                    \"name\": \"KeyText\",\n" + "                    \"type\": \"STRING\"\n" + "                },\n" + "                {\n"
			+ "                    \"name\": \"Description\",\n" + "                    \"type\": \"STRING\"\n" + "                },\n" + "                {\n"
			+ "                    \"name\": \"UserCode\",\n" + "                    \"type\": \"STRING\"\n" + "                },\n" + "                {\n"
			+ "                    \"name\": \"SecurityToken\",\n" + "                    \"type\": \"STRING\"\n" + "                }\n" + "            ],\n"
			+ "            \"rows\": [\n" + "                {\n" + "                    \"values\": [\n" + "                        null,\n"
			+ "                        \"s-Group\",\n" + "                        null,\n" + "                        null,\n"
			+ "                        \"s-Token\"\n" + "                    ]\n" + "                }\n" + "            ]\n" + "        }";

	String updateTable = "{\"name\":\"xpcasUpdateUserGroup\",\"columns\":[{\"name\":\"KeyLong\",\"type\":\"INTEGER\"},{\"name\":\"KeyText\",\"type\":\"STRING\"},{\"name\":\"Description\",\"type\":\"STRING\"},{\"name\":\"UserCode\",\"type\":\"STRING\"},{\"name\":\"SecurityToken\",\"type\":\"STRING\"}],\"rows\":[{\"values\":[\"n-2\",\"s-Group\",\"s-Des\",null,\"s-Token\"]}]}";

	String readTable = "{\"name\":\"xpcasReadUserGroup\",\"columns\":[{\"name\":\"KeyLong\",\"type\":\"INTEGER\",\"outputType\":\"OUTPUT\"},{\"name\":\"KeyText\",\"type\":\"STRING\",\"outputType\":\"OUTPUT\"},{\"name\":\"Description\",\"type\":\"STRING\",\"outputType\":\"OUTPUT\"},{\"name\":\"UserCode\",\"type\":\"STRING\",\"outputType\":\"OUTPUT\"},{\"name\":\"SecurityToken\",\"type\":\"STRING\",\"outputType\":\"OUTPUT\"}],\"rows\":[{\"values\":[\"n-2\",null,null,null,null]}]}";

	String deleteTable = "{\"name\":\"xpcasDeleteUserGroup\",\"columns\":[{\"name\":\"KeyLong\",\"type\":\"INTEGER\"}],\"rows\":[{\"values\":[\"n-2\"]}]}";

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
		Table insert = gson.fromJson(insertTable, Table.class);
		Table resInsert = userGroupExtension.insert(insert).getBody().getOutputParameters();
		Value keyLong = resInsert.getValue("KeyLong", 0);
		assertEquals(1, userGroupRepository.findByKeyText("Group").size());
		assertNotNull(keyLong);

		Table update = gson.fromJson(updateTable, Table.class);
		update.setValue(keyLong, "KeyLong", 0);
		userGroupExtension.update(update);
		assertEquals(1, userGroupRepository.findByKeyText("Group").size());
		assertEquals("Des", userGroupRepository.findByKeyText("Group").get(0).getDescription());

		Table read = gson.fromJson(readTable, Table.class);
		read.setValue(keyLong, "KeyLong", 0);
		Table resRead = userGroupExtension.read(read).getBody().getOutputParameters();
		assertEquals("Des", resRead.getValue("Description", 0).getStringValue());
		assertEquals("Token", resRead.getValue("SecurityToken", 0).getStringValue());

		Table delete = gson.fromJson(deleteTable, Table.class);
		delete.setValue(keyLong, "KeyLong", 0);
		userGroupExtension.delete(delete);
		assertEquals(0, userGroupRepository.findByKeyText("Group").size());
	}

}
