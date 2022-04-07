package me.kickscar.msa.service.guestbook.service;

import me.kickscar.msa.service.guestbook.repository.GuestbookRepository;
import me.kickscar.msa.service.guestbook.vo.GuestbookVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GuestbookService {
	final GuestbookRepository guestbookRepository;

	public GuestbookService(GuestbookRepository guestbookRepository) {
		this.guestbookRepository = guestbookRepository;
	}

	public List<GuestbookVo> getMessageList(Long no) {
		return guestbookRepository.findAll(no);
	}
	
	@Transactional
	public boolean deleteMessage(Long no, String password) {
		GuestbookVo vo = new GuestbookVo();
		vo.setNo(no);
		vo.setPassword(password);
		
		return guestbookRepository.delete(vo);
	}

	public boolean addMessage(GuestbookVo vo) {
		return guestbookRepository.insert(vo);
	}
}
