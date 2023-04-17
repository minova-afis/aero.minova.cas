package aero.minova.cas.controller;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.restapi.ClientRestAPI;

public abstract class BaseTest {

	Gson gson;

	@Autowired
	ClientRestAPI clientRestAPI;

	@SuppressWarnings("resource")
	public Table readTableFromExampleJson(String fileName) {

		gson = clientRestAPI.getGson();
		return gson.fromJson(new Scanner(getClass()//
				.getClassLoader()//
				.getResourceAsStream(fileName + ".json"), "UTF-8")//
						.useDelimiter("\\A")//
						.next(),
				Table.class);
	}

}
