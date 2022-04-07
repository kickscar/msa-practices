package me.kickscar.msa.service.emaillist.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.kickscar.msa.service.emaillist.vo.EmaillistVo;

@Repository
public class EmaillistRepository {
	
	private final SqlSession sqlSession;

	public EmaillistRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public List<EmaillistVo> findAll(String keyword) {
		return sqlSession.selectList("emaillist.findAll", keyword);
	}
	
	public Boolean insert(EmaillistVo vo) {
		return sqlSession.insert("emaillist.insert", vo) == 1;		
	}
}
