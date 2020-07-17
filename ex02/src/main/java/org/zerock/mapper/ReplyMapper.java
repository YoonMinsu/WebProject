package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.Criteira;
import org.zerock.domain.ReplyVO;

public interface ReplyMapper {

	public ReplyVO read(Long bno);
	public int insert(ReplyVO vo);
	public int delete(Long targetRno);
	public int update(ReplyVO reply);
	
	public List<ReplyVO> getListWithPaging(@Param("cri") Criteira cri, @Param("bno") Long bno);
	
	public int getCountByBno(Long bno);
}
