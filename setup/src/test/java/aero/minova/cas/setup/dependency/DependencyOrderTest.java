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
						"aero.minova.common",
						"aero.minova.cas.app",
						"aero.minova.data.schema.app",
						"aero.minova.afis.dispatch",
						"aero.minova.afis.flightschedulelog.app",
						"aero.minova.afis.productivity",
						"aero.minova.aircrafttype",
						"aero.minova.automatic",
						"aero.minova.blockeddata",
						"aero.minova.contact",
						"aero.minova.creditcardcompany",
						"aero.minova.data.job.jobexecutor",
						"aero.minova.delaycode",
						"aero.minova.flightfieldposition",
						"aero.minova.flightschedule",
						"aero.minova.install",
						"aero.minova.internalmerge",
						"aero.minova.item",
						"aero.minova.loadingpermission",
						"aero.minova.logbook",
						"aero.minova.order",
						"aero.minova.shipment.driverefficiency",
						"aero.minova.shorttank",
						"aero.minova.site.app",
						"aero.minova.taxcode",
						"aero.minova.truckconfiguration",
						"aero.minova.vat",
						"aero.minova.vehicle",
						"aero.minova.aircraft",
						"aero.minova.airport",
						"aero.minova.driver",
						"aero.minova.entry.partunloading",
						"aero.minova.jobdestination",
						"aero.minova.orderreceiver",
						"aero.minova.shippingagency",
						"aero.minova.tank",
						"aero.minova.taxresponsable",
						"aero.minova.truck",
						"aero.minova.vat.site",
						"aero.minova.entry",
						"aero.minova.idcard",
						"aero.minova.jobresult.app",
						"aero.minova.orderpartner",
						"aero.minova.truck.adr",
						"aero.minova.truck.disposition",
						"aero.minova.truck.truckmeterreading",
						"aero.minova.truckpool",
						"aero.minova.vehicle.stockadjustment",
						"aero.minova.virtualtruck",
						"aero.minova.service.jobexecutor.app",
						"aero.minova.supplier",
						"aero.minova.truck.truckmeter",
						"aero.minova.truck.truckvehiclecompartment",
						"aero.minova.customer",
						"aero.minova.job.closing",
						"aero.minova.job.closingprocedureexecutor.app",
						"aero.minova.stock",
						"aero.minova.data.closing",
						"aero.minova.loading",
						"aero.minova.lucustomerflightnumber",
						"aero.minova.lusuppliercustomer",
						"aero.minova.tim",
						"aero.minova.stockexit",
						"aero.minova.afis.shipment",
						"com.minova.afis"
				);
	}

	private String retrieveContentFromResource(String filename) throws IOException {
		return IOUtils.resourceToString(filename, StandardCharsets.UTF_8, getClass().getClassLoader());
	}
}
