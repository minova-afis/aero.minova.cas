package aero.minova.cas.api.domain;

import lombok.Data;

@Data
public class TableMetaData {
	private Integer limited;
	private Integer page;
	private Integer totalResults;
	private Integer totalPages;
	private Integer resultsLeft;
}
