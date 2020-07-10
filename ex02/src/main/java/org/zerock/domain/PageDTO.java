package org.zerock.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageDTO {

	private int startPage;
	private int endPage;
	private boolean prev, next;
	
	private int total;
	private Criteira cri;
	
	public PageDTO(Criteira cri, int total) {
		this.cri = cri;
		this.total = total;
		
		// 페이징의 끝 번호 계산
		this.endPage = (int)(Math.ceil(cri.getPageNum()/10.0)) * 10;
		
		// 페이징의 시작 번호 계산( 화면에 10개씩 보여준다고 가정 했을 시 )
		this.startPage = this.endPage - 9;
		
		
		// 전체 데이터 수를 이용해 진짜 끝 페이지가 몇 번까지 되는지를 계산
		int realEnd =(int)(Math.ceil((total*1.0) / cri.getAmount()));
		
		// 만인 진짜 끝 페이지(realEnd)가 구해둔 끝 번호(endPage)보다 작다면 끝번호는 작은 값이 되어야한다.
		if(realEnd < this.endPage) {
			this.endPage = realEnd;
		}
		
		// 이전의 경우는 시작번호가 1보다 큰 경우라면 존재한다.
		this.prev = this.startPage > 1;
		
		
		// 다음의 경우 realEnd가 끝 번호보다 큰 경우에만 존재한다
		this.next = this.endPage < realEnd;
	}
}
