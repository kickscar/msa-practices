package com.poscodx.msa.service.gallery.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.poscodx.msa.service.gallery.vo.GalleryVo;

import java.util.List;

@Repository
public class GalleryRepository {

	private final SqlSession sqlSession;

	public GalleryRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public Boolean insert(GalleryVo vo) {
		return 1 == sqlSession.insert("gallery.insert", vo);
	}

	public Boolean delete(Long no) {
		return 1 == sqlSession.delete("gallery.delete", no);
	}

	public List<GalleryVo> findAll() {
		return sqlSession.selectList("gallery.findAll");
	}
}
