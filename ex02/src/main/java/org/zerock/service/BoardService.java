package org.zerock.service;

import java.util.List;

import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteira;


public interface BoardService {

	public void register(BoardVO board); 
	
	public BoardVO get(Long bno);
	
	public boolean modify(BoardVO board);
	
	public boolean remove(Long bno);
	
//	public List<BoardVO> getList();
	
	public List<BoardVO> getList(Criteira cri);
	
	public List<BoardAttachVO> getAttachList( Long bno );
	
	public int getTotal(Criteira cri);
}
