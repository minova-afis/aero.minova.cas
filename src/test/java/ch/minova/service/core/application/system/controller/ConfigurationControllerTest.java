package ch.minova.service.core.application.system.controller;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.write;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.val;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ConfigurationControllerTest {

	@Autowired
	FilesService files;

	@Autowired
	FilesController testSubject;

	@Test
	public void test() throws Exception {
		val afisFolder = files.applicationFolder("AFIS");
		createDirectory(afisFolder);
		write(afisFolder.resolve("AFIS.xbs"), new String("<preferences></preferences>").getBytes(StandardCharsets.UTF_8));
		assertThat(testSubject.getFile("Shared Data/Program Files/AFIS/AFIS.xbs")).isEqualTo("<preferences></preferences>");
	}

}
