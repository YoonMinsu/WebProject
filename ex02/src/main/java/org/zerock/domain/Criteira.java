package org.zerock.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteira {
	private int pageNum;
	private int amount;
	
	public Criteira() {
		this(1,10);
	}
	
	public Criteira(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
}
