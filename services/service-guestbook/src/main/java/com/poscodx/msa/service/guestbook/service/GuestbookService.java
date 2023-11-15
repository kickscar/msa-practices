package com.poscodx.msa.service.guestbook.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poscodx.msa.service.guestbook.repository.GuestbookRepository;
import com.poscodx.msa.service.guestbook.vo.GuestbookVo;

@Service
public class GuestbookService {
	final GuestbookRepository guestbookRepository;

	public GuestbookService(GuestbookRepository guestbookRepository) {
		this.guestbookRepository = guestbookRepository;
	}

	public List<GuestbookVo> getContentsList(Long no) {
		return guestbookRepository.findAll(no);
	}
	
	@Transactional
	public boolean deleteContents(Long no, String password) {
		GuestbookVo vo = new GuestbookVo();
		vo.setNo(no);
		vo.setPassword(password);
		
		return guestbookRepository.delete(vo);
	}

	public boolean addContents(GuestbookVo vo) {
		return guestbookRepository.insert(vo);
	}
}
