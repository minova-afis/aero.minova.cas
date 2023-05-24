package aero.minova.cas.setup.dependency;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static aero.minova.cas.setup.dependency.DependencyOrder.determineDependencyOrder;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class DependencyOrderTest {
	@Test
	void determineDependencyOrderWithSimpleExampleTest() throws Exception {
		final var result = determineDependencyOrder(retrieveContentFromResource("dependency-graph-oiltanking.swb.json"));
		assertThat(result)
				.containsExactly(
						"aero.minova.app.i18n", //
						"aero.minova.cas.app", //
						"aero.minova.data.schema.app", //
						"aero.minova.contact", //
						"aero.minova.invoice"
				);
	}

	@Test
	void determineDependencyOrderWithComplexExampleTest() throws Exception {
		final var result = determineDependencyOrder(retrieveContentFromResource("dependency-graph-skyber.awb.app.json"));
		assertThat(result)
				.containsExactly(//
				"aero.minova.app.i18n",
				"aero.minova.cas.app",
				"aero.minova.data.schema.app",
				"aero.minova.loadingpermission",
				"aero.minova.vehicle",
				"aero.minova.logbook",
				"aero.minova.afis.productivity",
				"aero.minova.internalmerge",
				"aero.minova.afis.dispatch",
				"aero.minova.data.job.jobexecutor",
				"aero.minova.site.app",
				"aero.minova.install",
				"aero.minova.tank",
				"aero.minova.contact",
				"aero.minova.driver",
				"aero.minova.taxresponsable",
				"aero.minova.truck",
				"aero.minova.vehicle.stockadjustment",
				"aero.minova.truck.truckmeterreading",
				"aero.minova.truckpool",
				"aero.minova.truck.adr",
				"aero.minova.truck.disposition",
				"aero.minova.shorttank",
				"aero.minova.creditcardcompany",
				"aero.minova.order",
				"aero.minova.truckconfiguration",
				"aero.minova.aircrafttype",
				"aero.minova.delaycode",
				"aero.minova.jobdestination",
				"aero.minova.jobresult.app",
				"aero.minova.service.jobexecutor.app",
				"aero.minova.job.closingprocedureexecutor.app",
				"aero.minova.job.closing",
				"aero.minova.data.closing",
				"aero.minova.tim",
				"aero.minova.orderreceiver",
				"aero.minova.orderpartner",
				"aero.minova.truck.truckmeter",
				"aero.minova.automatic",
				"aero.minova.supplier",
				"aero.minova.vat",
				"aero.minova.vat.site",
				"aero.minova.taxcode",
				"aero.minova.virtualtruck",
				"aero.minova.truck.truckvehiclecompartment",
				"aero.minova.airport",
				"aero.minova.entry.partunloading",
				"aero.minova.flightfieldposition",
				"aero.minova.customer",
				"aero.minova.lusuppliercustomer",
				"aero.minova.lucustomerflightnumber",
				"aero.minova.afis.flightschedulelog.app",
				"aero.minova.shipment.driverefficiency",
				"aero.minova.blockeddata",
				"aero.minova.shippingagency",
				"aero.minova.idcard",
				"aero.minova.flightschedule",
				"aero.minova.aircraft",
				"aero.minova.common",
				"aero.minova.item",
				"aero.minova.stock",
				"aero.minova.loading",
				"aero.minova.entry",
				"aero.minova.stockexit",
				"aero.minova.afis.shipment",
				"com.minova.afis"
				);
	}

	private String retrieveContentFromResource(String filename) throws IOException {
		return IOUtils.resourceToString(filename, StandardCharsets.UTF_8, getClass().getClassLoader());
	}
}
