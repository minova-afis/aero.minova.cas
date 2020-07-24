package aero.minova.core.application.system.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SqlControllerTest {
	
	@Autowired
	SqlController testSubject;

	@Test
	void testIndexViewResultName() {
		//assertThat(testSubject.getIndexView("IndexViewName").getName()).isEqualTo("IndexViewName");
	}

}
