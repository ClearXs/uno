package cc.allio.uno.data.query.excel;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class CellIndex implements Serializable {

	/**
	 * 指标值
	 */
	private String key;

	/**
	 * 指标名称
	 */
	private String name;
}
