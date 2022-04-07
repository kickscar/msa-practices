package me.kickscar.msa.service.gallery.service;

import me.kickscar.msa.service.gallery.repository.GalleryRepository;
import me.kickscar.msa.service.gallery.vo.GalleryVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GalleryService {

	private final GalleryRepository galleryRepository;

	public GalleryService(GalleryRepository galleryRepository) {
		this.galleryRepository = galleryRepository;
	}

	public List<GalleryVo> getGalleryImages() {
		return galleryRepository.findAll();
	}
	
	public Boolean deleteGalleryImage(Long no) {
		return galleryRepository.delete(no);
	}
	
	public Boolean addGalleryImage(GalleryVo vo) {
		return galleryRepository.insert(vo);
	}
}
