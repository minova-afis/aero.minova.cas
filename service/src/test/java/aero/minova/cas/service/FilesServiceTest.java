package aero.minova.cas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.BaseTest;
import aero.minova.cas.controller.SqlViewController;

//benötigt, damit JUnit-Tests nicht abbrechen
@SpringBootTest(properties = { "application.runner.enabled=false" })
public class FilesServiceTest extends BaseTest {

	@Autowired
	FilesService testSubject;

	@Mock
	SqlViewController mockController;

	@DisplayName("MDI Test mit Masken und Actions")
	@WithMockUser(username = "user", roles = {})
	@Test
	public void testMdi() throws Exception {

		Table mockResult = new Table();
		mockResult.addColumn(new Column("ID", DataType.STRING));
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

		byte[] awatingResult = new byte[] { 60, 63, 120, 109, 108, 32, 118, 101, 114, 115, 105, 111, 110, 61, 34, 49, 46, 48, 34, 32, 101, 110, 99, 111, 100,
											105, 110, 103, 61, 34, 85, 84, 70, 45, 56, 34, 32, 115, 116, 97, 110, 100, 97, 108, 111, 110, 101, 61, 34, 121, 101,
											115, 34, 63, 62, 10, 60, 109, 97, 105, 110, 32, 105, 99, 111, 110, 61, 34, 83, 73, 83, 34, 32, 116, 105, 116, 108,
											101, 61, 34, 64, 99, 117, 115, 115, 111, 109, 57, 55, 50, 56, 54, 46, 84, 105, 116, 108, 101, 34, 62, 10, 32, 32,
											32, 32, 60, 97, 99, 116, 105, 111, 110, 32, 105, 100, 61, 34, 99, 117, 115, 115, 111, 109, 57, 55, 50, 56, 54, 86,
											111, 117, 99, 104, 101, 114, 34, 32, 116, 101, 120, 116, 61, 34, 64, 120, 116, 99, 117, 115, 115, 111, 109, 57, 55,
											50, 56, 54, 86, 111, 117, 99, 104, 101, 114, 34, 32, 97, 99, 116, 105, 111, 110, 61, 34, 99, 117, 115, 115, 111,
											109, 57, 55, 50, 56, 54, 86, 111, 117, 99, 104, 101, 114, 46, 120, 109, 108, 34, 32, 105, 99, 111, 110, 61, 34, 86,
											111, 117, 99, 104, 101, 114, 34, 47, 62, 10, 32, 32, 32, 32, 60, 97, 99, 116, 105, 111, 110, 32, 105, 100, 61, 34,
											99, 117, 115, 115, 111, 109, 57, 55, 50, 56, 54, 84, 114, 97, 102, 102, 105, 99, 77, 101, 97, 115, 117, 114, 101,
											109, 101, 110, 116, 34, 32, 116, 101, 120, 116, 61, 34, 64, 120, 116, 99, 117, 115, 115, 111, 109, 57, 55, 50, 56,
											54, 84, 114, 97, 102, 102, 105, 99, 77, 101, 97, 115, 117, 114, 101, 109, 101, 110, 116, 34, 32, 97, 99, 116, 105,
											111, 110, 61, 34, 99, 117, 115, 115, 111, 109, 57, 55, 50, 56, 54, 84, 114, 97, 102, 102, 105, 99, 77, 101, 97, 115,
											117, 114, 101, 109, 101, 110, 116, 46, 120, 109, 108, 34, 32, 105, 99, 111, 110, 61, 34, 84, 114, 97, 102, 102, 105,
											99, 77, 101, 97, 115, 117, 114, 101, 109, 101, 110, 116, 34, 47, 62, 10, 32, 32, 32, 32, 60, 109, 101, 110, 117, 32,
											105, 100, 61, 34, 109, 97, 105, 110, 34, 62, 10, 32, 32, 32, 32, 32, 32, 32, 32, 60, 109, 101, 110, 117, 32, 105,
											100, 61, 34, 99, 117, 115, 115, 111, 109, 57, 55, 50, 56, 54, 84, 97, 98, 108, 101, 115, 34, 32, 116, 101, 120, 116,
											61, 34, 64, 99, 117, 115, 115, 111, 109, 57, 55, 50, 56, 54, 46, 109, 100, 105, 46, 84, 97, 98, 108, 101, 115, 34,
											62, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 60, 101, 110, 116, 114, 121, 32, 105, 100, 61, 34, 99, 117,
											115, 115, 111, 109, 57, 55, 50, 56, 54, 86, 111, 117, 99, 104, 101, 114, 34, 32, 116, 121, 112, 101, 61, 34, 97, 99,
											116, 105, 111, 110, 34, 47, 62, 10, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 60, 101, 110, 116, 114, 121, 32,
											105, 100, 61, 34, 99, 117, 115, 115, 111, 109, 57, 55, 50, 56, 54, 84, 114, 97, 102, 102, 105, 99, 77, 101, 97, 115,
											117, 114, 101, 109, 101, 110, 116, 34, 32, 116, 121, 112, 101, 61, 34, 97, 99, 116, 105, 111, 110, 34, 47, 62, 10,
											32, 32, 32, 32, 32, 32, 32, 32, 60, 47, 109, 101, 110, 117, 62, 10, 32, 32, 32, 32, 60, 47, 109, 101, 110, 117, 62,
											10, 60, 47, 109, 97, 105, 110, 62, 10 };

		assertThat(testSubject.readMDI())//
				.isEqualTo(awatingResult);
	}

}
