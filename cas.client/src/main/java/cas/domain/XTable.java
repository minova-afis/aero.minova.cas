package cas.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class XTable implements Serializable {
	private static final long serialVersionUID = 202111011356L;

	private String id;
	private Table table;
}
