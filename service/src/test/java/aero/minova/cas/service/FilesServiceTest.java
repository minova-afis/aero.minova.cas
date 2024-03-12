package aero.minova.cas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import aero.minova.cas.BaseTest;
import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.SqlViewController;

//benötigt, damit JUnit-Tests nicht abbrechen
@SpringBootTest(classes = CoreApplicationSystemApplication.class, properties = { "application.runner.enabled=false" })
class FilesServiceTest extends BaseTest {

	@Autowired
	FilesService testSubject;

	@Mock
	SqlViewController mockController;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@DisplayName("MDI Test mit Masken und Actions")
	@WithMockUser(username = "user", roles = {})
	@Test
	void testMdi() throws Exception {

		Table mockResult = new Table();
		mockResult.addColumn(new Column("KeyText", DataType.STRING));
		mockResult.addColumn(new Column("Icon", DataType.STRING));
		mockResult.addColumn(new Column("Label", DataType.STRING));
		mockResult.addColumn(new Column("Menu", DataType.STRING));
		mockResult.addColumn(new Column("Position", DataType.DOUBLE));
		mockResult.addColumn(new Column("SecurityToken", DataType.STRING));
		mockResult.addColumn(new Column("MdiTypeKey", DataType.INTEGER));
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("cussom97286TrafficMeasurement", null));
			inputRow.addValue(new Value("TrafficMeasurement", null));
			inputRow.addValue(new Value("@xtcussom97286TrafficMeasurement", null));
			inputRow.addValue(new Value("cussom97286Tables", null));
			inputRow.addValue(new Value(2.0, null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(1, null));
			mockResult.addRow(inputRow);
		}
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("cussom97286Voucher", null));
			inputRow.addValue(new Value("Voucher", null));
			inputRow.addValue(new Value("@xtcussom97286Voucher", null));
			inputRow.addValue(new Value("cussom97286Tables", null));
			inputRow.addValue(new Value(1.0, null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(1, null));
			mockResult.addRow(inputRow);
		}
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("cussom97286Tables", null));
			inputRow.addValue(null);
			inputRow.addValue(new Value("@cussom97286.mdi.Tables", null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(1.0, null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(2, null));
			mockResult.addRow(inputRow);
		}
		{
			Row inputRow = new Row();
			inputRow.addValue(null);
			inputRow.addValue(new Value("SIS", null));
			inputRow.addValue(new Value("@cussom97286.Title", null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(1.0, null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(3, null));
			mockResult.addRow(inputRow);
		}

		doReturn(mockResult).when(mockController).getIndexView(Mockito.any());

		testSubject.setViewController(mockController);

		File testFile = new File(getClass().getClassLoader().getResource("xmls/MdiTest.xml").getFile());

		FileInputStream fl = new FileInputStream(testFile);
		byte[] awatingResult = new byte[(int) testFile.length()];

		fl.read(awatingResult);
		fl.close();

		assertThat(new String(testSubject.readMDI())).isEqualToNormalizingNewlines(new String(awatingResult));
	}

	@DisplayName("MDI Test ohne Menu Eintrag in Action")
	@WithMockUser(username = "user", roles = {})
	@Test
	void testMdiWithoutMenuEntryInAction() throws Exception {

		Table mockResult = new Table();
		mockResult.addColumn(new Column("KeyText", DataType.STRING));
		mockResult.addColumn(new Column("Icon", DataType.STRING));
		mockResult.addColumn(new Column("Label", DataType.STRING));
		mockResult.addColumn(new Column("Menu", DataType.STRING));
		mockResult.addColumn(new Column("Position", DataType.DOUBLE));
		mockResult.addColumn(new Column("SecurityToken", DataType.STRING));
		mockResult.addColumn(new Column("MdiTypeKey", DataType.INTEGER));
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("cussom97286TrafficMeasurement", null));
			inputRow.addValue(new Value("TrafficMeasurement", null));
			inputRow.addValue(new Value("@xtcussom97286TrafficMeasurement", null));
			inputRow.addValue(new Value("cussom97286Tables", null));
			inputRow.addValue(new Value(2.0, null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(1, null));
			mockResult.addRow(inputRow);
		}
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("cussom97286Voucher", null));
			inputRow.addValue(new Value("Voucher", null));
			inputRow.addValue(new Value("@xtcussom97286Voucher", null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(1.0, null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(1, null));
			mockResult.addRow(inputRow);
		}
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("cussom97286Tables", null));
			inputRow.addValue(null);
			inputRow.addValue(new Value("@cussom97286.mdi.Tables", null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(1.0, null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(2, null));
			mockResult.addRow(inputRow);
		}
		{
			Row inputRow = new Row();
			inputRow.addValue(null);
			inputRow.addValue(new Value("SIS", null));
			inputRow.addValue(new Value("@cussom97286.Title", null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(1.0, null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(3, null));
			mockResult.addRow(inputRow);
		}

		doReturn(mockResult).when(mockController).getIndexView(Mockito.any());

		testSubject.setViewController(mockController);

		File testFile = new File(getClass().getClassLoader().getResource("xmls/MdiTestActionWithoutMenu.xml").getFile());

		FileInputStream fl = new FileInputStream(testFile);
		byte[] awatingResult = new byte[(int) testFile.length()];

		fl.read(awatingResult);
		fl.close();

		assertThat(new String(testSubject.readMDI())).isEqualToNormalizingNewlines(new String(awatingResult));
	}

	@DisplayName("MDI Test ohne Hauptmenu")
	@WithMockUser(username = "user", roles = {})
	@Test
	void testMdiWithoutMainMenu() throws Exception {

		Table mockResult = new Table();
		mockResult.addColumn(new Column("KeyText", DataType.STRING));
		mockResult.addColumn(new Column("Icon", DataType.STRING));
		mockResult.addColumn(new Column("Label", DataType.STRING));
		mockResult.addColumn(new Column("Menu", DataType.STRING));
		mockResult.addColumn(new Column("Position", DataType.DOUBLE));
		mockResult.addColumn(new Column("SecurityToken", DataType.STRING));
		mockResult.addColumn(new Column("MdiTypeKey", DataType.INTEGER));
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("cussom97286TrafficMeasurement", null));
			inputRow.addValue(new Value("TrafficMeasurement", null));
			inputRow.addValue(new Value("@xtcussom97286TrafficMeasurement", null));
			inputRow.addValue(new Value("cussom97286Tables", null));
			inputRow.addValue(new Value(2.0, null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(1, null));
			mockResult.addRow(inputRow);
		}
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("cussom97286Voucher", null));
			inputRow.addValue(new Value("Voucher", null));
			inputRow.addValue(new Value("@xtcussom97286Voucher", null));
			inputRow.addValue(new Value("cussom97286Tables", null));
			inputRow.addValue(new Value(1.0, null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(1, null));
			mockResult.addRow(inputRow);
		}

		doReturn(mockResult).when(mockController).getIndexView(Mockito.any());

		testSubject.setViewController(mockController);

		Throwable exception = assertThrows(RuntimeException.class, () -> testSubject.readMDI());
		assertThat(exception).hasMessage("No menu defined. Mdi cannot be build!");
	}
}
