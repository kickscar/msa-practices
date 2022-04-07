package me.kickscar.msa.service.guestbook.repository;

import me.kickscar.msa.service.guestbook.vo.GuestbookVo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GuestbookRepository {
	private final SqlSession sqlSession;

	public GuestbookRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public List<GuestbookVo> findAll(Long no) {
		return sqlSession.selectList("guestbook.findAllByNo", no);
	}

	public Boolean insert(GuestbookVo vo) {
		return 1 == sqlSession.insert("guestbook.insert", vo);
	}
	
	public Boolean delete(GuestbookVo vo) {
		return 1 == sqlSession.delete("guestbook.delete", vo);
	}
}