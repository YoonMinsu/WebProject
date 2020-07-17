package org.zerock.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.Criteira;
import org.zerock.domain.ReplyVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyMapperTests {
	
	private Long[] bnoArr = { 23531L, 23532L, 23533L,23534L,23535L };
	
	@Setter(onMethod_ = {@Autowired})
	private ReplyMapper mapper;
	
//	@Test
//	public void testMapper() {
//		
//		log.info(mapper);
//	}
	
	
//	@Test
//	public void testCreate() {
//		IntStream.rangeClosed(1, 10).forEach(i -> {
//			ReplyVO vo = new ReplyVO();
//			
//			vo.setBno(bnoArr[i % 5]);
//			vo.setReply("댓글 테스트 " + i);
//			vo.setReplyer("replyer" + i);
//			
//			mapper.insert(vo);
//		});
//	}
	
//	@Test
//	public void testRead() {
//		Long targetBno = 2L;
//		
//		ReplyVO vo = mapper.read(targetBno);
//		
//		log.info(vo);
//	}
	
//	@Test
//	public void testDelete() {
//		Long targetRno = 1L;
//		mapper.delete(targetRno);
//	}
	
//	@Test
//	public void testUpdate() {
//		Long targetBno = 10L;
//		
//		ReplyVO vo = mapper.read(targetBno);
//		
//		vo.setReply("Update Reply");
//		
//		int count = mapper.update(vo);
//		log.info("UPDATE COUNT" + count);
//	}
	
//	@Test
//	public void testList() {
//		Criteira cri = new Criteira();
//		
//		List<ReplyVO> replies = mapper.getListWithPaging(cri, bnoArr[0]);
//		
//		replies.forEach(reply -> log.info(reply));
//	}
	
	@Test
	public void testList2() {
		Criteira cri = new Criteira( 1, 10 );
		
		List<ReplyVO> replies = mapper.getListWithPaging(cri, 23582L);
		
		replies.forEach( reply -> log.info(reply));
	}
	
	
}




















